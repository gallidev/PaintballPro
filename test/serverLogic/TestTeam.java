package serverLogic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import enums.TeamEnum;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import players.EssentialPlayer;
import rendering.Map;

/**
 * Test class to test the team game logic.
 * Class tested - Team.java
 * @author Alexandra Paduraru
 *
 */
public class TestTeam {
	
	private Team team;

	/**
	 * Initialises a new team.
	 */
	@Before
	public void setUp() {
		team = new Team(TeamEnum.RED);
		
	}

	/* Testers for all the game logic functionality for teams */

	
	@Test
	public void incrementScoreTest() {
		assertEquals(team.getScore(),  0);
		
		team.incrementScore();
		assertEquals(team.getScore(),  1);

		team.incrementScore(5);
		assertEquals(team.getScore(),  6);
	}
	
	
	@Test
	public void getMembersNoTest() {
		assertEquals(team.getMembersNo(), 0);
	}
	
	@Test
	public void getScoreTest() {
		team.setScore(6);
		assertEquals(team.getScore(), 6);
	}
	
	@Test
	public void addMember(){
		//Map map = Map.loadRaw("ctf");
	}
	
	@Test
	public void setMembersTest(){
		ArrayList<EssentialPlayer> players = new ArrayList<>();
		team.setMembers(players);
		assertEquals(team.getMembersNo(), 0);
		assertTrue(team.getColour()== TeamEnum.RED);
	}
	
	@Test
	public void setScoreTest() {
		team.setScore(10);
		assertEquals(team.getScore(),  10);	
	}
		
	@Test
	public void colourTest() {
		assertTrue(team.getColour() == TeamEnum.RED);
		team.setColour(TeamEnum.BLUE);
		assertTrue(team.getColour() == TeamEnum.BLUE);
	}
	
	@Test
	public void getMembersTest() {
		assertTrue(team.getMembers().isEmpty());
	}
		

}
