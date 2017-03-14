package ai;

import helpers.JavaFXTestHelper;
import javafx.geometry.Point2D;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rendering.Map;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

/**
 * Tests AI Path and Cover Generators
 */
public class TestPathAndCoverGenerators {

    HashMapGen elimPathGen;
    HashMapGen ctfPathGen;

    /**
     * Setup the test method
     * @throws Exception Test setup failed
     */
    @Before
    public void setUp() throws Exception {
        JavaFXTestHelper.setupApplication();

        Map elimMap = Map.loadRaw("elimination");
        elimPathGen = new HashMapGen(elimMap);

        Map ctfMap = Map.loadRaw("ctf");
        ctfPathGen = new HashMapGen(ctfMap);

    }

    /**
     * Tear down the test cases
     * @throws Exception Test teardown failed
     */
    @After
    public void tearDown() throws Exception {
        elimPathGen = null;
        ctfPathGen = null;
    }

    @Test
    public void elimPathsGenerated() {
        HashMap<PointPairs, ArrayList<Point2D>> pathMap = elimPathGen.getPathMap();
        assertTrue(!pathMap.isEmpty());
    }

    @Test
    public void ctfPathsGenerated() {
        HashMap<PointPairs, ArrayList<Point2D>> pathMap = ctfPathGen.getPathMap();
        assertTrue(!pathMap.isEmpty());
    }

    @Test
    public void elimCoverMapsGenerated() {
        HashMap<PointPairs, Boolean> coverMap = elimPathGen.getCoverMap();
        assertTrue(!coverMap.isEmpty());
    }

    @Test
    public void ctfCoverMapsGenerated() {
        HashMap<PointPairs, Boolean> coverMap = ctfPathGen.getCoverMap();
        assertTrue(!coverMap.isEmpty());
    }





}
