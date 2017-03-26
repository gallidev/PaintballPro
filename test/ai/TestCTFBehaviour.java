package ai;

import enums.TeamEnum;
import helpers.JavaFXTestHelper;
import javafx.geometry.Point2D;
import logic.GameMode;
import logic.server.CaptureTheFlagMode;
import logic.server.Team;
import networking.game.ServerGameSimulation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import physics.CollisionsHandler;
import players.AIPlayer;
import rendering.Map;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class TestCTFBehaviour {

    private Map map;
    private AIPlayer player1;
    //private AIPlayer player2;
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
        map = Map.loadRaw("castle");
        CollisionsHandler ch = new CollisionsHandler(map);
        HashMapGen hashMaps = new HashMapGen(map);

        red = new Team(TeamEnum.RED);
        blue = new Team(TeamEnum.BLUE);

        player1 = new AIPlayer(map.getSpawns()[1].x * 64, map.getSpawns()[1].y * 64, 1, map, TeamEnum.RED, ch, hashMaps, map.getGameMode(), 60);
        red.addMember(player1);

        //player2 = new AIPlayer(map.getSpawns()[5].x * 64, map.getSpawns()[5].y * 64, 5, map, TeamEnum.BLUE, ch, hashMaps, map.getGameMode(), 60);
        //blue.addMember(player2);

        player1.setOppTeam(blue);
        //player2.setOppTeam(red);

        bManager = player1.getBehaviourManager();
        mover = bManager.getMover();

        game = new CaptureTheFlagMode(red, blue);
        gameSimulation = new ServerGameSimulation(game);
    }

    @After
    public void tearDown()
    {
        map = null;
        red = null;
        blue = null;
        player1 = null;
        game = null;
        gameSimulation.stopGameLoop();
        gameSimulation = null;
        bManager = null;
        mover = null;
    }

    @Test
    public void testFlagTargeting()
    {
        gameSimulation.runGameLoop();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 7; i++){
            player1.setLayoutX(map.getSpawns()[1].x * 64);
            player1.setLayoutY(map.getSpawns()[1].y * 64);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ArrayList<Point2D> path = mover.getPath();
        Point2D p1 = (path.get(path.size() - 1));
        Point2D p2 = (new Point2D(Math.floor(map.getFlag().getLayoutX() / 64), Math.floor(map.getFlag().getLayoutY() / 64)));
        assertTrue(p1.equals(p2));

        gameSimulation.stopGameLoop();
    }

    @Test
    public void testRetreating()
    {
        gameSimulation.runGameLoop();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        player1.setLayoutX(map.getFlag().getLayoutX());
        player1.setLayoutY(map.getFlag().getLayoutY());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Point2D> path = mover.getPath();
        assertTrue(path.get(path.size() - 1).distance(new Point2D(map.getSpawns()[1].x, map.getSpawns()[1].y)) < 5);


        gameSimulation.stopGameLoop();
    }
}
