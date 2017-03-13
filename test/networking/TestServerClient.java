package networking;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import gui.GUIManager;
import networking.client.Client;
import networking.server.Server;

public class TestServerClient {
	
	Server server;
	Client client;

	@Before
	public void setUp() throws Exception {
		server = new Server(9854, "127.0.0.1", null);
		server.testing = true;
		server.start();
		client = new Client("test", 9854, "127.0.0.1", new GUIManager(), 9884, true);
		Thread.sleep(1000);
	}

	@After
	public void tearDown() throws Exception {
		server.isRunning = false;
		server.interrupt();
		Thread.sleep(1000);
	}
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void test() {
		assertNotEquals(client.getClientID(),0);
	}
}
