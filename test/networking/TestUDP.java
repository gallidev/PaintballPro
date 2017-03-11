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
		table = new ClientTable();
		lobby = new LobbyTable();
		lobby.testEnv = true;
		int id = table.add("test");
		lobby.addPlayerToLobby(table.getPlayer(id), 1, null, null);
		server = new UDPServer(table, lobby);
		server.start();
		client1 = new UDPClient(id, "127.0.0.1", null, null, 25567, "test");
		id = table.add("test2");
		lobby.addPlayerToLobby(table.getPlayer(id), 1, null, null);
		lobby.switchTeams(table.getPlayer(id), null);
		client2 = new UDPClient(id, "127.0.0.1", null, null, 25568, "test");
		client1.start();
		client2.start();
		server.sendToAll("TestSendToAll", "127.0.0.1:25567");
	}

	@After
	public void tearDown() throws Exception {
		client1.interrupt();
		client2.interrupt();
		server.interrupt();
	}

	@Test
	public void testUDPServer() {
		assertNotNull(server);
		assertNotNull(client1);
		assertTrue(client1.connected);
		assertTrue(client2.connected);
		assertTrue(client1.getTestSend());
		assertTrue(client2.getTestSend());
	}
}