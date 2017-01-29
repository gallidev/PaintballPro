package rendering;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import physics.KeyPressListener;
import physics.KeyReleaseListener;
import physics.MouseListener;
import physics.Player;

public class Renderer extends Scene
{
	private static Group view = new Group();

	public Renderer()
	{
		super(view, 800, 600);
		Player player = new Player(72, 72);
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
			}
		}.start();
	}
}
