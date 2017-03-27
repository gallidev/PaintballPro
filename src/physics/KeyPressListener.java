package physics;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import static gui.GUIManager.renderer;

/**
 *
 *	This class handles the key press input events from the keyboard.
 *
 * @author Filippo Galli
 * @author Sivarjuen Ravichandran
 */
public class KeyPressListener implements EventHandler<KeyEvent>
{

	/** The input handler. */
	private InputHandler inputHandler;

	/**
	 * Instantiates a new key press listener.
	 *
	 * @param inputHandler the input handler
	 */
	public KeyPressListener(InputHandler inputHandler){
		this.inputHandler = inputHandler;
	}

	/**
	 * handle the keys pressed by the player and updates the inputHandler
	 *
	 * @param event the KeyEvent when a keyboard button is pressed
	 */
	@Override
	public void handle(KeyEvent event)
	{
		if(renderer.getSettingsMenuState())
		{
			switch(event.getCode())
			{
				case ESCAPE:
					renderer.toggleSettingsMenu();
					break;
				default:
					break;
			}
		}
		else if(renderer.getPauseMenuState())
		{
			switch(event.getCode())
			{
				case ESCAPE:
					renderer.togglePauseMenu();
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
//				case P:
//					System.out.println(Math.round(renderer.player.getLayoutX() / 64) + " " + Math.round(renderer.player.getLayoutY() / 64));
//					break;
				case ESCAPE:
					renderer.togglePauseMenu();
					break;
				default:
					break;
			}
		}
		event.consume();
	}
}
