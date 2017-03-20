package networking;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import enums.TeamEnum;
import networking.client.TeamTable;
import players.GhostPlayer;

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
		table = new TeamTable();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTeamTable() {
		assertNotNull(table);
	}

	@Test
	public void testMyTeam() {
		ArrayList<GhostPlayer> team = new ArrayList<GhostPlayer>();
		team.add(new GhostPlayer(0, 0, 1, null, null, TeamEnum.BLUE));
		team.add(new GhostPlayer(0, 0, 2, null, null, TeamEnum.BLUE));
		table.setMyTeam(team);
		ArrayList<GhostPlayer> test = table.getMyTeam();
		assertEquals(test.get(1).getPlayerId(),2);
		assertEquals(test.get(0).getPlayerId(),1);
	}
	
	@Test
	public void testEnemies() {
		ArrayList<GhostPlayer> team = new ArrayList<GhostPlayer>();
		team.add(new GhostPlayer(0, 0, 3, null, null, TeamEnum.RED));
		team.add(new GhostPlayer(0, 0, 4, null, null, TeamEnum.RED));
		table.setEnemies(team);
		ArrayList<GhostPlayer> test = table.getEnemies();
		assertEquals(test.get(1).getPlayerId(),4);
		assertEquals(test.get(0).getPlayerId(),3);
	}
}