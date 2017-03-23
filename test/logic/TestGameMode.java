package logic;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import enums.TeamEnum;
import logic.server.Team;
import logic.server.TeamMatchMode;

/**
 * Test class for the GameMode abstract class. Class tested - {@link GameMode}
 * 
 * @author Alexandra Paduraru
 *
 */
public class TestGameMode {

	GameMode game;
	
	//teams
	Team red = new Team(TeamEnum.RED);
	Team blue = new Team(TeamEnum.BLUE);

	/**
	 * Initialises the team.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		game = new TeamMatchMode(red, blue);
	}

	/**
	 * Method to test if the if the red team is correct.
	 */
	@Test
	public void getRedTeamTest() {
		assertTrue(game.getRedTeam() == red);
	}

	/**
	 * Method to test if the if the blue team is correct.
	 */
	@Test
	public void getBlueTeamTest() {
		assertTrue(game.getBlueTeam() == blue);
	}

	/**
	 * Method to test if the if the red team is assigned correctly.
	 */
	@Test
	public void setRedTeamTest() {
		Team newTeam;
		newTeam = new Team(TeamEnum.RED);
		game.setRedTeam(newTeam);
		assertTrue(game.getRedTeam() == newTeam);
	}

	/**
	 * Method to test if the if the blue team is assigned correctly.
	 */
	@Test
	public void setBlueTeamTest() {
		Team newTeam;
		newTeam = new Team(TeamEnum.RED);
		game.setBlueTeam(newTeam);
		assertTrue(game.getBlueTeam() == newTeam);
	}

}
