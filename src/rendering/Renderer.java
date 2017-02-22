package rendering;

import java.util.ArrayList;

import audio.AudioManager;
import enums.TeamEnum;

import javafx.animation.AnimationTimer;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
<<<<<<< HEAD
import logic.LocalPlayer;
import logic.OfflineGameMode;
import logic.OfflineTeamMatchMode;
=======
>>>>>>> 32ad1e201d00ce6a669921a8fafd3a8134a47d0d
import networkingClient.ClientReceiver;
import physics.*;
import players.AIPlayer;
import players.GeneralPlayer;
import players.ClientLocalPlayer;
import players.PhysicsClientPlayer;

/**
 * A scene of a game instance. All assets are drawn on a <i>view</i> pane.
 *
 * @author Artur Komoter
 */
public class Renderer extends Scene
{
	static Pane view = new Pane();
	private double scale = 1;

	/**
	 * Renders a game instance by loading the selected map, spawning the players and responding to changes in game logic.
	 *
	 * @param mapName Name of the selected map
	 */
	public Renderer(String mapName, AudioManager audio, ClientReceiver receiver)
	{
		super(view, 1024, 576);
		setFill(Color.BLACK);
		setCursor(Cursor.CROSSHAIR);
		view.setStyle("-fx-background-color: black;");

		//16:9 aspect ratio
		widthProperty().addListener(observable ->
		{
			scale = getWidth() / 1024;
			view.setScaleX(scale);
			view.setScaleY((getWidth() * 0.5625) / 576);
		});

		Map.load("res/maps/" + mapName + ".json");

		PhysicsClientPlayer player = receiver.getClientPlayer();
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

		ArrayList<Bullet> pellets = new ArrayList<>();
		new AnimationTimer()
		{
			@Override
			public void handle(long now)
			{
				view.setLayoutX(((getWidth() / 2) - player.getImage().getWidth() - player.getLayoutX()) * scale);
				view.setLayoutY(((getHeight() / 2) - player.getImage().getHeight() - player.getLayoutY()) * scale);

				view.getChildren().removeAll(pellets);
				pellets.clear();

				for(Bullet pellet : player.getBullets())
				{
					if(pellet.isActive())
						pellets.add(pellet);
				}
				for(ClientLocalPlayer player : receiver.getMyTeam())
					pellets.addAll(player.getFiredBullets());
				for(ClientLocalPlayer player : receiver.getEnemies())
					pellets.addAll(player.getFiredBullets());
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
	public Renderer(String mapName, AudioManager audio)
	{
		super(view, 1024, 576);
		setFill(Color.BLACK);
		setCursor(Cursor.CROSSHAIR);
		view.setStyle("-fx-background-color: black;");

		//16:9 aspect ratio
		widthProperty().addListener(observable ->
		{
			scale = getWidth() / 1024;
			view.setScaleX(scale);
			view.setScaleY((getWidth() * 0.5625) / 576);
		});

		Map map = Map.load("res/maps/" + mapName + ".json");

		ArrayList<GeneralPlayer> players = new ArrayList<>();

		OfflinePlayer player = new OfflinePlayer(map.getSpawns()[0].x * 64, map.getSpawns()[0].y * 64, 0, false, map, audio, TeamEnum.RED);
		view.getChildren().add(player);
		players.add(player);

		
		for (GeneralPlayer p : player.getTeamPlayers()){
			players.add((AIPlayer) p);
			view.getChildren().add((AIPlayer) p);
		}
		
		for (GeneralPlayer p : player.getEnemies()){
			players.add((AIPlayer) p);
			view.getChildren().add((AIPlayer) p);
		}
		
		OfflineGameMode game = new OfflineTeamMatchMode(player);
		game.start();
		//player.setGame(new OfflineTeamMatchMode(player));

		
//		AIPlayer ai = new AIPlayer(map.getSpawns()[4].x * 64, map.getSpawns()[4].y * 64, 1, map, TeamEnum.BLUE, audio);
//		view.getChildren().add(ai);
//		players.add(ai);
//		AIPlayer ai2 = new AIPlayer(map.getSpawns()[5].x * 64, map.getSpawns()[5].y * 64, 2, map, TeamEnum.BLUE,audio);
//		view.getChildren().add(ai2);
//		players.add(ai2);
//		AIPlayer ai3 = new AIPlayer(map.getSpawns()[1].x * 64, map.getSpawns()[1].y * 64, 1, map, TeamEnum.RED, audio);
//		view.getChildren().add(ai3);
//		players.add(ai3);
		
		players.forEach(p -> {
			p.setCache(true);
			p.setCacheHint(CacheHint.SCALE_AND_ROTATE);
		});

		//provisional way to differ enemies and team players
		ArrayList<GeneralPlayer> teamRed = new ArrayList<GeneralPlayer>();
		ArrayList<GeneralPlayer> teamBlue = new ArrayList<GeneralPlayer>();
		for(GeneralPlayer p : players)
		{
			if(p.getTeam() == TeamEnum.RED)
				teamRed.add(p);
			else
				teamBlue.add(p);
		}
		for(GeneralPlayer p : players)
		{
			if(p.getTeam() == TeamEnum.RED)
			{
				p.setEnemies(teamBlue);
				p.setTeamPlayers(teamRed);
			}
			else
			{
				p.setEnemies(teamRed);
				p.setTeamPlayers(teamBlue);
			}
		}
		KeyPressListener keyPressListener = new KeyPressListener(player);
		KeyReleaseListener keyReleaseListener = new KeyReleaseListener(player);
		MouseListener mouseListener = new MouseListener(player);
		setOnKeyPressed(keyPressListener);
		setOnKeyReleased(keyReleaseListener);
		setOnMouseDragged(mouseListener);
		setOnMouseMoved(mouseListener);
		setOnMousePressed(mouseListener);
		setOnMouseReleased(mouseListener);

		new AnimationTimer()
		{
			@Override
			public void handle(long now)
			{
				view.setLayoutX(((getWidth() / 2) - player.getImage().getWidth() - player.getLayoutX()) * scale);
				view.setLayoutY(((getHeight() / 2) - player.getImage().getHeight() - player.getLayoutY()) * scale);

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

}
