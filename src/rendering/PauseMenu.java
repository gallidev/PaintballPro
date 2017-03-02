package rendering;

import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

class PauseMenu extends SubScene
{
	private static Pane view = new Pane();
	boolean opened = false;

	PauseMenu()
	{
		super(view, Renderer.view.getWidth(), Renderer.view.getHeight());
		setFill(Color.BLACK);
		setOpacity(0.5);
	}
}
