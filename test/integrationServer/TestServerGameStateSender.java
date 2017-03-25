package integrationServer;

import enums.GameMode;
import enums.TeamEnum;
import gui.GUIManager;
import helpers.JavaFXTestHelper;
import integration.client.ClientInputSender;
import integration.server.ServerGameSimulation;
import integration.server.ServerGameStateSender;
import logic.server.Team;
import logic.server.TeamMatchMode;
import networking.game.UDPClient;
import networking.game.UDPServer;
import networking.server.ClientTable;
import networking.server.LobbyTable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import physics.CollisionsHandler;
import physics.InputHandler;
import physics.PowerupType;
import players.ClientPlayer;
import players.EssentialPlayer;
import players.GhostPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;
import rendering.Renderer;

import java.util.ArrayList;

/**
 * Test class for a server game state sender, which transmits the game data to
 * the clients. Class Tested {@link ServerGameStateSender}
 * 
 * @author Filippo Galli
 *
 */
public class TestServerGameStateSender {

	// players
	private GhostPlayer ghostPlayer;
	private InputHandler handler;
	private ClientPlayer player;
	private UserPlayer userPlayer1;
	private UserPlayer userPlayer2;

	// networking
	private UDPClient client1;
	private UDPClient client2;
	private ServerGameSimulation gameSimulation;
	private ClientInputSender inputSender;
	private LobbyTable lobby;
	private UDPServer server;
	private ClientTable table;
	private ServerGameStateSender serverGameStateSender;

	/**
	 * Initialising all the required networking and ServerGameState Sender
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		table = new ClientTable();

		lobby = new LobbyTable();
		lobby.testEnv = true;

		int id = table.add("test");

		lobby.addPlayerToLobby(table.getPlayer(id), 1, null, null);

		server = new UDPServer(table, lobby, 19999);
		server.start();
		Thread.sleep(500);

		JavaFXTestHelper.setupApplication();
		Map map;
		map = Map.loadRaw("castle");

		CollisionsHandler ch;
		ch = new CollisionsHandler(map);

		player = new ClientPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, new GUIManager(), ch, new InputHandler(),
				ImageFactory.getPlayerImage(TeamEnum.RED), GameMode.CAPTURE_THE_FLAG, Renderer.TARGET_FPS);

		ghostPlayer = new GhostPlayer(0, 0, 2, map.getSpawns(), TeamEnum.BLUE, ch, GameMode.CAPTURE_THE_FLAG,
				Renderer.TARGET_FPS);

		userPlayer1 = new UserPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, ch,
				ImageFactory.getPlayerImage(TeamEnum.RED), GameMode.CAPTURE_THE_FLAG, ServerGameSimulation.GAME_HERTZ);
		userPlayer2 = new UserPlayer(0, 0, 2, map.getSpawns(), TeamEnum.BLUE, ch,
				ImageFactory.getPlayerImage(TeamEnum.BLUE), GameMode.CAPTURE_THE_FLAG, ServerGameSimulation.GAME_HERTZ);

		ArrayList<EssentialPlayer> playersForServer = new ArrayList<>();
		playersForServer.add(userPlayer1);
		playersForServer.add(userPlayer2);

		ch.setPlayers(playersForServer);

		Team red = new Team(TeamEnum.RED);
		Team blue = new Team(TeamEnum.BLUE);

		red.incrementScore();
		red.addMember(userPlayer1);
		blue.addMember(userPlayer2);
		TeamMatchMode game = new TeamMatchMode(red, blue);

		gameSimulation = new ServerGameSimulation(game);

		serverGameStateSender = new ServerGameStateSender(server, playersForServer, 1);
		serverGameStateSender.setGameLoop(gameSimulation);

		client1 = new UDPClient(id, "127.0.0.1", 19999, null, null, 25568, "test");

		id = table.add("test2");
		lobby.addPlayerToLobby(table.getPlayer(id), 1, null, null);
		lobby.switchTeams(table.getPlayer(id), null);

		client2 = new UDPClient(id, "127.0.0.1", 19999, null, null, 25569, "test");

		client1.testNetworking = true;
		client2.testNetworking = true;

		client1.start();
		client2.start();

		server.sendToAll("TestSendToAll", "127.0.0.1:" + client1.port);
	}

	@After
	public void tearDown() throws Exception {
		client1.stopThread();
		client2.stopThread();
		server.m_running = false;
		server.interrupt();
		client1.active = false;
		client2.active = false;
	}

	/*
	 * @Test public void startSendingTest() throws InterruptedException {
	 * 
	 * serverGameStateSender.startSending();
	 * 
	 * Thread.sleep(1000);
	 * 
	 * serverGameStateSender.stopSending();
	 * 
	 * }
	 */

	/**
	 * Method to test if the server is sending moves, bullets, flag actions and
	 * power-up stats correctly to the clients.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testSendingEverything(
			) throws InterruptedException {
		gameSimulation.runGameLoop();

		serverGameStateSender.startSending();

		serverGameStateSender.onShotBullet(1, 1, 0, 0, 1.35);
		serverGameStateSender.onBulletKills(1, 1);
		serverGameStateSender.onFlagCaptured(1);
		serverGameStateSender.onFlagDropped(1);
		serverGameStateSender.onFlagRespawned(1);
		serverGameStateSender.onPowerupAction(PowerupType.SHIELD, 2);
		serverGameStateSender.onPowerupAction(PowerupType.SPEED, 1);
		serverGameStateSender.onPowerupRespawn(PowerupType.SPEED, 1);
		serverGameStateSender.onPowerupRespawn(PowerupType.SHIELD, 1);
		serverGameStateSender.sendWinner();
		serverGameStateSender.sendShieldRemoved(1);

		Thread.sleep(2000);

		serverGameStateSender.stopSending();
		gameSimulation.stopGameLoop();

	}

}
