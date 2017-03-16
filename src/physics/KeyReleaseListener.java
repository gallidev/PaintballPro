package physics;

import gui.GUIManager;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class KeyReleaseListener implements EventHandler<KeyEvent>
{
	private InputHandler inputHandler;

	public KeyReleaseListener(InputHandler inputHandler){
		this.inputHandler = inputHandler;
	}

	@Override
	public void handle(KeyEvent event)
	{
		if(!GUIManager.renderer.getPauseMenuState() && !GUIManager.renderer.getSettingsMenuState())
			switch(event.getCode())
			{
				case UP:
					inputHandler.setUp(false);
					break;
				case DOWN:
					inputHandler.setDown(false);
					break;
				case LEFT:
					inputHandler.setLeft(false);
					break;
				case RIGHT:
					inputHandler.setRight(false);
					break;
				case W:
					inputHandler.setUp(false);
					break;
				case S:
					inputHandler.setDown(false);
					break;
				case A:
					inputHandler.setLeft(false);
					break;
				case D:
					inputHandler.setRight(false);
					break;
				default:
					break;
			}
		event.consume();
	}
}
