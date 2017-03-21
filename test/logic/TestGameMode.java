package logic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import enums.TeamEnum;
import serverLogic.Team;
import serverLogic.TeamMatchMode;

/**
 * Test class for the GameMode abstract class.
 * @author Alexandra Paduraru
 *
 */
public class TestGameMode {

	GameMode game;
	Team red = new Team(TeamEnum.RED);
	Team blue = new Team(TeamEnum.BLUE);
	
	/**
	 * Initialises the team.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		game = new TeamMatchMode(red, blue);
	}

	/* Testers for all methods implemented in the GameMode class */
	
	@Test
	public void getRedTeamTest() {
		assertTrue(game.getRedTeam() == red);
	}

	@Test
	public void getBlueTeamTest() {
		assertTrue(game.getBlueTeam() == blue);
	}
	
	@Test
	public void setRedTeamTest() {
		Team newTeam = new Team(TeamEnum.RED);
		game.setRedTeam(newTeam);
		assertTrue(game.getRedTeam() == newTeam);
	}
	
	@Test
	public void setBlueTeamTest() {
		Team newTeam = new Team(TeamEnum.RED);
		game.setBlueTeam(newTeam);
		assertTrue(game.getBlueTeam() == newTeam);
	}
	
}
