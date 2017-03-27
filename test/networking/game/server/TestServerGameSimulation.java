package networking.game.server;

import enums.TeamEnum;
import helpers.JavaFXTestHelper;
import logic.GameMode;
import logic.server.Team;
import logic.server.TeamMatchMode;
import networking.game.ServerGameSimulation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import physics.CollisionsHandler;
import players.EssentialPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Test class to test the game simulation running on the server in multiplayer.
 * Class tested - {@link ServerGameSimulation}
 * 
 * @author Alexandra Paduraru
 *
 */
public class TestServerGameSimulation {

	private Team blue;
	private GameMode game;
	private Team red;
	private ServerGameSimulation gameSimulation;

	@Before
	public void setUp() throws Exception {
		red = new Team(TeamEnum.RED);
		blue = new Team(TeamEnum.BLUE);

		JavaFXTestHelper.setupApplication();
		Map map;
		map = Map.loadRaw("desert");
		EssentialPlayer p = new UserPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, new CollisionsHandler(map),
				ImageFactory.getPlayerFlagImage(TeamEnum.RED), enums.GameMode.TEAM_MATCH,
				ServerGameSimulation.GAME_HERTZ);

		red.addMember(p);

		game = new TeamMatchMode(red, blue);
		gameSimulation = new ServerGameSimulation(game);
	}
	
	@After
	public void tearDown() throws Exception {
		gameSimulation = null;
	}

	/**
	 * Method to test that the simulation ticks the player continuously during
	 * the game.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void startExecutionTest() throws InterruptedException {
		gameSimulation.runGameLoop();
		Thread.sleep(1000);
		assertTrue(game.getRemainingTime() <= 17900);

		ArrayList<EssentialPlayer> players = new ArrayList<>();
		players.addAll(red.getMembers());
		players.addAll(blue.getMembers());

		for (EssentialPlayer p : players){
			assertTrue(UserPlayer.isTicked);
		}

		gameSimulation.stopGameLoop();
	}

	/**
	 * Method to test that the correct game mode is being played.
	 */
	@Test
	public void getGameTest() {
		assertTrue(gameSimulation.getGame() instanceof TeamMatchMode);
	}

}
