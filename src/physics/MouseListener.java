package physics;

import gui.GUIManager;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;


/**
*
*	This class handles the positions and click events from the mouse.
*
* @author Filippo Galli
* @author Sivarjuen Ravichandran
*
*/
public class MouseListener implements EventHandler<MouseEvent>{

	/** The input handler. */
	private InputHandler inputHandler;

	/**
	 * Instantiates a new mouse listener.
	 *
	 * @param inputHandler the input handler
	 */
	public MouseListener(InputHandler inputHandler){
		this.inputHandler = inputHandler;
	}

	/**
	 * handle the mouse positions & click and updates the inputHandler
	 *
	 * @param event the KeyEvent when a keyboard button is pressed
	 */
	@Override
    public void handle(MouseEvent event) {

		if((!GUIManager.renderer.getPauseMenuState() && !GUIManager.renderer.getSettingsMenuState()))
		{
				int newX = (int) event.getX();
				int newY = (int) event.getY();
				inputHandler.setMouseX(newX);
				inputHandler.setMouseY(newY);
				inputHandler.setShoot(event.isPrimaryButtonDown());
		}
    }
}
