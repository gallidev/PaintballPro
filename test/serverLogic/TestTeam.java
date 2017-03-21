package serverLogic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import enums.GameMode;
import enums.TeamEnum;
import helpers.JavaFXTestHelper;
import logic.server.Team;
import physics.CollisionsHandler;
import players.EssentialPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;

/**
 * Test class to test the team game logic.
 * Class tested - Team.java
 * @author Alexandra Paduraru
 *
 */
public class TestTeam {
	
	private Team team;
	private EssentialPlayer p;
	private Map map;

	/**
	 * Initialises a new team.
	 */
	@Before
	public void setUp() {
		team = new Team(TeamEnum.RED);
		
		JavaFXTestHelper.setupApplication();
		map = Map.loadRaw("ctf");
		p = new UserPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, new CollisionsHandler(map), ImageFactory.getPlayerImage(TeamEnum.RED), GameMode.ELIMINATION, 30);
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
	public void addMemberTest(){
		
		team.addMember(p);
		
		assertTrue(team.getMembers().get(0) == p);
	}
	
	@Test
	public void containsMemberTest(){
		
		team.addMember(p);
		EssentialPlayer p1 = new UserPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, new CollisionsHandler(map), ImageFactory.getPlayerImage(TeamEnum.RED), GameMode.ELIMINATION, 30);

		assertTrue(team.containsPlayer(p));
		assertFalse(team.containsPlayer(p1));

	}
	
	
	@Test
	public void setMembersTest(){
		ArrayList<EssentialPlayer> players = new ArrayList<>();
		team.setMembers(players);
		assertEquals(team.getMembersNo(), 0);
		assertTrue(team.getColour()== TeamEnum.RED);
		
		players.add(p);
		team.setMembers(players);
		assertTrue(team.getMembers().get(0) == p);
		assertTrue(team.getMembersNo() == 1);
		assertTrue(team.getColour() == TeamEnum.RED);
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
