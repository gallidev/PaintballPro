package gui;

import helpers.JavaFXTestHelper;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for establishing a server connection from GUI Manager
 *
 * @author Jack Hughes
 */
public class TestGUIConnection {

	private GUIManager guiManager;

	/**
	 * Setup the JavaFX application
	 * @throws Exception test failed
	 */
	@Before
	public void setUp() throws Exception {
		JavaFXTestHelper.setupApplication();
		JavaFXTestHelper.waitForPlatform();
		Thread.sleep(3000);
		guiManager = new GUIManager();
		Platform.runLater(() -> {
			guiManager.setStage(new Stage());
		});
		JavaFXTestHelper.waitForPlatform();
	}

	/**
	 * Test a successful and unsuccessful connection
	 * @throws Exception test failed
	 */
	@Test
	public void testConnection() throws Exception {
		guiManager.setIpAddress("0.0.0.0");
		assertTrue(guiManager.establishConnection() != 0);
	}

}