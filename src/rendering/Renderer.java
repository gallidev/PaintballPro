package rendering;


import enums.TeamEnum;
import integrationClient.ClientInputSender;
import gui.GUIManager;
import javafx.animation.AnimationTimer;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import networking.client.ClientReceiver;
import physics.*;
import players.GeneralPlayer;
import players.GhostPlayer;

import java.util.ArrayList;

import static players.GeneralPlayer.playerHeadX;
import static players.GeneralPlayer.playerHeadY;

/**
 * A scene of a game instance. All assets are drawn on a <i>view</i> pane.
 *
 * @author Artur Komoter
 */
public class Renderer extends Scene
{
	static Pane view = new Pane();
	static AnimationTimer timer;
	private static PauseMenu pauseMenu;
	private static PauseSettingsMenu settingsMenu;
	private static HeadUpDisplay hud;
	private GeneralPlayer player;
	private GhostPlayer cPlayer;
	private ClientInputSender inputSender;
	private Map map;
	private InputHandler inputHandler;


	/**
	 * Renders an offline game instance by loading the selected map, spawning the players and responding to changes in game logic.
	 *
	 * @param mapName Name of the selected map
	 * @param guiManager GUI manager that creates this object
	 */
	public Renderer(String mapName, GUIManager guiManager)
	{

		super(view, guiManager.getStage().getWidth(), guiManager.getStage().getHeight());
		init(guiManager, mapName);

		ArrayList<GeneralPlayer> players = new ArrayList<>();

		CollisionsHandlerGeneralPlayer collisionsHandler = new CollisionsHandlerGeneralPlayer(map);

		player = new OfflinePlayer(map.getSpawns()[0].x * 64, map.getSpawns()[0].y * 64, 0, false, map, guiManager.getAudioManager(), TeamEnum.RED, collisionsHandler);

		players.add(player);
		players.addAll(player.getTeamPlayers());
		players.addAll(player.getEnemies());
		players.forEach(p ->
		{
			p.setCache(true);
			p.setCacheHint(CacheHint.SCALE_AND_ROTATE);
			p.setEffect(new DropShadow(16, 0, 0, Color.BLACK));
		});
		view.getChildren().addAll(players);

		//provisional way to differ enemies and team players
		ArrayList<GeneralPlayer> redTeam = new ArrayList<>();
		ArrayList<GeneralPlayer> blueTeam = new ArrayList<>();
		for(GeneralPlayer p : players)
		{
			if(p.getTeam() == TeamEnum.RED)
				redTeam.add(p);
			else
				blueTeam.add(p);
		}
		for(GeneralPlayer p : players)
		{
			if(p.getTeam() == TeamEnum.RED)
			{
				p.setEnemies(blueTeam);
				p.setTeamPlayers(redTeam);
			}
			else
			{
				p.setEnemies(redTeam);
				p.setTeamPlayers(blueTeam);
			}
		}

		collisionsHandler.setBlueTeam(blueTeam);
		collisionsHandler.setRedTeam(redTeam);
		//OfflineGameMode game = new OfflineTeamMatchMode((OfflinePlayer) player);
		//game.start();

		initListeners();

		timer = new AnimationTimer()
		{
			@Override
			public void handle(long now)
			{
				updateView();

				for(GeneralPlayer player : players)
				{
					player.tick();
					for(Bullet pellet : player.getBullets())
					{
						if(pellet.isActive())
						{
							if(!view.getChildren().contains(pellet))
								view.getChildren().add(pellet);
						}
						else if(view.getChildren().contains(pellet))
							view.getChildren().remove((pellet));
					}
				}
			}
		};
		timer.start();
	}

	/**
	 * Renders an online game instance by loading the selected map, receiving data from the client receiver and responding to changes in game logic.
	 *
	 * @param mapName    Name of the selected map
	 * @param receiver   Client receiver for communication with the game server
	 * @param guiManager GUI manager that creates this object
	 */
	public Renderer(String mapName, ClientReceiver receiver, GUIManager guiManager)
	{
		super(view, guiManager.getStage().getWidth(), guiManager.getStage().getHeight());
		init(guiManager, mapName);

		cPlayer = receiver.getClientPlayer();
		cPlayer.setCache(true);
		cPlayer.setCacheHint(CacheHint.SCALE_AND_ROTATE);

		ArrayList<GhostPlayer> allplayers = receiver.getAllPlayers();
		view.getChildren().add(cPlayer);

		receiver.getMyTeam().forEach(localPlayer ->
		{
			localPlayer.setCache(true);
			localPlayer.setCacheHint(CacheHint.SCALE_AND_ROTATE);
		});

		view.getChildren().addAll(receiver.getMyTeam());

		receiver.getEnemies().forEach(localPlayer ->
		{
			localPlayer.setCache(true);
			localPlayer.setCacheHint(CacheHint.SCALE_AND_ROTATE);
		});

		view.getChildren().addAll(receiver.getEnemies());

		InputHandler inputHandler = new InputHandler();
		KeyPressListener keyPressListener = new KeyPressListener(inputHandler);
		KeyReleaseListener keyReleaseListener = new KeyReleaseListener(inputHandler);
		MouseListener mouseListener = new MouseListener(inputHandler);

		setOnKeyPressed(keyPressListener);
		setOnKeyReleased(keyReleaseListener);
		setOnMouseDragged(mouseListener);
		setOnMouseMoved(mouseListener);
		setOnMousePressed(mouseListener);
		setOnMouseReleased(mouseListener);

		inputSender = new ClientInputSender(receiver.getUdpClient(),inputHandler, cPlayer.getPlayerId());

		inputSender.startSending();

		Group displayBullets = new Group();

		view.getChildren().add(displayBullets);

		timer = new AnimationTimer()
		{
			@Override
			public void handle(long now)
			{
				updateViewCPlayer();
				displayBullets.getChildren().clear();
				for(GhostPlayer player : allplayers)
				{
					for(GhostBullet pellet : player.getFiredBullets())
					{
						displayBullets.getChildren().add(pellet);
					}
				}
			}
		};
		timer.start();
	}

