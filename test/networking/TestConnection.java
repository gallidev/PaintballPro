package networking;

import gui.GUIManager;
import gui.ServerGUI;
import helpers.GUIManagerTestHelper;
import helpers.JavaFXTestHelper;
import javafx.application.Platform;
import javafx.stage.Stage;
import networking.discoveryNew.DiscoveryServerAnnouncer;
import networking.discoveryNew.IPAddress;
import networking.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Test to check that a connection can be established
 *
 * @author Jack Hughes
 */
public class TestConnection {

	Thread serverThread;
	ServerGUI serverGUI;
	GUIManager guiManager;

	/**
	 * Setup the JavaFX application, server and GUI
	 *
	 * @throws Exception setup failed
	 */
	@Before
	public void setUp() throws Exception {
		JavaFXTestHelper.setupApplication();
		JavaFXTestHelper.waitForPlatform();
		serverThread = new Thread(() -> {
			int portNo = 25566;
			DiscoveryServerAnnouncer discovery = new DiscoveryServerAnnouncer();
			discovery.start();
			serverGUI = new ServerGUI();
			Server server = new Server(portNo, IPAddress.getLAN(), serverGUI, 0);
			server.start();
			serverGUI.setServer(server, discovery);
		});
		serverThread.start();
		Thread.sleep(3000);
		guiManager = new GUIManager();
		Platform.runLater(() -> {
			guiManager.setStage(new Stage());
		});
		JavaFXTestHelper.waitForPlatform();
	}

	/**
	 * Test the connection with the lobby
	 *
	 * @throws Exception test failed
	 */
	@Test
	public void testConnection() throws Exception {
		Thread.sleep(2000);
		Platform.runLater(() -> {
			try {
				GUIManagerTestHelper.findButtonByTextInParent("Multiplayer", guiManager.getStage().getScene().getRoot()).fire();
			} catch (RuntimeException e) {
				fail(e.getMessage());
			}
		});
		JavaFXTestHelper.waitForPlatform();
		Thread.sleep(100);
		Platform.runLater(() -> {
			try {
				GUIManagerTestHelper.findButtonByTextInParent("Connect", guiManager.getStage().getScene().getRoot()).fire();
			} catch (RuntimeException e) {
				fail(e.getMessage());
			}
		});
		JavaFXTestHelper.waitForPlatform();
		Thread.sleep(4000);
		Platform.runLater(() ->

		{
			try {
				GUIManagerTestHelper.findButtonByTextInParent("Team Match", guiManager.getStage().getScene().getRoot()).fire();
			} catch (RuntimeException e) {
				fail(e.getMessage());
			}
		});
		JavaFXTestHelper.waitForPlatform();
		Thread.sleep(3000);
		Platform.runLater(() -> {
			try {
				GUIManagerTestHelper.findButtonByTextInParent("Back", guiManager.getStage().getScene().getRoot()).fire();
			} catch (RuntimeException e) {
				fail(e.getMessage());
			}
		});
		JavaFXTestHelper.waitForPlatform();
		Thread.sleep(1000);
		Platform.runLater(() -> {
			try {
				GUIManagerTestHelper.findButtonByTextInParent("Back", guiManager.getStage().getScene().getRoot()).fire();
			} catch (RuntimeException e) {
				fail(e.getMessage());
			}
		});
		JavaFXTestHelper.waitForPlatform();
		Thread.sleep(2000);
	}

	/**
	 * Tear down the test
	 *
	 * @throws Exception tear down failed
	 */
	@After
	public void tearDown() throws Exception {
		serverThread.interrupt();
		serverThread = null;
		serverGUI = null;
		guiManager = null;
	}

}