package integrationServer;

import enums.GameMode;
import enums.TeamEnum;
import helpers.JavaFXTestHelper;
import networking.game.ServerGameSimulation;
import networking.game.UDPServer;
import networking.server.ClientTable;
import networking.server.LobbyTable;
import networking.server.ServerInputReceiver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import physics.CollisionsHandler;
import players.EssentialPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test class to tests the server-sided integration: messages received by the
 * clients.
 * Classes tested {@link UDPServer}
 *
 * @author Alexandra Paduraru
 *
 */
public class TestServerIntegration {

	private ServerInputReceiver inputReceiver;
	private EssentialPlayer player;
	private UDPServer server;

	@Before
	public void setUp() throws Exception {
		ClientTable clientTable;
		LobbyTable lobby;

		clientTable = new ClientTable();
		lobby = new LobbyTable();

		server = new UDPServer(clientTable, lobby, 19877);
		inputReceiver = new ServerInputReceiver();

		JavaFXTestHelper.setupApplication();
		Map map;
		map = Map.loadRaw("desert");
		player = new UserPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, new CollisionsHandler(map),
				ImageFactory.getPlayerFlagImage(TeamEnum.RED), GameMode.TEAM_MATCH, ServerGameSimulation.GAME_HERTZ);

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

	/**
	 * Method to test that the player is updated correctly on the client-side
	 * when a server sends a new move.
	 */
	@Test
	public void playerInputChangedTest() {
		String input = "0:1:10:Up:Left:Shoot:Angle:30";

		assertNotNull(server);
		server.playerInputChanged(input);

		assertFalse(player.getRight());
		assertFalse(player.getDown());
		assertTrue(player.getUp());
		assertTrue(player.getLeft());
		assertTrue(player.isShooting());
		assertTrue(player.getAngle() == 30.0);

		input = "0:1:11:Right:Down";
		server.playerInputChanged(input);

		assertTrue(player.getRight());
		assertTrue(player.getDown());
		assertFalse(player.getUp());
		assertFalse(player.getLeft());
		assertFalse(player.isShooting());
	}

	/**
	 * Method to check that the winning team is sent correctly to the cliennt.
	 */
	@Test
	public void getWinnerTest() {
		String input = "2:Red";

		server.getWinner(input);

		assertTrue("Red".equals(server.winnerTest));
	}

}
