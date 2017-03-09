package rendering;

import enums.Team;
import gui.GUIManager;
import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import networking.client.ClientReceiver;
import physics.*;
import players.GeneralPlayer;
import players.OfflinePlayer;

import java.util.ArrayList;
import java.util.Random;

import static players.GeneralPlayer.playerHeadX;
import static players.GeneralPlayer.playerHeadY;

/**
 * A scene of a game instance. All assets are drawn on a <i>view</i> pane. There are two instances of <code>SubScene</code> for the pause menu and the settings menu, and a <code>SubScene</code> for the in-game head up display.
 *
 * @author Artur Komoter
 */
public class Renderer extends Scene
{
	static Pane view = new Pane();
	private static PauseMenu pauseMenu;
	private static PauseSettingsMenu settingsMenu;
	private static HeadUpDisplay hud;
	private static Map map;
	private AnimationTimer timer;
	private GeneralPlayer player;
	private GUIManager guiManager;

	/**
	 * Renders an offline game instance by loading the selected map, spawning the AI players and responding to changes in game logic.
	 *
	 * @param mapName    Name of the selected map
	 * @param guiManager GUI manager that creates this object
	 */
	public Renderer(String mapName, GUIManager guiManager)
	{
		super(view, guiManager.getStage().getWidth(), guiManager.getStage().getHeight());
		this.guiManager = guiManager;
		init(mapName);

		ArrayList<GeneralPlayer> players = new ArrayList<>();

		CollisionsHandler collisionsHandler = new CollisionsHandler(map);

		player = new OfflinePlayer(map.getSpawns()[0].x * 64, map.getSpawns()[0].y * 64, 0, map, guiManager, Team.RED, collisionsHandler);

		players.add(player);
		players.addAll(player.getTeamPlayers());
		players.addAll(player.getEnemies());
		players.forEach(player -> {
			player.setEffect(new DropShadow(16, 0, 0, Color.BLACK));
			view.getChildren().add(player.getNameTag());
		});
		view.getChildren().remove(player.getNameTag());
		view.getChildren().addAll(players);

		//provisional way to differ enemies and team players
		ArrayList<GeneralPlayer> redTeam = new ArrayList<>();
		ArrayList<GeneralPlayer> blueTeam = new ArrayList<>();
		for(GeneralPlayer p : players)
		{
			if(p.getTeam() == Team.RED)
				redTeam.add(p);
			else
				blueTeam.add(p);
		}
		for(GeneralPlayer p : players)
		{
			if(p.getTeam() == Team.RED)
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

		initListeners();

		timer = new AnimationTimer()
		{
			long lastSecond = 0;

			@Override
			public void handle(long now)
			{
				if(now - lastSecond >= 1000000000)
				{
					hud.tick();
					lastSecond = now;
				}
				updateView();

				for(GeneralPlayer player : players)
				{
					for(Bullet pellet : player.getBullets())
					{
						if(pellet.isActive())
						{
							if(!view.getChildren().contains(pellet))
								view.getChildren().add(view.getChildren().size() - 2, pellet);
						}
						else if(view.getChildren().contains(pellet))
						{
							if(pellet.getCollision() != null)
								generateSpray(pellet, player.getTeam());
							view.getChildren().remove(pellet);
						}
					}
					player.tick();
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
		this.guiManager = guiManager;
		init(mapName);

		player = receiver.getClientPlayer();
		view.getChildren().add(player);
		view.getChildren().addAll(receiver.getMyTeam());
		view.getChildren().addAll(receiver.getEnemies());

		initListeners();

		ArrayList<Bullet> pellets = new ArrayList<>();
		timer = new AnimationTimer()
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
		};
		timer.start();
	}

	/**
	 * Toggles the pause menu whilst in-game
	 */
	public static void togglePauseMenu()
	{
		if(!pauseMenu.opened)
			view.getChildren().add(pauseMenu);
		else
			view.getChildren().remove(pauseMenu);
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
	 *
	 * @return <code>true</code> if the pause menu is active, <code>false</code> otherwise
	 */
	public static boolean getPauseMenuState()
	{
		return pauseMenu.opened;
	}

	/**
	 * Get the current state of the settings scene in the pause menu
	 *
	 * @return <code>true</code> if the settings scene is active, <code>false</code> otherwise
	 */
	public static boolean getSettingsMenuState()
	{
		return settingsMenu.opened;
	}

	/**
	 * Increment the score on the HUD for a given team by a given amount
	 *
	 * @param team   The team that has scored
	 * @param amount The amount to increase the score by
	 */
	public static void incrementScore(Team team, int amount)
	{
		hud.incrementScore(team, amount);
	}

	public static void destroy(Renderer renderer)
	{
		if(renderer != null)
		{
			renderer.timer.stop();
			view = new Pane();
			PauseMenu.p = new GridPane();
			pauseMenu = null;
			PauseSettingsMenu.p = new GridPane();
			settingsMenu = null;
			HeadUpDisplay.view = new BorderPane();
			hud = null;
			map = null;
		}
	}

	private static void generateSpray(Bullet pellet, Team team)
	{
		WritableImage paint = new WritableImage(64, 64);
		PixelWriter pixelWriter = paint.getPixelWriter();

		Random random = new Random();
		double probability = 0.1;
		for(int i = 0; i < 60; i++)
		{
			for(int j = 0; j < 60; j++)
				if(random.nextDouble() < probability)
					pixelWriter.setArgb(i, j, (team == Team.RED ? java.awt.Color.RED : java.awt.Color.BLUE).getRGB());
		}

		ImageView imageView = new ImageView(paint);
		imageView.relocate(pellet.getCollision().getX(), pellet.getCollision().getY());
		imageView.setCache(true);
		view.getChildren().add(view.getChildren().size() - 2, imageView);
	}

	private void init(String mapName)
	{
		setFill(Color.BLACK);
		setCursor(Cursor.CROSSHAIR);

		view.setStyle("-fx-background-color: black;");
		view.getStylesheets().add("styles/menu.css");
		pauseMenu = new PauseMenu(guiManager);
		settingsMenu = new PauseSettingsMenu(guiManager);
		map = Map.load(mapName);
	}

	private void initListeners()
	{
		KeyPressListener keyPressListener = new KeyPressListener(player);
		KeyReleaseListener keyReleaseListener = new KeyReleaseListener(player);
		MouseListener mouseListener = new MouseListener(player);

		setOnKeyPressed(keyPressListener);
		setOnKeyReleased(keyReleaseListener);
		setOnMouseDragged(mouseListener);
		setOnMouseMoved(mouseListener);
		setOnMousePressed(mouseListener);
		setOnMouseReleased(mouseListener);

		hud = new HeadUpDisplay(guiManager, player.getTeam());
		view.getChildren().add(hud);
		hud.toFront();
	}

	private void updateView()
	{
		view.relocate((getWidth() / 2) - playerHeadX - player.getLayoutX(), (getHeight() / 2) - playerHeadY - player.getLayoutY());
		if (hud != null)
			hud.relocate(player.getLayoutX() + playerHeadX - getWidth() / 2, player.getLayoutY() + playerHeadY - getHeight() / 2);

		if(view.getChildren().contains(pauseMenu))
			pauseMenu.relocate(player.getLayoutX() + playerHeadX - getWidth() / 2, player.getLayoutY() + playerHeadY - getHeight() / 2);

		if(view.getChildren().contains(settingsMenu))
			settingsMenu.relocate(player.getLayoutX() + playerHeadX - getWidth() / 2, player.getLayoutY() + playerHeadY - getHeight() / 2);
	}
}
