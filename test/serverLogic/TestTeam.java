package serverLogic;

import enums.GameMode;
import enums.TeamEnum;
import helpers.JavaFXTestHelper;
import integration.server.ServerGameSimulation;
import logic.server.Team;
import org.junit.Before;
import org.junit.Test;
import physics.CollisionsHandler;
import players.EssentialPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test class to test the team game logic. Class tested {@link Team}
 * 
 * @author Alexandra Paduraru
 *
 */
public class TestTeam {

	private EssentialPlayer player;
	private Map map;
	private Team team;
	

	/**
	 * Initialises a new team.
	 */
	@Before
	public void setUp() {
		team = new Team(TeamEnum.RED);

		JavaFXTestHelper.setupApplication();
		map = Map.loadRaw("ctf");
		player = new UserPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, new CollisionsHandler(map),
				ImageFactory.getPlayerImage(TeamEnum.RED), GameMode.TEAM_MATCH, 30);
	}

	/**
	 * Method to test if the score is incremented correctly.
	 */
	@Test
	public void incrementScoreTest() {
		assertEquals(team.getScore(), 0);

		team.incrementScore();
		assertEquals(team.getScore(), 1);

		team.incrementScore(5);
		assertEquals(team.getScore(), 6);
	}

	/**
	 * Method to test that the team has the correct players.
	 */
	@Test
	public void getMembersNoTest() {
		assertEquals(team.getMembersNo(), 0);
	}

	/**
	 * Method to test if the score is correct.
	 */
	@Test
	public void getScoreTest() {
		team.setScore(6);
		assertEquals(team.getScore(), 6);
	}

	/**
	 * Method to test if a member is added to the team.
	 */
	@Test
	public void addMemberTest() {

		team.addMember(player);

		assertTrue(team.getMembers().get(0) == player);
	}

	/**
	 * Method to test if the reliability of the containsPlayer method.
	 */
	@Test
	public void containsMemberTest() {

		team.addMember(player);
		EssentialPlayer p1;
		p1 = new UserPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, new CollisionsHandler(map),
				ImageFactory.getPlayerImage(TeamEnum.RED), GameMode.TEAM_MATCH, ServerGameSimulation.GAME_HERTZ);

		assertTrue(team.containsPlayer(player));
		assertFalse(team.containsPlayer(p1));

	}

	/**
	 * Method to test if the memebrs are assigned correctly.
	 */
	@Test
	public void setMembersTest() {
		ArrayList<EssentialPlayer> players = new ArrayList<>();
		team.setMembers(players);
		assertEquals(team.getMembersNo(), 0);
		assertTrue(team.getColour() == TeamEnum.RED);

		players.add(player);
		team.setMembers(players);
		assertTrue(team.getMembers().get(0) == player);
		assertTrue(team.getMembersNo() == 1);
		assertTrue(team.getColour() == TeamEnum.RED);
	}

	/**
	 * Method to test if the score is set correctly.
	 */
	@Test
	public void setScoreTest() {
		team.setScore(10);
		assertEquals(team.getScore(), 10);
	}

	/**
	 * Method to test if the team has the correct colour.
	 */
	@Test
	public void colourTest() {
		assertTrue(team.getColour() == TeamEnum.RED);
		team.setColour(TeamEnum.BLUE);
		assertTrue(team.getColour() == TeamEnum.BLUE);
	}

	/**
	 * Method to test if the right members.
	 */
	@Test
	public void getMembersTest() {
		assertTrue(team.getMembers().isEmpty());
	}

}
