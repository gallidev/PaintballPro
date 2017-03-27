package physics;

import gui.GUIManager;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
*
*	This class handles the key release input events from the keyboard.
*
* @author Filippo Galli
* @author Sivarjuen Ravichandran
*/
public class KeyReleaseListener implements EventHandler<KeyEvent>
{

	/** The input handler. */
	private InputHandler inputHandler;

	/**
	 * Instantiates a new key release listener.
	 *
	 * @param inputHandler the input handler
	 */
	public KeyReleaseListener(InputHandler inputHandler){
		this.inputHandler = inputHandler;
	}

	/**
	 * handle the keys released by the player and updates the inputHandler
	 *
	 * @param event the KeyEvent when a keyboard button is released
	 */
	@Override
	public void handle(KeyEvent event)
	{
		if((!GUIManager.renderer.getPauseMenuState() && !GUIManager.renderer.getSettingsMenuState()))
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
