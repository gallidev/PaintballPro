package gui;

import helpers.JavaFXTestHelper;
import javafx.application.Platform;
import javafx.stage.Stage;
import networking.discoveryNew.DiscoveryServerAnnouncer;
import networking.discoveryNew.IPAddress;
import networking.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for establishing a server connection from GUI Manager
 *
 * @author Jack Hughes
 */
public class TestGUIConnection {

	private Thread serverThread;
	private GUIManager guiManager;
	private ServerGUI serverGUI;

	/**
	 * Setup the JavaFX application
	 * @throws Exception test failed
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
	 * Test a successful and unsuccessful connection
	 * @throws Exception test failed
	 */
	@Test
	public void testConnection() throws Exception {
		guiManager.setIpAddress(IPAddress.getLAN());
		assertTrue(guiManager.establishConnection() == 0);

		guiManager.setIpAddress("0.0.0.0");
		assertTrue(guiManager.establishConnection() != 0);
	}

	/**
	 * Remove created fields
	 * @throws Exception test failed
	 */
	@After
	public void tearDown() throws Exception {
		if (serverThread != null)
			serverThread.interrupt();
		serverThread = null;
		serverGUI = null;
	}
}