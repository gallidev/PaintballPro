package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import enums.GameMode;
import enums.TeamEnum;
import helpers.JavaFXTestHelper;
import integration.server.ServerGameSimulation;
import integration.server.ServerInputReceiver;
import networking.game.UDPServer;
import networking.server.ClientTable;
import networking.server.LobbyTable;
import networking.server.ServerReceiver;
import physics.CollisionsHandler;
import players.EssentialPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;

/**
 * Test class to tests the server-sided integration: messages received by the
 * clients.
 * Classes tested {@link UDPServer}
 * @author Alexandra Paduraru
 *
 */
public class TestServerIntegration {

	private UDPServer server;
	private ServerInputReceiver inputReceiver;
	private EssentialPlayer player;


	@Before
	public void setUp() throws Exception {
		ClientTable clientTable = new ClientTable();
		LobbyTable lobby = new LobbyTable();
		
		server = new UDPServer(clientTable, lobby, 19877);
		inputReceiver = new ServerInputReceiver();
		
		JavaFXTestHelper.setupApplication();
		Map map = Map.loadRaw("elimination");
		player = new UserPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, new CollisionsHandler(map), ImageFactory.getPlayerFlagImage(TeamEnum.RED), GameMode.ELIMINATION, ServerGameSimulation.GAME_HERTZ);
		
		ArrayList<EssentialPlayer> players = new ArrayList<>();
		players.add(player);
		inputReceiver.setPlayers(players);
		
		server.setInputReceiver(inputReceiver);
	}
	
	@After
	public void tearDown() throws Exception {
		server.m_running = false;
		server.interrupt();
	}

	@Test
	public void playerInputChangedTest() {
		String input = "0:1:Up:Left:Shoot:Angle:30";
		
		assertNotNull(server);
		server.playerInputChanged(input);
		
		assertFalse(player.getRight());
		assertFalse(player.getDown());
		assertTrue(player.getUp());
		assertTrue(player.getLeft());
		assertTrue(player.isShooting());
		assertTrue(player.getAngle() == 30.0);
		
		input = "0:1:Right:Down";
		server.playerInputChanged(input);
		
		assertTrue(player.getRight());
		assertTrue(player.getDown());
		assertFalse(player.getUp());
		assertFalse(player.getLeft());
		assertFalse(player.isShooting());
	}

	@Test
	public void getWinnerTest() {
		String input = "2:Red";
		
		server.getWinner(input);
		
		assertTrue("Red".equals(server.winnerTest));
	}

}
