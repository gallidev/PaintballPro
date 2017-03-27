package physics;

import gui.GUIManager;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving keyRelease events.
 * The class that is interested in processing a keyRelease
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addKeyReleaseListener<code> method. When
 * the keyRelease event occurs, that object's appropriate
 * method is invoked.
 *
 * @see KeyReleaseEvent
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

	/* (non-Javadoc)
	 * @see javafx.event.EventHandler#handle(javafx.event.Event)
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
