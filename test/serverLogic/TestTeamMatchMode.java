package serverLogic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import enums.TeamEnum;
import logic.server.Team;
import logic.server.TeamMatchMode;

/**
 * Test class to test how the TeamMatchMode logic simulation behaves. Classes
 * tested {@link TeamMatchMode}
 * 
 * @author Alexandra Paduraru
 *
 */
public class TestTeamMatchMode {

	private TeamMatchMode game;
	
	//teams
	private Team blue;
	private Team red;
	
	/**
	 * Initialises the game with the red and blue team.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		red = new Team(TeamEnum.RED);
		blue = new Team(TeamEnum.BLUE);
		game = new TeamMatchMode(red, blue);
	}

	/**
	 * Method to test the remaining game time and the if the game finishes at
	 * the right time.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void gameTimeTest() throws InterruptedException {
		game.start();

		assertFalse(game.isGameFinished());
		assertTrue(game.getTimer().getTimeLeft() >= 170);
	}

	/**
	 * Method to test the remaining game time.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void getRemaingTimeTest() {
		assertEquals(game.getRemainingTime(), game.getTimer().getTimeLeft());
		assertTrue(game.getRemainingTime() >= 150);
		assertTrue(game.getRemainingTime() <= 180);
	}

	/**
	 * Method to test the winning team.
	 * @throws InterruptedException
	 */
	@Test
	public void whoWonTest() {
		game.getBlueTeam().incrementScore(5);
		game.getRedTeam().incrementScore(2);

		assertTrue(game.getBlueTeam().getScore() > game.getRedTeam().getScore());
		assertFalse(game.whoWon().getColour() == TeamEnum.RED);
		assertTrue(game.whoWon().getColour() == TeamEnum.BLUE);

		game.getRedTeam().incrementScore(10);
		assertTrue(game.whoWon().getColour() == TeamEnum.RED);
		assertFalse(game.whoWon().getColour() == TeamEnum.BLUE);

		game.getBlueTeam().incrementScore(7);

		game.whoWon();
		game.getRedTeam().setScore(5);
		assertTrue(game.getTimer().getTimeLeft() <= 30);
		assertTrue(game.getTimer().getTimeLeft() >= 1);

	}

	/**
	 * Method to test the correctness of the first team.
	 * @throws InterruptedException
	 */
	@Test
	public void firstTeamTest() {
		assertTrue(game.getRedTeam() == red);

		Team newTeam = new Team(TeamEnum.RED);
		game.setRedTeam(newTeam);

		assertTrue(game.getRedTeam() == newTeam);
		assertFalse(game.getRedTeam() == red);
	}

	/**
	 * Method to test the correctness of the second team.
	 * @throws InterruptedException
	 */
	@Test
	public void secondTeamTest() {
		assertTrue(game.getBlueTeam() == blue);

		Team newSndTeam = new Team(TeamEnum.BLUE);
		game.setRedTeam(newSndTeam);

		// setSecondTeam
		assertTrue(game.getRedTeam() == newSndTeam);
		assertFalse(game.getRedTeam() == blue);
	}

}
