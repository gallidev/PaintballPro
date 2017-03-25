package ai;

import enums.TeamEnum;
import helpers.JavaFXTestHelper;
import integration.server.ServerGameSimulation;
import javafx.geometry.Point2D;
import logic.GameMode;
import logic.server.Team;
import logic.server.TeamMatchMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import physics.CollisionsHandler;
import players.AIPlayer;
import rendering.Map;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class TestEliminationBehaviour {

    private Map map;
    private AIPlayer player1;
    private AIPlayer player2;
    private BehaviourManager bManager;
    private Mover mover;

    private Team red;
    private Team blue;
    private GameMode game;
    private ServerGameSimulation gameSimulation;

    @Before
    public void setUp()
    {
        JavaFXTestHelper.setupApplication();
        map = Map.loadRaw("desert");
        CollisionsHandler ch = new CollisionsHandler(map);
        HashMapGen hashMaps = new HashMapGen(map);

        red = new Team(TeamEnum.RED);
        blue = new Team(TeamEnum.BLUE);

        player1 = new AIPlayer(map.getSpawns()[1].x * 64, map.getSpawns()[1].y * 64, 1, map, TeamEnum.RED, ch, hashMaps, map.getGameMode(), 60);
        red.addMember(player1);

        player2 = new AIPlayer(map.getSpawns()[5].x * 64, map.getSpawns()[5].y * 64, 5, map, TeamEnum.BLUE, ch, hashMaps, map.getGameMode(), 60);
        blue.addMember(player2);

        player1.setOppTeam(blue);
        player2.setOppTeam(red);

        bManager = player1.getBehaviourManager();
        mover = bManager.getMover();

        game = new TeamMatchMode(red, blue);
        gameSimulation = new ServerGameSimulation(game);
    }

    @After
    public void tearDown()
    {
        map = null;
        red = null;
        blue = null;
        player1 = null;
        player2 = null;
        game = null;
        gameSimulation.stopGameLoop();
        gameSimulation = null;
        bManager = null;
        mover = null;
    }

    @Test
    public void testClosestEnemy()
    {
        gameSimulation.runGameLoop();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(bManager.getClosestEnemy() == player2);

        gameSimulation.stopGameLoop();
    }

    @Test
    public void testPath()
    {
        gameSimulation.runGameLoop();

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Point2D> path = mover.getPath();
        assertTrue(path.get(0) == mover.getTarget());
        assertTrue(path.get(path.size()-1).distance(new Point2D(Math.floor(player2.getLayoutX()/64), Math.floor(player2.getLayoutY()/64))) < 2);

        gameSimulation.stopGameLoop();
    }
}
