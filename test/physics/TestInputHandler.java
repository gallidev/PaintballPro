package physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import enums.TeamEnum;
import helpers.JavaFXTestHelper;
import integrationServer.CollisionsHandlerListener;
import integrationServer.ServerGameSimulation;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import logic.GameMode;
import players.EssentialPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;
import serverLogic.Team;
import serverLogic.TeamMatchMode;

public class TestInputHandler {

	private Pane view;
	private Scene testScene;
	private InputHandler inputHandler;
	private MouseListener mouseListener;
	private KeyReleaseListener keyReleaseListener;
	private KeyPressListener keyPressListener;

	@Before
	public void setUp()
	{

		view = new Pane();
		testScene = new Scene(view, 1000, 1000);

		JavaFXTestHelper.setupApplication();

		inputHandler = new InputHandler();
		keyPressListener = new KeyPressListener(inputHandler);
		keyReleaseListener = new KeyReleaseListener(inputHandler);
		mouseListener = new MouseListener(inputHandler);
		testScene.setOnKeyPressed(keyPressListener);
		testScene.setOnKeyReleased(keyReleaseListener);
		testScene.setOnMouseDragged(mouseListener);
		testScene.setOnMouseMoved(mouseListener);
		testScene.setOnMousePressed(mouseListener);
		testScene.setOnMouseReleased(mouseListener);



	}

	@After
	public void tearDown()
	{

	}

	@Test
	public void testMouse()
	{

		mouseListener.handle(new MouseEvent(MouseEvent.MOUSE_PRESSED, 20, 50, 30, 30, null, 0, false, false, false, false, true, false, false, false, false, false, null));

		assertTrue(inputHandler.isShooting());
		assertEquals(inputHandler.getMouseX(), 20);
		assertEquals(inputHandler.getMouseY(), 50);

	}

}
