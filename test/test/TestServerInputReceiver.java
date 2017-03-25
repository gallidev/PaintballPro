package test;

import enums.GameMode;
import enums.TeamEnum;
import helpers.JavaFXTestHelper;
import integration.server.ServerGameSimulation;
import integration.server.ServerInputReceiver;
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
 * Test class to test the input receiver on the server, which receives
 * information from all clients regarding player locations.
 * Class tested- {@link ServerInputReceiver}
 * @author Alexandra Paduraru
 *
 */
public class TestServerInputReceiver {

	private ServerInputReceiver inputReceiver;
	private ArrayList<EssentialPlayer> players;
	private EssentialPlayer p;
	private ServerInputReceiver inputReceiver2;

	@Before
	public void setUp() throws Exception {
		inputReceiver = new ServerInputReceiver();
		players = new ArrayList<>();

		JavaFXTestHelper.setupApplication();
		Map map = Map.loadRaw("elimination");
		p = new UserPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, new CollisionsHandler(map), ImageFactory.getPlayerFlagImage(TeamEnum.RED), GameMode.TEAM_MATCH, ServerGameSimulation.GAME_HERTZ);

		players.add(p);

		inputReceiver2 = new ServerInputReceiver(players);
	}

	@Test
	public void updatePlayerTest() {
		inputReceiver.setPlayers(players);
		inputReceiver.updatePlayer(1, 100, true, false, true, false, true, 30);

		assertTrue(p.getUp());
		assertFalse(p.getDown());
		assertTrue(p.getLeft());
		assertFalse(p.getRight());
		assertTrue(p.isShooting());
		assertEquals(30.0, p.getAngle(), 0.2);

	}

	@Test
	public void setPlayersTest() {
		inputReceiver.setPlayers(players);

		assertEquals(inputReceiver.getPlayers(), players);

	}

	@Test
	public void getPlayersTest() {
		assertEquals(inputReceiver2.getPlayers(), players);

	}
}
