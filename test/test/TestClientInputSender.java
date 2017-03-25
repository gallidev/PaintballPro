package test;

import enums.GameMode;
import enums.TeamEnum;
import gui.GUIManager;
import helpers.JavaFXTestHelper;
import integration.client.ClientInputSender;
import integration.server.ServerInputReceiver;
import networking.game.UDPClient;
import networking.game.UDPServer;
import networking.server.ClientTable;
import networking.server.LobbyTable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import physics.CollisionsHandler;
import physics.InputHandler;
import players.ClientPlayer;
import players.EssentialPlayer;
import rendering.ImageFactory;
import rendering.Map;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestClientInputSender {

	private UDPServer server;
	private UDPClient client;
	private InputHandler handler;
	private ClientPlayer player;
	private ServerInputReceiver inputReceiver;

	private ClientInputSender inputSender;

	@Before
	public void setUp() throws Exception {
		ClientTable table = new ClientTable();
		LobbyTable lobby = new LobbyTable();

		handler = new InputHandler();

		server = new UDPServer(table, lobby, 19877);
		server.start();

		JavaFXTestHelper.setupApplication();
		Map map = Map.loadRaw("desert");
		player = new ClientPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, new GUIManager(), new CollisionsHandler(map), new InputHandler(), ImageFactory.getPlayerImage(TeamEnum.RED), GameMode.TEAM_MATCH, 30);
		ArrayList<EssentialPlayer> players = new ArrayList<>();
		players.add(player);

		inputReceiver = new ServerInputReceiver();
		inputReceiver.setPlayers(players);
		server.setInputReceiver(inputReceiver);

		client = new UDPClient(1, "127.0.0.1", 19877,null, null, 25567, "test");
		client.start();

		inputSender = new ClientInputSender(client, handler, player);
	}

	@After
	public void tearDown() throws Exception {
		client.stopThread();
		server.m_running = false;
		server.interrupt();
		client.active = false;
	}

	@Test
	public void startSendingTest() throws InterruptedException {
		handler.setUp(true);
		handler.setDown(true);
		handler.setLeft(true);
		handler.setRight(true);
		handler.setShoot(true);

		//"0:1:Up:Left:Right:Shoot:2:3:0:0"
		inputSender.startSending();
		Thread.sleep(100);

		assertTrue(player.getUp());
		assertTrue(player.getDown());
		assertTrue(player.getLeft());
		assertTrue(player.getRight());
		assertTrue(player.isShooting());

		handler.setDown(false);
		inputSender.startSending();
		Thread.sleep(100);
		assertFalse(player.getDown());

	}

}