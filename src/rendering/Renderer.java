package rendering;

import enums.TeamEnum;
import gui.GUIManager;
import integration.client.ClientInputSender;
import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import logic.GameMode;
import logic.server.CaptureTheFlagMode;
import logic.server.Team;
import logic.server.TeamMatchMode;
import networking.client.ClientReceiver;
import physics.*;
import players.ClientPlayer;
import players.EssentialPlayer;
import players.OfflinePlayer;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;

import static players.EssentialPlayer.PLAYER_HEAD_X;
import static players.EssentialPlayer.PLAYER_HEAD_Y;

/**
 * A scene of a game instance. All assets are drawn on a <i>view</i> pane. There are two instances of <code>SubScene</code> for the pause menu and the settings menu, and a <code>SubScene</code> for the in-game head up display.
 *
 * @author Artur Komoter
 */
public class Renderer extends Scene
{
	public static double TARGET_FPS = 60.0;

	static Pane view = new Pane();

	ClientPlayer cPlayer;
	OfflinePlayer player;

	//attributes for multiplayer
	private int blueScore, redScore;
	private int timeRemaining;

	private PauseMenu pauseMenu;
	private PauseSettingsMenu settingsMenu;
	private HeadUpDisplay hud;
	private Map map;
	private int paintIndex;
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
		super(view, guiManager.width, guiManager.height);
		this.guiManager = guiManager;
		init(mapName);
		singlePlayer = true;

		if(map.gameMode == enums.GameMode.CAPTURETHEFLAG)
		{
			map.flag = new Flag(map.flagLocations);
			view.getChildren().add(map.flag);
		}

		map.powerups = new Powerup[]{new Powerup(PowerupType.SHIELD, map.powerupLocations), new Powerup(PowerupType.SPEED, map.powerupLocations)
		};
		map.powerups[0].addAlternatePowerup(map.powerups[1]);
		map.powerups[1].addAlternatePowerup(map.powerups[0]);
		view.getChildren().addAll(map.powerups);

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

		player = new OfflinePlayer(map.getSpawns()[0].x * 64, map.getSpawns()[0].y * 64, 0, map, guiManager, TeamEnum.RED, collisionsHandler, inputHandler, map.getGameMode(), 60);
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
			@Override
			public void handle(long now)
			{
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
								generateSpray(pellet);
							view.getChildren().remove(pellet);
						}
					}
					player.tick();
				}
				hud.tick(gameLoop.getRemainingTime());
				if(gameLoop.getRemainingTime() == 0)
					hud.endGame(gameLoop.getRedTeam().getScore(), gameLoop.getBlueTeam().getScore());

				hud.setScore(TeamEnum.RED, gameLoop.getRedTeam().getScore());
				hud.setScore(TeamEnum.BLUE, gameLoop.getBlueTeam().getScore());
				updateView();
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
		super(view, guiManager.width, guiManager.height);
		this.guiManager = guiManager;
		init(mapName);

		cPlayer = receiver.getClientPlayer();
		ArrayList<EssentialPlayer> players = receiver.getAllPlayers();
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

		cPlayer.setInputHandler(inputHandler);

		if(map.getGameMode() == enums.GameMode.CAPTURETHEFLAG)
			view.getChildren().add(receiver.getClientGameStateReceiver().getFlag());

		view.getChildren().addAll(receiver.getClientGameStateReceiver().getPowerups());

		hud = new HeadUpDisplay(guiManager, cPlayer.getTeam());
		view.getChildren().add(hud);
		hud.toFront();

		receiver.getUdpClient().setActive(true);
		ClientInputSender inputSender = new ClientInputSender(receiver.getUdpClient(), inputHandler, cPlayer);
		inputSender.startSending();

		timer = new AnimationTimer()
		{
			@Override
			public void handle(long now)
			{
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
								generateSpray(pellet);
							view.getChildren().remove(pellet);
						}
					}
					player.tick();
				}

				hud.tick(timeRemaining);
				hud.setScore(TeamEnum.RED, redScore);
				hud.setScore(TeamEnum.BLUE, blueScore);
				updateView();
			}
		};
		timer.start();
	}

	private GameMode initGame(OfflinePlayer player)
	{
		Team red = player.getMyTeam(), blue = player.getOppTeam();
		switch(map.getGameMode())
		{
			case ELIMINATION:
				return new TeamMatchMode(red, blue);
			case CAPTURETHEFLAG:
				return new CaptureTheFlagMode(red, blue);
			default:
				throw new NoSuchElementException("Gamemode doesn't exist");
		}
	}

	/**
	 * Toggles the pause menu whilst in-game
	 */
	public void togglePauseMenu()
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
	public void toggleSettingsMenu()
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
	public boolean getPauseMenuState()
	{
		return pauseMenu.opened;
	}

	/**
	 * Get the current state of the settings scene in the pause menu
	 *
	 * @return <code>true</code> if the settings scene is active, <code>false</code> otherwise
	 */
	public boolean getSettingsMenuState()
	{
		return settingsMenu.opened;
	}

	public Map getMap()
	{
		return map;
	}

	public HeadUpDisplay getHud()
	{
		return hud;
	}

	public void destroy()
	{
		timer.stop();
		view = new Pane();
		PauseMenu.gridPane = new GridPane();
		PauseSettingsMenu.gridPane = new GridPane();
		HeadUpDisplay.view = new BorderPane();
	}

	private void generateSpray(Bullet pellet)
	{
		WritableImage paint = new WritableImage(64, 64);
		PixelWriter pixelWriter = paint.getPixelWriter();
		Random random = new Random();
		double probability = 0.1;
		for(int i = 1; i < 63; i++)
		{
			for(int j = 1; j < 63; j++)
				if(random.nextDouble() < probability)
					pixelWriter.setArgb(i, j, (pellet.getColour() == TeamEnum.RED ? java.awt.Color.RED : java.awt.Color.BLUE).getRGB());
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
		view.setScaleX(guiManager.width / 1024);
		view.setScaleY(guiManager.height / 576);
		pauseMenu = new PauseMenu(guiManager);
		settingsMenu = new PauseSettingsMenu(guiManager);
		map = Map.load(mapName);
		paintIndex = view.getChildren().size();
	}

	private void updateView()
	{
		double playerLayoutX = (singlePlayer ? player : cPlayer).getLayoutX(), playerLayoutY = (singlePlayer ? player : cPlayer).getLayoutY();

		double viewPositionX = (getWidth() / 2 - playerLayoutX - PLAYER_HEAD_X) * view.getScaleX(),
				viewPositionY = (getHeight() / 2 - playerLayoutY - PLAYER_HEAD_Y) * view.getScaleY(),
				subScenePositionX = (playerLayoutX + PLAYER_HEAD_X - (getWidth() / 2)),
				subScenePositionY = (playerLayoutY + PLAYER_HEAD_Y - (getHeight() / 2));

		view.relocate(viewPositionX, viewPositionY);
		hud.relocate(subScenePositionX, subScenePositionY);

		if(view.getChildren().contains(pauseMenu))
			pauseMenu.relocate(subScenePositionX, subScenePositionY);
		if(view.getChildren().contains(settingsMenu))
			settingsMenu.relocate(subScenePositionX, subScenePositionY);
	}


	public void setBlueScore(int blueScore)
	{
		this.blueScore = blueScore;
	}

	public void setRedScore(int redScore)
	{
		this.redScore = redScore;
	}

	public void setTimeRemaining(int timeRemaining)
	{
		this.timeRemaining = timeRemaining;
	}

}