package serverLogic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import enums.TeamEnum;
import serverLogic.Team;
import serverLogic.TeamMatchMode;

/**
 * Tester for the TeamMatchMode logic simulation.
 * @author Alexandra Paduraru
 *
 */
public class TeamMatchModeTest {
	
	private Team red;
	private Team blue;
	private TeamMatchMode game;

	/**
	 * Initialises the game with the red and blue team.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		red = new Team(TeamEnum.RED);
		blue = new Team(TeamEnum.BLUE);
		game = new TeamMatchMode(red, blue);
	}

	/* Testers for all the functionality needed to run the logic in the Team Match Mode.*/

	@Test
	public void gameTimeTest() throws InterruptedException{
		
		game.start();
		
		assertFalse(game.isGameFinished());
		assertTrue(game.getTimer().getTimeLeft() >= 170);  

		Thread.sleep(180500);  
		assertTrue(game.isGameFinished());
	}
	
	@Test
	public void getRemaingTimeTest(){
		assertEquals(game.getRemainingTime(), game.getTimer().getTimeLeft());
		assertTrue(game.getRemainingTime() >= 150);
		assertTrue(game.getRemainingTime() <= 180);
	}
	
	
	@Test
	public void whoWonTest(){
		game.getBlueTeam().incrementScore(5);
		game.getRedTeam().incrementScore(2);
		
		assertTrue(game.getBlueTeam().getScore() > game.getRedTeam().getScore());
		assertFalse(game.whoWon().getColour() == TeamEnum.RED);
		assertTrue(game.whoWon().getColour() == TeamEnum.BLUE);
		
		game.getRedTeam().incrementScore(10);
		assertTrue(game.whoWon().getColour() == TeamEnum.RED);
		assertFalse(game.whoWon().getColour() == TeamEnum.BLUE);
		
		game.getBlueTeam().incrementScore(7);
		
//		game.whoWon();
//		game.getRedTeam().incrementScore(5);
//		System.out.println(game.getTimer().getTimeLeft());
//		assertTrue(game.getTimer().getTimeLeft() <=10);
//		assertTrue(game.getTimer().getTimeLeft() >=1);
		
	}
	
	
	@Test
	public void firstTeamTest(){
		assertTrue(game.getRedTeam() == red);

		Team newTeam = new Team(TeamEnum.RED);
		game.setRedTeam(newTeam);
		
		assertTrue(game.getRedTeam() == newTeam);
		assertFalse(game.getRedTeam() == red);
	}
	
	@Test
	public void secondTeamTest(){
		assertTrue(game.getBlueTeam() == blue);
		
		Team newSndTeam = new Team(TeamEnum.BLUE);
		game.setRedTeam(newSndTeam);

		//setSecondTeam
		assertTrue(game.getRedTeam() == newSndTeam);
		assertFalse(game.getRedTeam() == blue);
	}
	

}
