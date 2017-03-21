package physics;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
		map = Map.loadRaw("ctf");
		collisionsHandler = new CollisionsHandler(map);


		player1 = new UserPlayer(map.getSpawns()[0].x * 64, map.getSpawns()[0].y * 64, 1, map.getSpawns(), TeamEnum.RED, collisionsHandler, ImageFactory.getPlayerImage(TeamEnum.RED), enums.GameMode.ELIMINATION, ServerGameSimulation.GAME_HERTZ);

		player2 = new UserPlayer(map.getSpawns()[4].x * 64, map.getSpawns()[4].y, 2 * 64, map.getSpawns(), TeamEnum.BLUE, collisionsHandler, ImageFactory.getPlayerImage(TeamEnum.BLUE), enums.GameMode.ELIMINATION, ServerGameSimulation.GAME_HERTZ);

		red.addMember(player1);
		blue.addMember(player2);

		player1.setOppTeam(blue);
		player2.setOppTeam(red);

		game = new TeamMatchMode(red, blue);
		gameSimulation = new ServerGameSimulation(game);

		collisionsHandler.setBlueTeam(blue);
		collisionsHandler.setRedTeam(red);
	}

	@After
	public void tearDown()
	{
		//gameSimulation.stopGameLoop();
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

		gameSimulation.stopGameLoop();
	}

	@Test
	public void testHandleBulletCollisions()
	{

		gameSimulation.runGameLoop();

		player1.setAngle(1.5708);
		player2.setAngle(1.5708);

		player2.relocate(player1.getLayoutX()+80, player1.getLayoutY());

		player1.setShoot(true);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(player2.isEliminated());

		gameSimulation.stopGameLoop();
	}

	@Test
	public void testhandleFlagCollision()
	{

		gameSimulation.runGameLoop();
		//catch the flag first

		player1.relocate(collisionsHandler.getFlag().getLayoutX()+80, collisionsHandler.getFlag().getLayoutY());

		player1.setLeft(true);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(player1.hasFlag());

		//test when the player loses the flag and the other one catches it
		player1.relocate(collisionsHandler.getFlag().getLayoutX()+80, collisionsHandler.getFlag().getLayoutY());

		player1.setLeft(true);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(player1.hasFlag());

		player1.setLeft(false);

		player2.setAngle(-1.5708);

		player2.relocate(player1.getLayoutX()+90, player1.getLayoutY());

		player2.setShoot(true);
		player2.setLeft(true);

		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertFalse(player1.hasFlag());
		assertTrue(player2.hasFlag());

		//test when the player brings the flag back to his base
		player2.relocate(map.getSpawns()[4].x * 64, map.getSpawns()[4].y * 64);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertFalse(player2.hasFlag());
		gameSimulation.stopGameLoop();
	}

	@Test
	public void testHandlePowerUpCollision()
	{

		gameSimulation.runGameLoop();

		player1.setAngle(1.5708);

		player1.relocate(collisionsHandler.getShieldPowerup().getLayoutX()+80, collisionsHandler.getShieldPowerup().getLayoutY());

		player1.setLeft(true);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertFalse(collisionsHandler.getShieldPowerup().isVisible());

		player1.relocate(collisionsHandler.getSpeedPowerup().getLayoutX()-80, collisionsHandler.getSpeedPowerup().getLayoutY());

		player1.setLeft(false);
		player1.setRight(true);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertFalse(collisionsHandler.getSpeedPowerup().isVisible());

		gameSimulation.stopGameLoop();
	}


	@SuppressWarnings("deprecation")
	@Test
	public void testGetMeanAngle()
	{

		ArrayList<Double> angles = new ArrayList<>();

		angles.add( 90.0);
		angles.add( 180.0);
		angles.add( 220.0);

		Method method = null;
		try {
			method = CollisionsHandler.class.getDeclaredMethod("getMeanAngle", List.class);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		method.setAccessible(true);
		double result = 0;
		try {
			result = (double) method.invoke(collisionsHandler, angles);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(168.5, result, 0.1);


	}

}
