package serverLogic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import enums.TeamEnum;
import logic.GameMode;

/**
 * Tester class for the CaptureTheFlagGameMode.
 * @author Alexandra Paduraru
 *
 */
public class TestCaptureTheFlagMode {

	CaptureTheFlagMode game;
	Team red;
	Team blue;
	
	/**
	 * Initialises the game and the two teams.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		red = new Team(TeamEnum.RED);
		blue = new Team(TeamEnum.BLUE);
		game = new CaptureTheFlagMode(red, blue);
	}

	/* Testers for all the functionality needed to run the logic in the Capture the Flag Mode*/
	
	@Test
	public void startTest() {
		game.start();
		
		assertFalse(game.isGameFinished());
		assertTrue(game.getRemainingTime() >= 290);  
	}
	
	@Test
	public void getRemainingTimeTest(){
		assertTrue(game.getRemainingTime() >= 290);
		assertTrue(game.getRemainingTime() <= 300);
	}
	
	@Test
	public void flagCapturedTest() {
		game.flagCaptured(red);
		
		assertTrue(game.getRedTeam().getScore() > game.getBlueTeam().getScore());
		
		game.getBlueTeam().incrementScore();
		game.flagCaptured(blue);
		assertTrue(game.getBlueTeam().getScore() > game.getRedTeam().getScore());
		
	}
	
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
		assertTrue(game.getRemainingTime() <=30);
		assertTrue(game.getRemainingTime() >=1);
	}
	
	@Test
	public void isGameFinishedTest(){
		assertFalse(game.isGameFinished());
	}
}
