package rendering;

import enums.Menu;
import enums.PowerupType;
import enums.TeamEnum;
import gui.GUIManager;
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
import networking.client.ClientInputSender;
import networking.client.ClientReceiver;
import physics.*;
import players.ClientPlayer;
import players.EssentialPlayer;
import players.OfflinePlayer;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;

import static gui.GUIManager.renderer;
import static players.EssentialPlayer.PLAYER_HEAD_X;
import static players.EssentialPlayer.PLAYER_HEAD_Y;

/**
 * A class that extends <code>Scene</code> to implement the displaying of a game instance. All assets are drawn on a <code>Pane</code>. There are two instances of <code>SubScene</code> for the pause menu and the settings menu, and a <code>SubScene</code> for the in-game head up display.<br><br>
 * Two constructors exist, depending on whether a singleplayer or multiplayer game is launched.
 *
 * @author Artur Komoter
 */
public class Renderer extends Scene {
	public static final double TARGET_FPS = 60.0;

	static Pane VIEW = new Pane();

	ClientPlayer onlinePlayer;
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

	//are we playing singleplayer?
	private boolean singlePlayer = false;


	/**
	 * Renders an offline game instance by loading the selected map, spawning the AI players and responding to changes in game logic.
	 *
	 * @param mapName    Name of the selected map
	 * @param guiManager GUI manager that creates this object
	 */
	public Renderer(String mapName, GUIManager guiManager) {
		super(VIEW, guiManager.width, guiManager.height);
		this.guiManager = guiManager;
		init(mapName);
		singlePlayer = true;

		if(map.gameMode == enums.GameMode.CAPTURE_THE_FLAG) {
			map.flag = new Flag(map.flagLocations);
			VIEW.getChildren().add(map.flag);
		}

		map.powerups = new Powerup[]{new Powerup(PowerupType.SHIELD, map.powerupLocations), new Powerup(PowerupType.SPEED, map.powerupLocations)
		};
		map.powerups[0].addAlternatePowerup(map.powerups[1]);
		map.powerups[1].addAlternatePowerup(map.powerups[0]);
		VIEW.getChildren().addAll(map.powerups);

		CollisionsHandler collisionsHandler = new CollisionsHandler(map);
		collisionsHandler.isLocal = true;
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

		VIEW.getChildren().addAll(players);
		collisionsHandler.setPlayers(players);

		hud = new HeadUpDisplay(guiManager, map.gameMode, player.getTeam());
		VIEW.getChildren().add(hud);
		hud.toFront();

		GameMode gameLoop = initGame();
		gameLoop.start();

		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				for(EssentialPlayer player : players) {
					for(Pellet pellet : player.getBullets()) {
						if(pellet.isActive()) {
							if(!VIEW.getChildren().contains(pellet))
								VIEW.getChildren().add(VIEW.getChildren().size() - 2, pellet);
						} else if(VIEW.getChildren().contains(pellet)) {
							if(pellet.getCollision() != null)
								generateSpray(pellet);
							VIEW.getChildren().remove(pellet);
						}
					}
					player.tick();
				}
				hud.tick(gameLoop.getRemainingTime());

				if(gameLoop.getRemainingTime() == 0)
					guiManager.transitionTo(Menu.END_GAME, gameLoop.getRedTeam().getScore() + "," + gameLoop.getBlueTeam().getScore(), player.getTeam());

				hud.setScore(gameLoop.getRedTeam().getScore(), gameLoop.getBlueTeam().getScore());
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
	public Renderer(String mapName, ClientReceiver receiver, GUIManager guiManager) {
		super(VIEW, guiManager.width, guiManager.height);
		this.guiManager = guiManager;
		init(mapName);

		onlinePlayer = receiver.getClientPlayer();
		ArrayList<EssentialPlayer> players = receiver.getAllPlayers();
		VIEW.getChildren().add(onlinePlayer);
		VIEW.getChildren().addAll(receiver.getMyTeam());
		receiver.getMyTeam().forEach(player -> VIEW.getChildren().add(player.getNameTag()));
		VIEW.getChildren().addAll(receiver.getEnemies());
		receiver.getEnemies().forEach(player -> VIEW.getChildren().add(player.getNameTag()));

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

		onlinePlayer.setInputHandler(inputHandler);

		if(map.getGameMode() == enums.GameMode.CAPTURE_THE_FLAG)
			VIEW.getChildren().add(receiver.getClientGameStateReceiver().getFlag());

		VIEW.getChildren().addAll(receiver.getClientGameStateReceiver().getPowerups());

		hud = new HeadUpDisplay(guiManager, map.gameMode, onlinePlayer.getTeam());
		VIEW.getChildren().add(hud);
		hud.toFront();

		receiver.getUdpClient().setActive(true);
		ClientInputSender inputSender = new ClientInputSender(receiver.getUdpClient(), inputHandler, onlinePlayer);
		inputSender.startSending();

		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				for(EssentialPlayer player : players) {
					for(Pellet pellet : player.getBullets()) {
						if(pellet.isActive()) {
							if(!VIEW.getChildren().contains(pellet))
								VIEW.getChildren().add(VIEW.getChildren().size() - 2, pellet);
						} else if(VIEW.getChildren().contains(pellet)) {
							if(pellet.getCollision() != null)
								generateSpray(pellet);
							VIEW.getChildren().remove(pellet);
						}
					}
					player.tick();
				}

				hud.tick(timeRemaining);
				hud.setScore(redScore, blueScore);
				updateView();
			}
		};
		timer.start();
	}

	/**
	 * End the game by transitioning to the end game screen.
	 *
	 * @param red  Final score of the red team
	 * @param blue Final score of the blue team
	 */
	public void endGame(String red, String blue) {
		guiManager.transitionTo(Menu.END_GAME, red + "," + blue, (renderer.onlinePlayer == null ? renderer.player.getTeam() : renderer.onlinePlayer.getTeam()));
	}

	/**
	 * Determine which game logic to use before starting the game loop.
	 *
	 * @return Game logic to be used for the game loop
	 */
	private GameMode initGame() {
		Team red = player.getMyTeam(), blue = player.getOppTeam();
		switch(map.getGameMode()) {
			case TEAM_MATCH:
				return new TeamMatchMode(red, blue);
			case CAPTURE_THE_FLAG:
				return new CaptureTheFlagMode(red, blue);
			default:
				throw new NoSuchElementException("Gamemode doesn't exist");
		}
	}

	/**
	 * Toggles the pause menu whilst in-game
	 */
	public void togglePauseMenu() {
		if(!pauseMenu.opened)
			VIEW.getChildren().add(pauseMenu);
		else
			VIEW.getChildren().remove(pauseMenu);
		pauseMenu.opened = !pauseMenu.opened;
	}

	/**
	 * Toggles the settings scene from the pause menu whilst in-game
	 */
	public void toggleSettingsMenu() {
		if(!settingsMenu.opened) {
			VIEW.getChildren().remove(pauseMenu);
			VIEW.getChildren().add(settingsMenu);
		} else {
			VIEW.getChildren().remove(settingsMenu);
			VIEW.getChildren().add(pauseMenu);
		}
		settingsMenu.opened = !settingsMenu.opened;
	}

	/**
	 * Get the current state of the pause menu
	 *
	 * @return <code>true</code> if the pause menu is active, <code>false</code> otherwise
	 */
	public boolean getPauseMenuState() {
		return pauseMenu.opened;
	}

	/**
	 * Get the current state of the settings scene in the pause menu
	 *
	 * @return <code>true</code> if the settings scene is active, <code>false</code> otherwise
	 */
	public boolean getSettingsMenuState() {
		return settingsMenu.opened;
	}

	/**
	 * Get the current map that is loaded.
	 *
	 * @return Map that the game instance is using
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * Get the head up display that is displayed in-game.
	 *
	 * @return A head up display object
	 */
	public HeadUpDisplay getHud() {
		return hud;
	}

	/**
	 * Stop the <code>AnimationTimer</code> and reset all static attributes used by <code>Renderer</code> before killing the game renderer.<br><br>
	 * <b>NOTE</b>: Has to be called <u>before</u> setting <code>Renderer</code> object to <code>null</code>!
	 */
	public void destroy() {
		timer.stop();
		VIEW = new Pane();
		PauseMenu.gridPane = new GridPane();
		PauseSettingsMenu.gridPane = new GridPane();
		HeadUpDisplay.VIEW = new BorderPane();
	}

	/**
	 * Generate random paint spray pattern on a map asset.
	 *
	 * @param pellet The <code>Pellet</code> that has collided with a map asset
	 */
	private void generateSpray(Pellet pellet) {
		WritableImage paint = new WritableImage(64, 64);
		PixelWriter pixelWriter = paint.getPixelWriter();
		Random random = new Random();
		double probability = 0.1;
		for(int i = 1; i < 63; i++) {
			for(int j = 1; j < 63; j++)
				if(random.nextDouble() < probability)
					pixelWriter.setArgb(i, j, (pellet.getColour() == TeamEnum.RED ? java.awt.Color.RED : java.awt.Color.BLUE).getRGB());
		}
		ImageView imageView = new ImageView(paint);
		imageView.relocate(pellet.getCollision().getX(), pellet.getCollision().getY());
		imageView.setCache(true);
		VIEW.getChildren().add(paintIndex, imageView);
	}

	/**
	 * Common code to initialise the <code>Renderer</code>.
	 *
	 * @param mapName Name of the map file to load
	 */
	private void init(String mapName) {
		setFill(Color.BLACK);
		setCursor(Cursor.CROSSHAIR);
		VIEW.setStyle("-fx-background-color: black;");
		VIEW.getStylesheets().add("styles/menu.css");
		VIEW.setScaleX(guiManager.width / 1024);
		VIEW.setScaleY(guiManager.height / 576);
		pauseMenu = new PauseMenu(guiManager);
		settingsMenu = new PauseSettingsMenu(guiManager);
		map = Map.load(mapName);
		paintIndex = VIEW.getChildren().size();
	}

	/**
	 * Update the camera view to be centered on the player.
	 */
	private void updateView() {
		double playerLayoutX = (singlePlayer ? player : onlinePlayer).getLayoutX(), playerLayoutY = (singlePlayer ? player : onlinePlayer).getLayoutY();

		double viewPositionX = (getWidth() / 2 - playerLayoutX - PLAYER_HEAD_X) * VIEW.getScaleX(),
				viewPositionY = (getHeight() / 2 - playerLayoutY - PLAYER_HEAD_Y) * VIEW.getScaleY(),
				subScenePositionX = (playerLayoutX + PLAYER_HEAD_X - (getWidth() / 2)),
				subScenePositionY = (playerLayoutY + PLAYER_HEAD_Y - (getHeight() / 2));

		VIEW.relocate(viewPositionX, viewPositionY);
		hud.relocate(subScenePositionX, subScenePositionY);

		if(VIEW.getChildren().contains(pauseMenu))
			pauseMenu.relocate(subScenePositionX, subScenePositionY);
		if(VIEW.getChildren().contains(settingsMenu))
			settingsMenu.relocate(subScenePositionX, subScenePositionY);
	}

	/**
	 * Set the score of the blue team.
	 *
	 * @param blueScore New score of the blue team
	 */
	public void setBlueScore(int blueScore) {
		this.blueScore = blueScore;
	}

	/**
	 * Set the score of the red team.
	 *
	 * @param redScore New score of the red team
	 */
	public void setRedScore(int redScore) {
		this.redScore = redScore;
	}

	/**
	 * Update the game timer.
	 *
	 * @param time Game time remaining in seconds
	 */
	public void setTimeRemaining(int time) {
		timeRemaining = time;
	}

}