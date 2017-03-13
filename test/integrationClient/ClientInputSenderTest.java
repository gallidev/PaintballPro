//package integrationClient;
//
//import static org.junit.Assert.*;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import networking.game.UDPClient;
//import networking.game.UDPServer;
//import networking.server.ClientTable;
//import networking.server.LobbyTable;
//import physics.InputHandler;
//
//public class ClientInputSenderTest {
//	
//	private UDPServer server;
//	private UDPClient client;
//	private InputHandler handler;
//
//	@Before
//	public void setUp() throws Exception {
//		ClientTable table = new ClientTable();
//		LobbyTable lobby = new LobbyTable();
//		handler = new InputHandler();
//		server = new UDPServer(table, lobby);
//		client = new UDPClient(1, "127.0.0.1", null, null, 25567, "Test");
//	}
//
//	@Test
//	public void startSendingTest() {
//		assertNotNull(client);
//		assertNotNull(server);
//		fail("Not yet implemented");
//	}
//	
//	@Test
//	public void sendServerTest() {
//		
//		handler.setUp(true);
//		handler.setDown(false);
//		handler.setLeft(true);
//		handler.setRight(true);
//		handler.setShoot(true);
//		handler.setMouseX(2);
//		handler.setMouseY(3);
//		
//		
//		
//		
//	}
//
//}
