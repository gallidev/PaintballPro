package rendering;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import physics.*;

public class Renderer extends Scene
{
	static Group view = new Group();

	public Renderer()
	{
		super(view, 800, 600);

		new Map("maps/elimination.json");

		Player player = new Player(0, 0, false);
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
				player.tick();
				view.setLayoutX(370 - player.getLayoutX());
				view.setLayoutY(236 - player.getLayoutY());
				for(Bullet pellet : player.getBullets())
				{
					if(!view.getChildren().contains(pellet))
						view.getChildren().add(pellet);
					else
					{
						pellet.setX(pellet.getX());
						pellet.setY(pellet.getY());
					}
				}
			}
		}.start();
	}
}
