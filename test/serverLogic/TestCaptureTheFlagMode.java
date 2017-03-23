package serverLogic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import enums.TeamEnum;
import logic.server.CaptureTheFlagMode;
import logic.server.Team;

/**
 * Test class for the CaptureTheFlagGameMode logic.
 * 
 * @author Alexandra Paduraru
 *
 */
public class TestCaptureTheFlagMode {

	CaptureTheFlagMode game;
	
	//teams
	Team red;
	Team blue;

	/**
	 * Initialises the game and the two teams.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		red = new Team(TeamEnum.RED);
		blue = new Team(TeamEnum.BLUE);
		game = new CaptureTheFlagMode(red, blue);
	}

	/**
	 * Method to test the game start.
	 */
	@Test
	public void startTest() {
		game.start();

		assertFalse(game.isGameFinished());
		assertTrue(game.getRemainingTime() >= 290);
	}

	/**
	 * Method to test the game remaining time.
	 */
	@Test
	public void getRemainingTimeTest() {
		assertTrue(game.getRemainingTime() >= 290);
		assertTrue(game.getRemainingTime() <= 300);
	}

	/**
	 * Method to test the game behaviour when a flag is captured.
	 */
	@Test
	public void flagCapturedTest() {
		game.flagCaptured(red);

		assertTrue(game.getRedTeam().getScore() > game.getBlueTeam().getScore());

		game.getBlueTeam().incrementScore();
		game.flagCaptured(blue);
		assertTrue(game.getBlueTeam().getScore() > game.getRedTeam().getScore());

	}

	/**
	 * Method to test the winning team.
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
		System.out.println(game.getRemainingTime());
		assertTrue(game.getRemainingTime() <= 30);
		assertTrue(game.getRemainingTime() >= 1);
	}

	/**
	 * Method to test the game end.
	 */
	@Test
	public void isGameFinishedTest() {
		assertFalse(game.isGameFinished());
	}
}
