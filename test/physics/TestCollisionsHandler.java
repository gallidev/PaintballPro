package physics;

import enums.TeamEnum;
import helpers.JavaFXTestHelper;
import integration.server.GameUpdateListener;
import integration.server.ServerGameSimulation;
import logic.GameMode;
import logic.server.Team;
import logic.server.TeamMatchMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import players.EssentialPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestCollisionsHandler {

	private CollisionsHandler collisionsHandler;
	private Map map;
	private UserPlayer player1;
	private UserPlayer player2;
	private UserPlayer player3;
	private UserPlayer player4;

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
		map = Map.loadRaw("castle");
		collisionsHandler = new CollisionsHandler(map);

		ArrayList<EssentialPlayer> players = new ArrayList<>();

		player1 = new UserPlayer(map.getSpawns()[0].x * 64, map.getSpawns()[0].y * 64, 1, map.getSpawns(), TeamEnum.RED, collisionsHandler, ImageFactory.getPlayerImage(TeamEnum.RED), enums.GameMode.CAPTURE_THE_FLAG, ServerGameSimulation.GAME_HERTZ);

		player2 = new UserPlayer(map.getSpawns()[5].x * 64, map.getSpawns()[5].y * 64, 2, map.getSpawns(), TeamEnum.BLUE, collisionsHandler, ImageFactory.getPlayerImage(TeamEnum.BLUE), enums.GameMode.CAPTURE_THE_FLAG, ServerGameSimulation.GAME_HERTZ);


		player3 = new UserPlayer(map.getSpawns()[0].x * 64, map.getSpawns()[0].y * 64, 3, map.getSpawns(), TeamEnum.RED, collisionsHandler, ImageFactory.getPlayerImage(TeamEnum.RED), enums.GameMode.CAPTURE_THE_FLAG, ServerGameSimulation.GAME_HERTZ);

		player4 = new UserPlayer(map.getSpawns()[5].x * 64, map.getSpawns()[5].y * 64, 4, map.getSpawns(), TeamEnum.BLUE, collisionsHandler, ImageFactory.getPlayerImage(TeamEnum.BLUE), enums.GameMode.CAPTURE_THE_FLAG, ServerGameSimulation.GAME_HERTZ);

		red.addMember(player1);
		blue.addMember(player2);

		player1.setOppTeam(blue);
		player2.setOppTeam(red);

		game = new TeamMatchMode(red, blue);
		gameSimulation = new ServerGameSimulation(game);

		collisionsHandler.setBlueTeam(blue);
		collisionsHandler.setRedTeam(red);

		players.add(player3);
		players.add(player4);

		collisionsHandler.setPlayers(players);

		GameUpdateListener listener = new GameUpdateListener(){

			@Override
			public void onFlagCaptured(int player) {
			}

			@Override
			public void onFlagDropped(int player) {
			}

			@Override
			public void onFlagRespawned(int player) {
			}

			@Override
			public void onPowerupAction(PowerupType type, int player) {
			}

			@Override
			public void onPowerupRespawn(PowerupType type, int location) {

			}

			@Override
			public void onShotBullet(int playerId, int bullet, double x, double y, double angle) {

			}

			@Override
			public void onBulletKills(int playerId, int bulletId) {
				// TODO Auto-generated method stub

			}

		};
		collisionsHandler.setListener(listener);

		map.getPowerups()[0].setListener(listener);
		map.getPowerups()[1].setListener(listener);

	}

	@After
	public void tearDown()
	{
		collisionsHandler = null;
		map = null;
		player1 = null;
		player2 = null;
		player3 = null;
		player4 = null;

		red = null;
		blue = null;
		game = null;
		gameSimulation = null;
	}

	@Test
	public void testPropWallCollisions()
	{
		gameSimulation.runGameLoop();

		player2.setAngle(1);
		player1.setAngle(1);
		player1.setShoot(true);
		player1.setLeft(true);
		player1.setUp(false);
		player1.setRight(false);
		player1.setDown(false);

		//System.out.println("position :" + player2.getLayoutX() + " " + player2.getLayoutY());

		player2.setLeft(false);
		player2.setUp(true);
		player2.setRight(false);
		player2.setDown(false);


		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertFalse(player1.getBullets().isEmpty());
		assertFalse(player1.getCollDown());
		assertFalse(player1.getCollUp());
		assertTrue(player1.getCollLeft());
		assertFalse(player1.getCollRight());

		//System.out.println("position :" + player2.getLayoutX() + " " + player2.getLayoutY());
		assertTrue(player2.getCollUp());


		player1.setShoot(false);
		player1.setLeft(false);
		player1.setUp(false);
		player1.setRight(false);
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
		assertFalse(player1.getCollRight());


		player1.setLeft(false);
		player1.setUp(false);
		player1.setRight(true);
		player1.setDown(false);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertFalse(player1.getCollDown());
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
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(player2.isEliminated());


		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertFalse(player2.isEliminated());
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
	public void testhandleFlagCollisionInverse()
	{

		gameSimulation.runGameLoop();
		//catch the flag first

		player2.relocate(collisionsHandler.getFlag().getLayoutX()+80, collisionsHandler.getFlag().getLayoutY());

		player2.setLeft(true);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(player2.hasFlag());

		//test when the player loses the flag and the other one catches it
		player2.relocate(collisionsHandler.getFlag().getLayoutX()+80, collisionsHandler.getFlag().getLayoutY());

		player2.setLeft(true);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(player2.hasFlag());

		player2.setLeft(false);

		player1.setAngle(-1.5708);

		player1.relocate(player2.getLayoutX()+90, player2.getLayoutY());

		player1.setShoot(true);
		player1.setLeft(true);

		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertFalse(player2.hasFlag());
		assertTrue(player1.hasFlag());

		//test when the player brings the flag back to his base
		player1.relocate(map.getSpawns()[0].x * 64, map.getSpawns()[0].y * 64);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertFalse(player1.hasFlag());
		gameSimulation.stopGameLoop();
	}

	@Test
	public void testHandlePowerUpCollision()
	{

		gameSimulation.runGameLoop();

		player1.setAngle(1.5708);

		player1.relocate(collisionsHandler.getShieldPowerup().getLayoutX(), collisionsHandler.getShieldPowerup().getLayoutY());

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertFalse(collisionsHandler.getShieldPowerup().isVisible());

		player1.setAngle(1.5708);
		player2.setAngle(1.5708);

		player1.relocate(player2.getLayoutX()+80, player2.getLayoutY());

		player2.setShoot(true);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(player1.isEliminated());


		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		player1.relocate(collisionsHandler.getSpeedPowerup().getLayoutX(), collisionsHandler.getSpeedPowerup().getLayoutY());

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertFalse(collisionsHandler.getSpeedPowerup().isVisible());

		gameSimulation.stopGameLoop();
	}


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
