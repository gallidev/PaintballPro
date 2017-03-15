package rendering;

import static players.GhostPlayer.playerHeadX;
import static players.GhostPlayer.playerHeadY;

import java.util.ArrayList;
import java.util.Random;

import com.sun.org.apache.regexp.internal.REDebugCompiler;

import enums.TeamEnum;
import gui.GUIManager;
import integrationClient.ClientInputSender;
import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import logic.GameMode;
import networking.client.ClientReceiver;
import physics.Bullet;
import physics.CollisionsHandler;
import physics.GhostBullet;
import physics.InputHandler;
import physics.KeyPressListener;
import physics.KeyReleaseListener;
import physics.MouseListener;
import players.EssentialPlayer;
import players.GhostPlayer;
import players.OfflinePlayer;
import serverLogic.CaptureTheFlagMode;
import serverLogic.Team;
import serverLogic.TeamMatchMode;

/**
 * A scene of a game instance. All assets are drawn on a <i>view</i> pane. There are two instances of <code>SubScene</code> for the pause menu and the settings menu, and a <code>SubScene</code> for the in-game head up display.
 *
 * @author Artur Komoter
 */
public class Renderer extends Scene
{
	static Pane view = new Pane();
	static GhostPlayer cPlayer;
	static OfflinePlayer player;
	private static PauseMenu pauseMenu;
	private static PauseSettingsMenu settingsMenu;
	private static HeadUpDisplay hud;
	private static Map map;
	private static int paintIndex;
	private AnimationTimer timer;
	private GUIManager guiManager;
	private boolean singlePlayer = false;

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
		singlePlayer = true;

		CollisionsHandler collisionsHandler = new CollisionsHandler(map);
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

		player = new OfflinePlayer(map.getSpawns()[0].x * 64, map.getSpawns()[0].y * 64, 0, map, guiManager, TeamEnum.RED, collisionsHandler, inputHandler);
		ArrayList<EssentialPlayer> players = new ArrayList<>();
		
		players.addAll(player.getMyTeam().getMembers());
		players.addAll(player.getOppTeam().getMembers());

		view.getChildren().addAll(players);
		collisionsHandler.setPlayers(players);

		hud = new HeadUpDisplay(guiManager, player.getTeam());
		view.getChildren().add(hud);
		hud.toFront();
		
		GameMode gameLoop = initGame(player);
		
		gameLoop.start();

