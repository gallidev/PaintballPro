package networking;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import networking.game.UDPClient;
import networking.game.UDPServer;
import networking.server.ClientTable;
import networking.server.LobbyTable;

/**
 * Test class to test UDP Server and Client Communication.
 * Tests - UDPClient.java, UDPServer.java
 * 
 * @author Matthew Walters
 */
public class TestUDP {

	UDPServer server;
	UDPClient client1;
	UDPClient client2;
	ClientTable table;
	LobbyTable lobby;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testUDPServer() throws InterruptedException {
		table = new ClientTable();
		
		lobby = new LobbyTable();
		lobby.testEnv = true;
		
		int id = table.add("test");
		
		lobby.addPlayerToLobby(table.getPlayer(id), 1, null, null);
		
		server = new UDPServer(table, lobby, 19878);
		server.start();
		Thread.sleep(500); 
		
		client1 = new UDPClient(id, "127.0.0.1", 19878,null, null, 25568, "test");
		
		id = table.add("test2");
		lobby.addPlayerToLobby(table.getPlayer(id), 1, null, null);
		lobby.switchTeams(table.getPlayer(id), null);
		
		client2 = new UDPClient(id, "127.0.0.1", 19878,null, null, 25569, "test");
		
		client1.testNetworking = true;
		client2.testNetworking = true;
		
		client1.start();
		client2.start(); 
		
		server.sendToAll("TestSendToAll", "127.0.0.1:"+client1.port);
		Thread.sleep(500); 
		
		assertNotNull(server);
		assertNotNull(client1);
		assertTrue(client1.connected);
		assertTrue(client2.connected);
		assertTrue(client1.testSendToAll);
		assertTrue(client2.testSendToAll);
		
		client1.active = false;
		client2.active = false;
		server.m_running = false;
		server.interrupt();
	}
}