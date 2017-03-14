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

/**
 * Test class to test TCP Server and Client Communication.
 * Tests - Server.java, Client.java, ServerReceiver.java, ServerSender.java, ClientReceiver.java, ClientSender.java
 * 
 * @author Matthew Walters
 */
public class TestServerClient {
	
	Server server;
	//Client client;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testConnection() throws InterruptedException {
		server = new Server(9854, "127.0.0.1", null);
		server.testing = true;
		server.start();
		Client client = new Client("test", 9854, "127.0.0.1", new GUIManager(), 9884, true);
		Thread.sleep(1000);
		assertNotEquals(client.getClientID(),0);
		server.isRunning = false;
		server.interrupt();
		Thread.sleep(1000);
	}
	
	@Test
	public void testNoServer() {
		Client client = new Client("test2", 9855, "127.0.0.1", new GUIManager(), 9885, true);
		assertEquals(client.exceptionCheck,2);
	}
	
	@Test
	public void testBadUsername1() {
		Client client2 = new Client("test-4", 9854, "127.0.0.1", new GUIManager(), 9884, true);
		assertEquals(client2.exceptionCheck,1);
	}
	
	@Test
	public void testBadUsername2() {
		Client client3 = new Client("test:5", 9854, "127.0.0.1", new GUIManager(), 9884, true);
		assertEquals(client3.exceptionCheck,1);
	}
	

}
