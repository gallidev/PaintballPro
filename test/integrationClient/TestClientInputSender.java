<<<<<<< HEAD
//package integrationClient;
//
//import static org.junit.Assert.*;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import enums.TeamEnum;
//import gui.GUIManager;
//import networking.client.TeamTable;
//import networking.game.UDPClient;
//import networking.game.UDPServer;
//import networking.server.ClientTable;
//import networking.server.LobbyTable;
//import physics.InputHandler;
//import players.ClientPlayer;
//import rendering.ImageFactory;
//
//public class TestClientInputSender {
//	
//	private UDPServer server;
//	private UDPClient client;
//	private InputHandler handler;
//	
//	private ClientInputSender inputSender;
//
//	@Before
//	public void setUp() throws Exception {
//		ClientTable table = new ClientTable();
//		LobbyTable lobby = new LobbyTable();
//		
//		handler = new InputHandler();
//		
//		server = new UDPServer(table, lobby, 0);
//		client = new UDPClient(1, "127.0.0.1", 19857, new GUIManager(), new TeamTable(), 9879, "TestClient");
//		
//		ClientPlayer p = new ClientPlayer(0.0, 0.0, 1,  ImageFactory.getPlayerImage(TeamEnum.RED), null, null, 30.0);
//		inputSender = new ClientInputSender(client, handler, p);
=======
package integrationClient;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import enums.TeamEnum;
import gui.GUIManager;
import networking.client.TeamTable;
import networking.game.UDPClient;
import networking.game.UDPServer;
import networking.server.ClientTable;
import networking.server.LobbyTable;
import physics.InputHandler;
import players.ClientPlayer;
import rendering.ImageFactory;
import rendering.Renderer;

public class TestClientInputSender {

	private UDPServer server;
	private UDPClient client;
	private InputHandler handler;

	private ClientInputSender inputSender;

	@Before
	public void setUp() throws Exception {
		ClientTable table = new ClientTable();
		LobbyTable lobby = new LobbyTable();

		handler = new InputHandler();

		server = new UDPServer(table, lobby, 0);
		client = new UDPClient(1, "127.0.0.1", 19857, new GUIManager(), new TeamTable(), 9879, "TestClient");

		ClientPlayer p = new ClientPlayer(0, 0, 0, null, TeamEnum.RED, null, null, handler, null, null, Renderer.TARGET_FPS);
		inputSender = new ClientInputSender(client, handler, p);
	}

	@Test
	public void startSendingTest() {
		System.out.println("incepe");
		fail();
//		handler.setUp(true);
//		handler.setDown(false);
//		handler.setLeft(true);
//		handler.setRight(true);
//		handler.setShoot(true);
//		System.out.println("da");
//
//		//"0:1:Up:Left:Right:Shoot:2:3:0:0"
//		//inputSender.startSending();
//		//Thread.sleep(100);
//		assertTrue(client.testIntegration);
//		System.out.println("done that");
	}

//	@Test
//	public void sendServerTest() {
//
//
>>>>>>> 188b57133ad92887e8a3d7dd994520bfc5da7760
//	}
//
//	@Test
//	public void startSendingTest() {
//		System.out.println("incepe");
//		fail();
////		handler.setUp(true);
////		handler.setDown(false);
////		handler.setLeft(true);
////		handler.setRight(true);
////		handler.setShoot(true);
////		System.out.println("da");
////
////		//"0:1:Up:Left:Right:Shoot:2:3:0:0"
////		//inputSender.startSending();
////		//Thread.sleep(100);
////		assertTrue(client.testIntegration);
////		System.out.println("done that");
//	}
//	
////	@Test
////	public void sendServerTest() {
////		
////		
////	}
//
//}
