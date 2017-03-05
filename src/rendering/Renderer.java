package rendering;

import java.util.ArrayList;

import audio.AudioManager;
import enums.TeamEnum;
import gui.GUIManager;
import javafx.animation.AnimationTimer;
import javafx.scene.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import networking.client.ClientReceiver;
import offlineLogic.OfflineGameMode;
import offlineLogic.OfflineTeamMatchMode;
import physics.Bullet;
import physics.CollisionsHandler;
import physics.KeyPressListener;
import physics.KeyReleaseListener;
import physics.MouseListener;
import physics.OfflinePlayer;
import players.GeneralPlayer;

/**
 * A scene of a game instance. All assets are drawn on a <i>view</i> pane.
 *
 * @author Artur Komoter
 */
public class Renderer extends Scene
{
	static Pane view = new Pane();
	private static PauseMenu pauseMenu;
	private static PauseSettingsMenu settingsMenu;
	private static Overlay overlay;
	private double scale = 1;
	private GeneralPlayer player;

	/**
	 * Renders a game instance by loading the selected map, spawning the players and responding to changes in game logic.
	 *
	 * @param mapName Name of the selected map
	 */
	public Renderer(String mapName, ClientReceiver receiver, GUIManager m)
	{
		super(view, 1024, 576);
		setFill(Color.BLACK);
		setCursor(Cursor.CROSSHAIR);
		view.setStyle("-fx-background-color: black;");
		pauseMenu = new PauseMenu(m);
		settingsMenu = new PauseSettingsMenu(m);
		overlay = new Overlay(m);

		//16:9 aspect ratio
		widthProperty().addListener(observable ->
		{
			scale = getWidth() / 1024;
			view.setScaleX(scale);
			view.setScaleY((getWidth() * 0.5625) / 576);
		});

		Map.load("res/maps/" + mapName + ".json");

		player = receiver.getClientPlayer();
		player.setCache(true);
		player.setCacheHint(CacheHint.SCALE_AND_ROTATE);
		view.getChildren().add(player);

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

		KeyPressListener keyPressListener = new KeyPressListener(player);
		KeyReleaseListener keyReleaseListener = new KeyReleaseListener(player);
		MouseListener mouseListener = new MouseListener(player);

		setOnKeyPressed(keyPressListener);
		setOnKeyReleased(keyReleaseListener);
		setOnMouseDragged(mouseListener);
		setOnMouseMoved(mouseListener);
		setOnMousePressed(mouseListener);
		setOnMouseReleased(mouseListener);

		view.getChildren().add(overlay);

		ArrayList<Bullet> pellets = new ArrayList<>();
		new AnimationTimer()
		{
			@Override
			public void handle(long now)
			{
				updateView();

				view.getChildren().removeAll(pellets);
				pellets.clear();

				for(Bullet pellet : player.getBullets())
				{
					if(pellet.isActive())
						pellets.add(pellet);
				}
				for(GeneralPlayer player : receiver.getMyTeam())
					pellets.addAll(player.getBullets());
				for(GeneralPlayer player : receiver.getEnemies())
					pellets.addAll(player.getBullets());
				view.getChildren().addAll(pellets);

				player.tick();
			}
		}.start();
	}

	/**
	 * Renders a game instance by loading the selected map, spawning the players and responding to changes in game logic.
	 *
	 * @param mapName Name of the selected map
	 */
	public Renderer(String mapName, AudioManager audio, GUIManager m)
	{
		super(view, 1024, 576);
		setFill(Color.BLACK);
		setCursor(Cursor.CROSSHAIR);
		view.setStyle("-fx-background-color: black;");
		pauseMenu = new PauseMenu(m);
		settingsMenu = new PauseSettingsMenu(m);
		overlay = new Overlay(m);

		//16:9 aspect ratio
		widthProperty().addListener(observable ->
		{
			scale = getWidth() / 1024;
			view.setScaleX(scale);
			view.setScaleY((getWidth() * 0.5625) / 576);
		});

		Map map = Map.load("res/maps/" + mapName + ".json");

		ArrayList<GeneralPlayer> players = new ArrayList<>();

		CollisionsHandler collisionsHandler = new CollisionsHandler(map);

		player = new OfflinePlayer(map.getSpawns()[0].x * 64, map.getSpawns()[0].y * 64, 0, false, map, audio, TeamEnum.RED, collisionsHandler);

		players.add(player);
		players.addAll(player.getTeamPlayers());
		players.addAll(player.getEnemies());
		players.forEach(p -> {
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

		KeyPressListener keyPressListener = new KeyPressListener(player);
		KeyReleaseListener keyReleaseListener = new KeyReleaseListener(player);
		MouseListener mouseListener = new MouseListener(player);

		setOnKeyPressed(keyPressListener);
		setOnKeyReleased(keyReleaseListener);
		setOnMouseDragged(mouseListener);
		setOnMouseMoved(mouseListener);
		setOnMousePressed(mouseListener);
		setOnMouseReleased(mouseListener);

		view.getChildren().add(overlay);

		new AnimationTimer()
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
		}.start();
	}

	private void updateView()
	{
		view.setLayoutX(((getWidth() / 2) - player.getImage().getWidth() - player.getLayoutX()) * scale);
		view.setLayoutY(((getHeight() / 2) - player.getImage().getHeight() - player.getLayoutY()) * scale);
		if(view.getChildren().contains(pauseMenu))
		{
			pauseMenu.setLayoutX(player.getLayoutX() + player.getImage().getWidth() - getWidth() / 2);
			pauseMenu.setLayoutY(player.getLayoutY() + player.getImage().getHeight() - getHeight() / 2);
		} else if(view.getChildren().contains(settingsMenu))
		{
			settingsMenu.setLayoutX(player.getLayoutX() + player.getImage().getWidth() - getWidth() / 2);
			settingsMenu.setLayoutY(player.getLayoutY() + player.getImage().getHeight() - getHeight() / 2);
		} else if(view.getChildren().contains(overlay)) {
			overlay.setLayoutX(player.getLayoutX() + player.getImage().getWidth() - getWidth() / 2);
			overlay.setLayoutY(player.getLayoutY() + player.getImage().getHeight() + (getHeight() / 2 - overlay.getHeight()));
		}
	}

	public static void togglePauseMenu()
	{
		if(!pauseMenu.opened) {
			view.getChildren().add(pauseMenu);
			view.getChildren().remove(overlay);
		} else {
			view.getChildren().remove(pauseMenu);
			view.getChildren().add(overlay);
		}
		pauseMenu.opened = !pauseMenu.opened;
	}

	public static void toggleSettingsMenu()
	{
		if(!settingsMenu.opened) {
			view.getChildren().remove(pauseMenu);
			view.getChildren().add(settingsMenu);
		} else {
			view.getChildren().remove(settingsMenu);
			view.getChildren().add(pauseMenu);
		}
		settingsMenu.opened = !settingsMenu.opened;
	}

	public static boolean getPauseMenuState()
	{
		return pauseMenu.opened;
	}

	public static boolean getSettingsMenuState()
	{
		return settingsMenu.opened;
	}
}
