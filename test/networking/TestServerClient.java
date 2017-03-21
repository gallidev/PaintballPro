package networking;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		server = null;
	}
	
	@Test
	public void testConnection() throws InterruptedException {
		server = new Server(9854, "127.0.0.1", null, 1);
		server.start();
		Client client = new Client("test", 9854, "127.0.0.1", new GUIManager(), 9884, true);
		Thread.sleep(1000);
		assertNotEquals(client.getClientID(),0);
		server.isRunning = false;
		server.interrupt();
		Thread.sleep(1000);
	}
	
	@Test
	public void testConnection2() throws InterruptedException {
		Server server3 = new Server(9855, "bob", null, 1);
		assertEquals(server3.exceptionCheck,1);
		Thread.sleep(1000);
	}
	
	@Test
	public void testConnection3() throws InterruptedException {
		Server server4 = new Server(9854, "127.0.0.1", null, 1);
		server4.start();
		Thread.sleep(1000);
		assertEquals(server4.exceptionCheck,2);
		server4.isRunning = false;
		server4.interrupt();
		Thread.sleep(1000);
	}
	
	@Test
	public void testConnection4() throws InterruptedException {
		Server server5 = new Server(9856, "127.0.0.1", null, 2);
		server5.start();
		Thread.sleep(1000);
		assertEquals(server5.exceptionCheck,3);
		server5.isRunning = false;
		server5.interrupt();
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
	
	@Test
	public void testSameUsername() throws InterruptedException {
		Server server1 = new Server(9858, "127.0.0.1", null, 1);
		server1.start();
		Client client4 = new Client("test3", 9858, "127.0.0.1", new GUIManager(), 9884, true);
		Client client5 = new Client("test3", 9858, "127.0.0.1", new GUIManager(), 9885, true);
		assertEquals(client4.exceptionCheck,0);
		assertEquals(client5.exceptionCheck,6);
		server1.isRunning = false;
		server1.interrupt();
	}
}