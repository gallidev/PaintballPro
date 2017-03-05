

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import enums.TeamEnum;
import serverLogic.Team;
import serverLogic.TeamMatchMode;

public class TeamMatchModeTest {
	
	private Team red;
	private Team blue;
	private TeamMatchMode game;

	@Before
	public void setUp() throws Exception {
		red = new Team(TeamEnum.RED);
		blue = new Team(TeamEnum.BLUE);
		game = new TeamMatchMode(red, blue);
	}

	@Test
	public void test() {
		//methods to be tested
		
		//start
		game.start();
		assertTrue(game.getTimer().getTimeLeft() >= 9);  	//SCHIMBA
		
		assertFalse(game.isGameFinished());
		
		game.getFirstTeam().incrementScore(5);
		game.getSecondTeam().incrementScore(10);

		
		try {
			Thread.sleep(10500);   //SCHIMBA
			assertTrue(game.isGameFinished());
			
		} catch (InterruptedException e) {
			System.out.println("Thread couldn't sleep.");
			System.exit(1);
		}
		
		//whoWon
		assertFalse(game.whoWon().getColour() == TeamEnum.RED);
		assertTrue(game.whoWon().getColour() == TeamEnum.BLUE);

		//setFirstTeam
		Team newTeam = new Team(TeamEnum.RED);
		game.setFirstTeam(newTeam);

		
		//getFirstTeam
		assertTrue(game.getFirstTeam() == newTeam);
		assertFalse(game.getFirstTeam() == red);
		
		//setSecondTeam
		Team newSndTeam = new Team(TeamEnum.BLUE);
		game.setFirstTeam(newSndTeam);

		//setSecondTeam
		assertTrue(game.getFirstTeam() == newSndTeam);
		assertFalse(game.getFirstTeam() == blue);
		
	}

}
