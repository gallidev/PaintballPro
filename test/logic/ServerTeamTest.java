package testing.logic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import enums.TeamEnum;
import serverLogic.Team;

public class ServerTeamTest {
	
	private Team team;

	@Before
	public void setUp() throws Exception {
		team = new Team();
	}

	@Test
	public void test() {
		//test initial team score
		assertEquals(team.getScore(), 0);
		
		//methods to be tested: 
		
		//setColour && getColour
		team.setColour(TeamEnum.RED);
		assertTrue(team.getColour() == TeamEnum.RED);
		
		team.setColour(TeamEnum.BLUE);
		assertTrue(team.getColour() == TeamEnum.BLUE);
		
		//getScore &; setScore
		//for(int i = 0; i < )
		//assertEquals(team.getScore(), actual);
		//increment score
		
		//addMember
		
		//setMembers
		
		//getMembers
		
		
		
		//getColour
		
		//getMemberNo
		
	}

}
=======
package logic;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import serverLogic.Team;

public class ServerTeamTest {
	
	private Team team;

	@Before
	public void setUp() throws Exception {
		team = new Team();
	}

	@Test
	public void test() {
		//methods to be tested: 
		
		//increment score
		
		//addMember
		
		//setMembers
		
		//getMembers
		
		//getScore
		
		//setScore
		
		//getColour
		
		//getMemberNo
		
		fail("Not yet implemented");
	}

}
