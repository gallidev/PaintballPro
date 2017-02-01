package rendering;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import physics.*;

import java.util.Random;

public class Renderer extends Scene
{
	private static Group view = new Group();

	public Renderer()
	{
		super(view, 800, 600);

		Random random = new Random();
		for(int i = 0; i < 15; i++)
		{
			int x = random.nextInt(15), y = random.nextInt(15);
			Asset dirt = new Asset("assets/dirt.png", AssetType.Floor, x * 64, y * 64);
			view.getChildren().add(dirt);
		}
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
				view.setTranslateX(-player.getLayoutX() + 370);
				view.setTranslateY(-player.getLayoutY() + 236);
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
