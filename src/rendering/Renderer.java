package rendering;

import java.util.ArrayList;

import audio.AudioManager;
import enums.TeamEnum;

import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import networkingClient.ClientReceiver;
import physics.*;

import static gui.GUIManager.redPlayerImage;

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

		Map map = Map.load("res/maps/" + mapName + ".json");

		ClientPlayer player;
		if(receiver != null)
		{
			player = receiver.getClientPlayer();
			view.getChildren().addAll(receiver.getMyTeam());
			view.getChildren().addAll(receiver.getEnemies());
		}
		else
		{
			player = new ClientPlayer(map.getSpawns()[0].x * 64, map.getSpawns()[0].y * 64, 0, false, map, audio, TeamEnum.RED, null);
			player.setEnemies(new ArrayList<>());
		}
		view.getChildren().add(player);

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
		}.start();
	}
}
