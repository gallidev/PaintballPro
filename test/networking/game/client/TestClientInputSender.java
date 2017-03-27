package networking.game.client;

import enums.GameMode;
import enums.TeamEnum;
import gui.GUIManager;
import helpers.JavaFXTestHelper;
import networking.client.ClientInputSender;
import networking.game.UDPClient;
import networking.game.UDPServer;
import networking.server.ClientTable;
import networking.server.LobbyTable;
import networking.server.ServerInputReceiver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import physics.CollisionsHandler;
import physics.InputHandler;
import players.ClientPlayer;
import players.EssentialPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test class to test the information that a client sends to the server, based
 * on the user input.
 * Class tested - {@link ClientInputSender}
 *
 * @author Alexandra Paduraru
 *
 */
public class TestClientInputSender {

	private UDPClient client;
	private InputHandler handler;
	private ServerInputReceiver inputReceiver;
	private ClientInputSender inputSender;
	private ClientPlayer cPlayer;
	private UserPlayer uPlayer;
	private UDPServer server;

	@Before
	public void setUp() throws Exception {
		LobbyTable lobby;
		ClientTable table;

		table = new ClientTable();
		lobby = new LobbyTable();

		handler = new InputHandler();
		server = new UDPServer(table, lobby, 19887);
		server.start();

		JavaFXTestHelper.setupApplication();
		Map map;
		map = Map.loadRaw("desert");

		cPlayer = new ClientPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, new GUIManager(), new CollisionsHandler(map),
				new InputHandler(), ImageFactory.getPlayerImage(TeamEnum.RED), GameMode.TEAM_MATCH, 30);
		uPlayer = new UserPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, new CollisionsHandler(map),
				ImageFactory.getPlayerImage(TeamEnum.RED), GameMode.TEAM_MATCH, 30);
		ArrayList<EssentialPlayer> players;
		players = new ArrayList<>();
		players.add(uPlayer);

		inputReceiver = new ServerInputReceiver();
		inputReceiver.setPlayers(players);
		server.setInputReceiver(inputReceiver);

		client = new UDPClient(1, "127.0.0.1", 19887,null, null, 25567, "test");
		client.start();

		inputSender = new ClientInputSender(client, handler, cPlayer);
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



		// "0:1:<counterFrame>:Up:Left:Right:Shoot:2:3:0:0"
		inputSender.startSending();
		Thread.sleep(1500);


		assertEquals(ClientInputSender.counterFrame, uPlayer.getCounterFrame());
		assertTrue(uPlayer.getUp());
		assertTrue(uPlayer.getDown());
		assertTrue(uPlayer.getLeft());
		assertTrue(uPlayer.getRight());
		assertTrue(uPlayer.isShooting());

		handler.setDown(false);
		inputSender.startSending();
		Thread.sleep(100);
		assertFalse(uPlayer.getDown());

	}

}