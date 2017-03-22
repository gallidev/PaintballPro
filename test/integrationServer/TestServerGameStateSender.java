package integrationServer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import enums.GameMode;
import enums.TeamEnum;
import gui.GUIManager;
import helpers.JavaFXTestHelper;
import integration.client.ClientInputSender;
import integration.server.ServerGameSimulation;
import integration.server.ServerGameStateSender;
import integration.server.ServerInputReceiver;
import logic.server.Team;
import logic.server.TeamMatchMode;
import networking.game.UDPClient;
import networking.game.UDPServer;
import networking.server.ClientTable;
import networking.server.LobbyTable;
import physics.CollisionsHandler;
import physics.InputHandler;
import players.ClientPlayer;
import players.EssentialPlayer;
import players.GhostPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;
import rendering.Renderer;

public class TestServerGameStateSender {

	private UDPServer server;
	private UDPClient client;
	private InputHandler handler;
	private UserPlayer userPlayer1;
	private UserPlayer userPlayer2;

	private ClientPlayer player;
	private GhostPlayer ghostPlayer;
	private ServerGameStateSender serverGameStateSender;

	private ClientInputSender inputSender;

	@Before
	public void setUp() throws Exception {
		ClientTable table = new ClientTable();
		LobbyTable lobby = new LobbyTable();

//		int clientId = table.add("Filippo");
//		table.addNewIP("127.0.0.1", clientId);
//

		handler = new InputHandler();

		server = new UDPServer(table, lobby, 19877);

//		lobby.addPlayerToLobby(table.getPlayer(clientId), 1, null, server);

		server.start();

		JavaFXTestHelper.setupApplication();
		Map map = Map.loadRaw("ctf");

		CollisionsHandler ch = new CollisionsHandler(map);

		player = new ClientPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, new GUIManager(), ch , new InputHandler(), ImageFactory.getPlayerImage(TeamEnum.RED), GameMode.CAPTURETHEFLAG, Renderer.TARGET_FPS);

		ghostPlayer = new GhostPlayer(0, 0, 2, map.getSpawns(), TeamEnum.BLUE, ch, GameMode.CAPTURETHEFLAG, Renderer.TARGET_FPS);

		userPlayer1 = new UserPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, ch, ImageFactory.getPlayerImage(TeamEnum.RED), GameMode.CAPTURETHEFLAG, ServerGameSimulation.GAME_HERTZ);
		userPlayer2 = new UserPlayer(0, 0, 2, map.getSpawns(), TeamEnum.BLUE, ch, ImageFactory.getPlayerImage(TeamEnum.BLUE), GameMode.CAPTURETHEFLAG, ServerGameSimulation.GAME_HERTZ);


		ArrayList<EssentialPlayer> playersForServer = new ArrayList<>();
		playersForServer.add(userPlayer1);
		playersForServer.add(userPlayer2);

		ch.setPlayers(playersForServer);

		//lobbyId should be 1
		serverGameStateSender = new ServerGameStateSender(server, playersForServer, 1);

		client = new UDPClient(1, "127.0.0.1", 19877,null, null, 25567, "test");
		client.start();

		//inputSender = new ClientInputSender(client, handler, player);
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

		serverGameStateSender.startSending();

		Thread.sleep(1000);

		serverGameStateSender.stopSending();

	}

	@Test
	public void testSendingBullet() throws InterruptedException {

		serverGameStateSender.onShotBullet(1, 1, 0, 0, 1.35);

		Thread.sleep(1000);

		serverGameStateSender.stopSending();

	}

}
