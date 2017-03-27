package physics;

import gui.GUIManager;
import helpers.JavaFXTestHelper;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import players.EssentialPlayer;
import rendering.Renderer;

import static gui.GUIManager.renderer;
import static org.junit.Assert.*;


/**
 * Tests the Input handler and the listeners
 *
 * @author Filippo Galli
 *
 */
public class TestInputHandler {

	/** The input handler. */
	private InputHandler inputHandler;

	/** The mouse listener. */
	private MouseListener mouseListener;

	/** The key release listener. */
	private KeyReleaseListener keyReleaseListener;

	/** The key press listener. */
	private KeyPressListener keyPressListener;

	/** The gui manager. */
	private GUIManager guiManager;

	/**
	 * Set up all the objects and the environment
	 */
	@Before
	public void setUp()
	{

		JavaFXTestHelper.setupApplication();
		guiManager = new GUIManager();
		GUIManager.renderer = new Renderer("desert", guiManager);

		inputHandler = new InputHandler();
		keyPressListener = new KeyPressListener(inputHandler);
		keyReleaseListener = new KeyReleaseListener(inputHandler);
		mouseListener = new MouseListener(inputHandler);

	}

	/**
	 * Tear down the created objects
	 */
	@After
	public void tearDown()
	{
		renderer.destroy();
		renderer = null;
		inputHandler = null;
		mouseListener = null;
		keyReleaseListener = null;
		keyPressListener = null;
		guiManager = null;
	}

	/**
	 * Test the mouse listener with mouse positions and clicks.
	 */
	@Test
	public void testMouse()
	{

		mouseListener.handle(new MouseEvent(MouseEvent.MOUSE_PRESSED, 20, 50, 30, 30, null, 0, false, false, false, false, true, false, false, false, false, false, null));

		assertTrue(inputHandler.isShooting());
		assertEquals(inputHandler.getMouseX(), 20);
		assertEquals(inputHandler.getMouseY(), 50);

		double deltax = 20 - (1.65 * EssentialPlayer.PLAYER_HEAD_X);
		double deltay = EssentialPlayer.PLAYER_HEAD_Y - 50;
		double angle = Math.atan2(deltax, deltay);
		assertEquals(angle, inputHandler.getAngle(), 0.1);

	}

	/**
	 * Test key board listeners with key pressed and released for movement and shooting.
	 */
	@Test
	public void testKeyBoard()
	{
		keyPressListener.handle(new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.W, false, false, false, false));
		keyPressListener.handle(new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.A, false, false, false, false));
		keyPressListener.handle(new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.S, false, false, false, false));
		keyPressListener.handle(new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.D, false, false, false, false));

		assertTrue(inputHandler.isUp());
		assertTrue(inputHandler.isDown());
		assertTrue(inputHandler.isLeft());
		assertTrue(inputHandler.isRight());

		keyReleaseListener.handle(new KeyEvent(KeyEvent.KEY_RELEASED, null, null, KeyCode.W, false, false, false, false));
		keyReleaseListener.handle(new KeyEvent(KeyEvent.KEY_RELEASED, null, null, KeyCode.A, false, false, false, false));
		keyReleaseListener.handle(new KeyEvent(KeyEvent.KEY_RELEASED, null, null, KeyCode.S, false, false, false, false));
		keyReleaseListener.handle(new KeyEvent(KeyEvent.KEY_RELEASED, null, null, KeyCode.D, false, false, false, false));

		assertFalse(inputHandler.isUp());
		assertFalse(inputHandler.isDown());
		assertFalse(inputHandler.isLeft());
		assertFalse(inputHandler.isRight());

		keyPressListener.handle(new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.LEFT, false, false, false, false));
		keyPressListener.handle(new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.UP, false, false, false, false));
		keyPressListener.handle(new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.RIGHT, false, false, false, false));
		keyPressListener.handle(new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.DOWN, false, false, false, false));
		keyPressListener.handle(new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.ESCAPE, false, false, false, false));

		assertTrue(inputHandler.isUp());
		assertTrue(inputHandler.isDown());
		assertTrue(inputHandler.isLeft());
		assertTrue(inputHandler.isRight());

		assertTrue(GUIManager.renderer.getPauseMenuState());

		keyPressListener.handle(new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.ESCAPE, false, false, false, false));

		//GUIManager.renderer.toggleSettingsMenu();

		keyPressListener.handle(new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.ESCAPE, false, false, false, false));

		assertFalse(GUIManager.renderer.getSettingsMenuState());

		keyPressListener.handle(new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.ESCAPE, false, false, false, false));

		keyReleaseListener.handle(new KeyEvent(KeyEvent.KEY_RELEASED, null, null, KeyCode.LEFT, false, false, false, false));
		keyReleaseListener.handle(new KeyEvent(KeyEvent.KEY_RELEASED, null, null, KeyCode.UP, false, false, false, false));
		keyReleaseListener.handle(new KeyEvent(KeyEvent.KEY_RELEASED, null, null, KeyCode.RIGHT, false, false, false, false));
		keyReleaseListener.handle(new KeyEvent(KeyEvent.KEY_RELEASED, null, null, KeyCode.DOWN, false, false, false, false));
		keyReleaseListener.handle(new KeyEvent(KeyEvent.KEY_RELEASED, null, null, KeyCode.DOWN, false, false, false, false));


		assertFalse(inputHandler.isUp());
		assertFalse(inputHandler.isDown());
		assertFalse(inputHandler.isLeft());
		assertFalse(inputHandler.isRight());

	}

}
