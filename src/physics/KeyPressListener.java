package physics;

import gui.GUIManager;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class KeyPressListener implements EventHandler<KeyEvent>
{
	private InputHandler inputHandler;

	public KeyPressListener(InputHandler inputHandler){
		this.inputHandler = inputHandler;
	}

	@Override
	public void handle(KeyEvent event)
	{
		if(GUIManager.renderer.getSettingsMenuState())
		{
			switch(event.getCode())
			{
				case ESCAPE:
					GUIManager.renderer.toggleSettingsMenu();
					break;
				default:
					break;
			}
		}
		else if(GUIManager.renderer.getPauseMenuState())
		{
			switch(event.getCode())
			{
				case ESCAPE:
					GUIManager.renderer.togglePauseMenu();
					break;
				default:
					break;
			}
		}
		else
		{
			switch(event.getCode())
			{
				case UP:
					inputHandler.setUp(true);
					break;
				case DOWN:
					inputHandler.setDown(true);
					break;
				case LEFT:
					inputHandler.setLeft(true);
					break;
				case RIGHT:
					inputHandler.setRight(true);
					break;
				case W:
					inputHandler.setUp(true);
					break;
				case S:
					inputHandler.setDown(true);
					break;
				case A:
					inputHandler.setLeft(true);
					break;
				case D:
					inputHandler.setRight(true);
					break;
				case ESCAPE:
					GUIManager.renderer.togglePauseMenu();
					break;
				default:
					break;
			}
		}
		event.consume();
	}
}
