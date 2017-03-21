package physics;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import enums.TeamEnum;
import gui.GUIManager;
import helpers.JavaFXTestHelper;
import integrationServer.ServerGameSimulation;
import javafx.scene.shape.Rectangle;
import logic.GameMode;
import players.ClientPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;
import rendering.Renderer;
import serverLogic.Team;
import serverLogic.TeamMatchMode;

public class TestCollisionsHandler {

	private CollisionsHandler collisionsHandler;
	private Map map;
	private UserPlayer player1;
	private UserPlayer player2;

	private Team red;
	private Team blue;
	private GameMode game;
	private ServerGameSimulation gameSimulation;

	@Before
	public void setUp()
	{

		red = new Team(TeamEnum.RED);
		blue = new Team(TeamEnum.BLUE);

		JavaFXTestHelper.setupApplication();
		map = Map.loadRaw("elimination");
		collisionsHandler = new CollisionsHandler(map);


		player1 = new UserPlayer(map.getSpawns()[0].x * 64, map.getSpawns()[0].y * 64, 1, map.getSpawns(), TeamEnum.RED, collisionsHandler, ImageFactory.getPlayerImage(TeamEnum.RED), enums.GameMode.ELIMINATION, ServerGameSimulation.GAME_HERTZ);

		player2 = new UserPlayer(map.getSpawns()[4].x * 64, map.getSpawns()[4].y, 2 * 64, map.getSpawns(), TeamEnum.BLUE, collisionsHandler, ImageFactory.getPlayerImage(TeamEnum.BLUE), enums.GameMode.ELIMINATION, ServerGameSimulation.GAME_HERTZ);

		red.addMember(player1);
		red.addMember(player2);

		game = new TeamMatchMode(red, blue);
		gameSimulation = new ServerGameSimulation(game);

		collisionsHandler.setBlueTeam(blue);
		collisionsHandler.setRedTeam(red);
	}

	@After
	public void tearDown()
	{

		gameSimulation.stopGameLoop();
	}

	@Test
	public void testPropWallCollisions()
	{
		gameSimulation.runGameLoop();

		player1.setAngle(1.5708);
		player1.setShoot(true);
		player1.setLeft(true);
		player1.setUp(true);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertFalse(player1.getBullets().isEmpty());
		assertFalse(player1.getCollDown());
		assertTrue(player1.getCollUp());
		assertTrue(player1.getCollLeft());
		assertFalse(player1.getCollRight());


		player1.setShoot(false);
		player1.setLeft(false);
		player1.setUp(false);
		player1.setRight(true);
		player1.setDown(true);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertTrue(player1.getBullets().isEmpty());
		assertTrue(player1.getCollDown());
		assertFalse(player1.getCollUp());
		assertFalse(player1.getCollLeft());
		assertTrue(player1.getCollRight());

	}

	@Test
	public void testHandleBulletCollisions()
	{


	}

}
