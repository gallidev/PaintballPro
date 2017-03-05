import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import enums.TeamEnum;
import serverLogic.Team;

public class ServerTeamTest {
	
	private Team team;

	@Before
	public void setUp() throws Exception {
		team = new Team(TeamEnum.RED);
	}

	@Test
	public void test() {
		//test initial score
		assertEquals(team.getScore(),  0);
		
		//methods to be tested: 
		
		//increment score and getScore
		team.incrementScore(1);
		assertEquals(team.getScore(),  1);

		team.incrementScore(5);
		assertEquals(team.getScore(),  6);
		
		//setScore
		team.setScore(10);
		assertEquals(team.getScore(),  10);	
		
		//setColour and getColour
		assertTrue(team.getColour() == TeamEnum.RED);
		team.setColour(TeamEnum.BLUE);
		assertTrue(team.getColour() == TeamEnum.BLUE);

		//getMemberNo
		assertEquals(team.getMembersNo(), 0);
		
		//getMembers
		assertTrue(team.getMembers().isEmpty());
		
		
	}

}
