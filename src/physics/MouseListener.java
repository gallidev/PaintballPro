package physics;

import gui.GUIManager;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving mouse events.
 * The class that is interested in processing a mouse
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addMouseListener<code> method. When
 * the mouse event occurs, that object's appropriate
 * method is invoked.
 *
 * @see MouseEvent
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

	/* (non-Javadoc)
	 * @see javafx.event.EventHandler#handle(javafx.event.Event)
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
