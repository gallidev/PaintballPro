package rendering;

import java.util.ArrayList;

import ai.AIPlayer;
import audio.AudioManager;
import enums.Teams;

import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import physics.*;

/**
 * A scene of a game instance. All assets are drawn on a <i>view</i> pane.
 *
 * @author Artur Komoter
 */
public class Renderer extends Scene
{
	static Pane view = new Pane();
	private Map map;
	private double scale = 1;
	private ArrayList<GeneralPlayer> players = new ArrayList<GeneralPlayer>();
	private AudioManager audio;

	/**
	 * Renders a game instance by loading the selected map, spawning the players and responding to changes in game logic.
	 *
	 * @param mapName Name of the selected map
	 */
	public Renderer(String mapName, AudioManager audio)
	{
		super(view, 1024, 576);
		setCursor(Cursor.CROSSHAIR);
		view.setStyle("-fx-background-color: black;");

		this.audio = audio;

		//16:9 aspect ratio
		widthProperty().addListener(observable ->
		{
			scale = getWidth() / 1024;
			view.setScaleX(scale);
			view.setScaleY((getWidth() * 0.5625) / 576);
		});

		map = Map.load("res/maps/" + mapName + ".json");

		Image redPlayerImage = new Image("assets/player_red.png", 30, 64, true, true);
		Image bluePlayerImage = new Image("assets/player_blue.png", 30, 64, true, true);
		ClientPlayer player = new ClientPlayer(map.getSpawns()[0].x * 64, map.getSpawns()[0].y * 64, 0, false, this, Teams.RED, redPlayerImage);
		players.add(player);

		AIPlayer ai = new AIPlayer(map.getSpawns()[4].x * 64, map.getSpawns()[4].y * 64, 4, this, Teams.BLUE, bluePlayerImage);
		players.add(ai);

		AIPlayer ai2 = new AIPlayer(map.getSpawns()[5].x * 64, map.getSpawns()[5].y * 64, 5, this, Teams.BLUE, bluePlayerImage);
		players.add(ai2);

		view.getChildren().addAll(players);


		//provisional way to differ enemies and team players
		ArrayList<GeneralPlayer> teamRed = new ArrayList<GeneralPlayer>();
		ArrayList<GeneralPlayer> teamBlue = new ArrayList<GeneralPlayer>();

		for(GeneralPlayer p : players)
		{
			if(p.getTeam() == Teams.RED)
				teamRed.add(p);
			else
				teamBlue.add(p);
		}
		for(GeneralPlayer p : players)
		{
			if(p.getTeam() == Teams.RED)
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

	public Map getMap()
	{
		return map;
	}

	//only for testing the bullets collisions for the moment
	public ArrayList<GeneralPlayer> getPlayers()
	{
		return players;
	}

	public AudioManager getAudio() {
		return audio;
	}
}
