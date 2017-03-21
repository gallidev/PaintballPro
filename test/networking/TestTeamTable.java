package networking;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import enums.TeamEnum;
import helpers.JavaFXTestHelper;
import networking.client.TeamTable;
import players.EssentialPlayer;
import players.GhostPlayer;
import rendering.Renderer;

/**
 * Class to test client representation of Team Tables -
 * a class to store friendly and enemy team members.
 * Tests - TeamTable.java
 *
 * @author Matthew Walters
 */
public class TestTeamTable {

	TeamTable table;

	@Before
	public void setUp() throws Exception {
		JavaFXTestHelper.setupApplication();
		table = new TeamTable();
	}

	@After
	public void tearDown() throws Exception {
		table = null;
	}

	@Test
	public void testTeamTable() {
		assertNotNull(table);
	}

	@Test
	public void testMyTeam() {
		ArrayList<EssentialPlayer> team = new ArrayList<EssentialPlayer>();
		team.add(new GhostPlayer(0, 0, 1, null, TeamEnum.BLUE, null, null, Renderer.TARGET_FPS));
		team.add(new GhostPlayer(0, 0, 2, null, TeamEnum.BLUE, null, null, Renderer.TARGET_FPS));
		table.setMyTeam(team);
		ArrayList<EssentialPlayer> test = table.getMyTeam();
		assertEquals(test.get(1).getPlayerId(),2);
		assertEquals(test.get(0).getPlayerId(),1);
	}

	@Test
	public void testEnemies() {
		ArrayList<EssentialPlayer> team = new ArrayList<EssentialPlayer>();
		team.add(new GhostPlayer(0, 0, 3, null, TeamEnum.RED, null, null, Renderer.TARGET_FPS));
		team.add(new GhostPlayer(0, 0, 4, null, TeamEnum.RED, null, null, Renderer.TARGET_FPS));
		table.setEnemies(team);
		ArrayList<EssentialPlayer> test = table.getEnemies();
		assertEquals(test.get(1).getPlayerId(),4);
		assertEquals(test.get(0).getPlayerId(),3);
	}
}