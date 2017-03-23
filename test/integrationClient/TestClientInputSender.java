package integrationClient;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
import physics.CollisionsHandler;
import physics.InputHandler;
import players.ClientPlayer;
import players.EssentialPlayer;
import rendering.ImageFactory;
import rendering.Map;

/**
 * Test class to test the information that a client sends to the server, based
 * on the user input.
 * Class tested - {@link ClientInputSender}
 *
 * @author Alexandra Paduraru
 *
 */
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

		server = new UDPServer(table, lobby, 19887);
		server.start();

		JavaFXTestHelper.setupApplication();
		Map map = Map.loadRaw("elimination");
		player = new ClientPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, new GUIManager(), new CollisionsHandler(map),
				new InputHandler(), ImageFactory.getPlayerImage(TeamEnum.RED), GameMode.ELIMINATION, 30);
		ArrayList<EssentialPlayer> players = new ArrayList<>();
		players.add(player);

		inputReceiver = new ServerInputReceiver();
		inputReceiver.setPlayers(players);
		server.setInputReceiver(inputReceiver);

		client = new UDPClient(1, "127.0.0.1", 19887,null, null, 25567, "test");
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

	/**
	 * Method to test that the client sends the correct input.
	 *
	 * @throws InterruptedException
	 */
	@Test
	public void startSendingTest() throws InterruptedException {
		handler.setUp(true);
		handler.setDown(true);
		handler.setLeft(true);
		handler.setRight(true);
		handler.setShoot(true);

		// "0:1:Up:Left:Right:Shoot:2:3:0:0"
		inputSender.startSending();
		Thread.sleep(1500);

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