	/**
	 * Toggles the pause menu whilst in-game
	 */
	public static void togglePauseMenu()
	{
		if(!pauseMenu.opened) {
			view.getChildren().add(pauseMenu);
		} else {
			view.getChildren().remove(pauseMenu);
		}
		pauseMenu.opened = !pauseMenu.opened;
	}

	/**
	 * Toggles the settings scene from the pause menu whilst in-game
	 */
	public static void toggleSettingsMenu()
	{
		if(!settingsMenu.opened)
		{
			view.getChildren().remove(pauseMenu);
			view.getChildren().add(settingsMenu);
		}
		else
		{
			view.getChildren().remove(settingsMenu);
			view.getChildren().add(pauseMenu);
		}
		settingsMenu.opened = !settingsMenu.opened;
	}

	/**
	 * Get the current state of the pause menu
	 * @return <code>true</code> if the pause menu is active, <code>false</code> otherwise
	 */
	public static boolean getPauseMenuState()
	{
		return pauseMenu.opened;
	}

	/**
	 * Get the current state of the settings scene in the pause menu
	 * @return <code>true</code> if the settings scene is active, <code>false</code> otherwise
	 */
	public static boolean getSettingsMenuState()
	{
		return settingsMenu.opened;
	}

	private void init(GUIManager guiManager, String mapName)
	{
		setFill(Color.BLACK);
		setCursor(Cursor.CROSSHAIR);
		view.setStyle("-fx-background-color: black;");
		pauseMenu = new PauseMenu(guiManager);
		settingsMenu = new PauseSettingsMenu(guiManager);
		hud = new HeadUpDisplay();
		view.getChildren().add(hud);

		map = Map.load("res/maps/" + mapName + ".json");
	}

	private void initListeners()
	{
		KeyPressListener keyPressListener = new KeyPressListener(inputHandler);
		KeyReleaseListener keyReleaseListener = new KeyReleaseListener(inputHandler);
		MouseListener mouseListener = new MouseListener(inputHandler);

		setOnKeyPressed(keyPressListener);
		setOnKeyReleased(keyReleaseListener);
		setOnMouseDragged(mouseListener);
		setOnMouseMoved(mouseListener);
		setOnMousePressed(mouseListener);
		setOnMouseReleased(mouseListener);

		hud.toFront();
	}

	private void updateView()
	{
		view.relocate((getWidth() / 2) - playerHeadX - player.getLayoutX(), (getHeight() / 2) - playerHeadY - player.getLayoutY());
		hud.relocate(player.getLayoutX() + playerHeadX - getWidth() / 2, player.getLayoutY() + playerHeadY - getHeight() / 2);

		if(view.getChildren().contains(pauseMenu))
			pauseMenu.relocate(player.getLayoutX() + playerHeadX - getWidth() / 2, player.getLayoutY() + playerHeadY - getHeight() / 2);

		if(view.getChildren().contains(settingsMenu))
			settingsMenu.relocate(player.getLayoutX() + playerHeadX - getWidth() / 2, player.getLayoutY() + playerHeadY - getHeight() / 2);
	}

	private void updateViewCPlayer() {
		view.relocate((getWidth() / 2) - playerHeadX - cPlayer.getLayoutX(), (getHeight() / 2) - playerHeadY - cPlayer.getLayoutY());

		if(view.getChildren().contains(pauseMenu))
			pauseMenu.relocate(cPlayer.getLayoutX() + playerHeadX - getWidth() / 2, cPlayer.getLayoutY() + playerHeadY - getHeight() / 2);

		if(view.getChildren().contains(settingsMenu))
			settingsMenu.relocate(cPlayer.getLayoutX() + playerHeadX - getWidth() / 2, cPlayer.getLayoutY() + playerHeadY - getHeight() / 2);
	}


	@Override
	protected void finalize() throws Throwable
	{
		view = null;
		pauseMenu = null;
		settingsMenu = null;
		hud = null;
		timer = null;
		super.finalize();
	}
}
