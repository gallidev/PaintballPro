package ai;

import enums.TeamEnum;
import helpers.JavaFXTestHelper;
import integration.server.ServerGameSimulation;
import javafx.geometry.Point2D;
import logic.GameMode;
import logic.server.CaptureTheFlagMode;
import logic.server.Team;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import physics.CollisionsHandler;
import players.AIPlayer;
import rendering.Map;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class TestAIManager {

    private Map map;
    private AIManager aiManagerRed;
    private AIManager aiManagerBlue;

    private Team red;
    private Team blue;

    @Before
    public void setUp()
    {
        JavaFXTestHelper.setupApplication();
        map = Map.loadRaw("elimination");
        CollisionsHandler ch = new CollisionsHandler(map);
        HashMapGen hashMaps = new HashMapGen(map);

        red = new Team(TeamEnum.RED);
        blue = new Team(TeamEnum.BLUE);

        aiManagerRed = new AIManager(red, map, ch, 0, hashMaps);
        aiManagerBlue = new AIManager(blue, map, ch, 0, hashMaps);
    }

    @After
    public void tearDown()
    {
        map = null;
        red = null;
        blue = null;
        aiManagerRed = null;
        aiManagerBlue = null;
    }

    @Test
    public void testAITeamSize()
    {
        assertTrue(aiManagerRed.getTeam().getMembersNo() == 0);
        aiManagerRed.createPlayers();
        assertTrue(aiManagerRed.getTeam().getMembersNo() == 4);

        assertTrue(aiManagerBlue.getTeam().getMembersNo() == 0);
        aiManagerBlue.createPlayers();
        assertTrue(aiManagerBlue.getTeam().getMembersNo() == 4);
    }

}
