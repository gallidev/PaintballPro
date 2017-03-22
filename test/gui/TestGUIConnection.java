package gui;

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

import static org.junit.Assert.*;

/**
 * Created by jack on 22/03/2017.
 */
public class TestGUIConnection {

	Thread serverThread;
	GUIManager guiManager;
	ServerGUI serverGUI;

	@Before
	public void setUp() throws Exception {
		JavaFXTestHelper.setupApplication();
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
		Thread.sleep(3000);
	}

	@Test
	public void testConnection() throws Exception {
		guiManager.setIpAddress(IPAddress.getLAN());
		assertTrue(guiManager.establishConnection() == 0);

		guiManager.setIpAddress("0.0.0.0");
		assertTrue(guiManager.establishConnection() != 0);

		GUIManagerTestHelper.findButtonByTextInParent("Exit", serverGUI.getRoot()).fire();
	}

	@After
	public void tearDown() throws Exception {
		if (serverThread != null)
			serverThread.interrupt();
		serverThread = null;
		serverGUI = null;
	}
}