		timer = new AnimationTimer()
		{
			long lastSecond = 0;
			int timeLeft = 180;

			@Override
			public void handle(long now)
			{
				updateView();
				for(EssentialPlayer player : players)
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
//				if(now - lastSecond >= 1000000000)
//				{
//					hud.tick(timeLeft--);
//					lastSecond = now;
//				}
				hud.tick((int) gameLoop.getRemainingTime());

				
				//update the scores
				if (hud != null){
					incrementScore(TeamEnum.RED, gameLoop.getRedTeam().getScore());
					incrementScore(TeamEnum.BLUE, gameLoop.getBlueTeam().getScore());
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

		cPlayer = receiver.getClientPlayer();
		ArrayList<GhostPlayer> players = receiver.getAllPlayers();
		view.getChildren().add(cPlayer);
		view.getChildren().addAll(receiver.getMyTeam());
		receiver.getMyTeam().forEach(player -> view.getChildren().add(player.getNameTag()));
		view.getChildren().addAll(receiver.getEnemies());
		receiver.getEnemies().forEach(player -> view.getChildren().add(player.getNameTag()));

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

		hud = new HeadUpDisplay(guiManager, cPlayer.getTeam());
		view.getChildren().add(hud);
		hud.toFront();

		ClientInputSender inputSender = new ClientInputSender(receiver.getUdpClient(), inputHandler, cPlayer.getPlayerId());
		inputSender.startSending();
		Group displayBullets = new Group();
		view.getChildren().add(displayBullets);

		timer = new AnimationTimer()
		{
			@Override
			public void handle(long now)
			{
				updateView();
				for(GhostPlayer player : players)
				{
					for(GhostBullet pellet : player.getFiredBullets())
					{
						if(!displayBullets.getChildren().contains(pellet))
							displayBullets.getChildren().add(pellet);
					}
				}
			}
		};
		timer.start();
	}

	private GameMode initGame(OfflinePlayer player) {
		Team red = player.getMyTeam();
		red.addMember(player);
		
		Team blue = player.getOppTeam();
		
		switch (map.getGameMode()){

			case ELIMINATION : return new TeamMatchMode(red, blue);
			case CAPTURETHEFLAG : return new CaptureTheFlagMode(red, blue);
		}
		return null;
		
		
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

	public static HeadUpDisplay getHud()
	{
		return hud;
	}

	/**
	 * Increment the score on the HUD for a given team by a given amount
	 *
	 * @param team   The team that has scored
	 * @param amount The amount to increase the score by
	 */
	public static void incrementScore(TeamEnum team, int amount)
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
			cPlayer = null;
			map = null;
			paintIndex = 0;
		}
	}

	private static void generateSpray(Bullet pellet, TeamEnum team)
	{
		WritableImage paint = new WritableImage(64, 64);
		PixelWriter pixelWriter = paint.getPixelWriter();
		Random random = new Random();
		double probability = 0.1;
		for(int i = 1; i < 63; i++)
		{
			for(int j = 1; j < 63; j++)
				if(random.nextDouble() < probability)
					pixelWriter.setArgb(i, j, (team == TeamEnum.RED ? java.awt.Color.RED : java.awt.Color.BLUE).getRGB());
		}
		ImageView imageView = new ImageView(paint);
		imageView.relocate(pellet.getCollision().getX(), pellet.getCollision().getY());
		imageView.setCache(true);
		view.getChildren().add(paintIndex, imageView);
	}

	private void init(String mapName)
	{
		setFill(Color.BLACK);
		setCursor(Cursor.CROSSHAIR);
		view.setStyle("-fx-background-color: black;");
		view.getStylesheets().add("styles/menu.css");
		String[] resolution = GUIManager.getUserSettings().getResolution().split("x");
		view.setScaleX(Double.parseDouble(resolution[0]) / 1024);
		view.setScaleY(Double.parseDouble(resolution[1]) / 576);
		pauseMenu = new PauseMenu(guiManager);
		settingsMenu = new PauseSettingsMenu(guiManager);
		map = Map.load(mapName);
		paintIndex = view.getChildren().size();
	}

	private void updateView()
	{
		if(singlePlayer)
		{
			view.relocate(((getWidth() / 2) - playerHeadX - player.getLayoutX()) * view.getScaleX(), ((getHeight() / 2) - playerHeadY - player.getLayoutY()) * view.getScaleY());
			hud.relocate(player.getLayoutX() + playerHeadX - getWidth() / 2, player.getLayoutY() + playerHeadY - getHeight() / 2);
			if(view.getChildren().contains(pauseMenu))
				pauseMenu.relocate(player.getLayoutX() + playerHeadX - getWidth() / 2, player.getLayoutY() + playerHeadY - getHeight() / 2);
			if(view.getChildren().contains(settingsMenu))
				settingsMenu.relocate(player.getLayoutX() + playerHeadX - getWidth() / 2, player.getLayoutY() + playerHeadY - getHeight() / 2);
		}
		else
		{
			//System.out.println("cPlayer x and y :" + cPlayer.getLayoutX() + "  " + cPlayer.getLayoutY() );
			view.relocate(((getWidth() / 2) - playerHeadX - cPlayer.getLayoutX()) * view.getScaleX(), ((getHeight() / 2) - playerHeadY - cPlayer.getLayoutY()) * view.getScaleY());
			hud.relocate(cPlayer.getLayoutX() + playerHeadX - getWidth() / 2, cPlayer.getLayoutY() + playerHeadY - getHeight() / 2);
			if(view.getChildren().contains(pauseMenu))
				pauseMenu.relocate(cPlayer.getLayoutX() + playerHeadX - getWidth() / 2, cPlayer.getLayoutY() + playerHeadY - getHeight() / 2);
			if(view.getChildren().contains(settingsMenu))
				settingsMenu.relocate(cPlayer.getLayoutX() + playerHeadX - getWidth() / 2, cPlayer.getLayoutY() + playerHeadY - getHeight() / 2);
		}
	}
}