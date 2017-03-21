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

public class CollisionsHandlerTest {

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


		player1 = new UserPlayer(map.getSpawns()[1].x, map.getSpawns()[1].y, 1, map.getSpawns(), TeamEnum.RED, collisionsHandler, ImageFactory.getPlayerImage(TeamEnum.RED), enums.GameMode.ELIMINATION, ServerGameSimulation.GAME_HERTZ);

		player2 = new UserPlayer(map.getSpawns()[5].x, map.getSpawns()[5].y, 2, map.getSpawns(), TeamEnum.BLUE, collisionsHandler, ImageFactory.getPlayerImage(TeamEnum.BLUE), enums.GameMode.ELIMINATION, ServerGameSimulation.GAME_HERTZ);

		red.addMember(player1);
		red.addMember(player2);

		game = new TeamMatchMode(red, blue);
		gameSimulation = new ServerGameSimulation(game);
	}

	@After
	public void tearDown()
	{

	}

	@Test
	public void testPropWallCollisions()
	{
		collisionsHandler.handlePropWallCollision(player1);

		assertFalse(player1.getCollDown());
		assertFalse(player1.getCollUp());
		assertFalse(player1.getCollLeft());
		assertFalse(player1.getCollRight());



	}

}
