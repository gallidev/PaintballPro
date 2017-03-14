package ai;

import helpers.JavaFXTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rendering.Map;

import static org.junit.Assert.assertTrue;

/**
 * Tests AI Pathfinding
 */
public class TestPathFinding {

    Pathfinding elimPathfinder;
    Pathfinding ctfPathfinder;

    Path elimPath1;
    Path elimPath2;

    Path ctfPath1;
    Path ctfPath2;

    /**
     * Setup the test method
     * @throws Exception Test setup failed
     */
    @Before
    public void setUp() throws Exception {
        JavaFXTestHelper.setupApplication();
        Map elimMap = Map.loadRaw("elimination") ;
        elimPathfinder = new Pathfinding(elimMap);

        Map ctfMap = Map.loadRaw("ctf") ;
        ctfPathfinder = new Pathfinding(ctfMap);

        //Elimination paths

        //Path from (1,6) to (3,6)
        elimPath1 = new Path();
        elimPath1.appendNode(new Node(2,6));
        elimPath1.appendNode(new Node(3,6));

        //Path from (22,10) to (11,9)
        elimPath2 = new Path();
        elimPath2.appendNode(new Node(21, 9));
        elimPath2.appendNode(new Node(20, 9));
        elimPath2.appendNode(new Node(19, 9));
        elimPath2.appendNode(new Node(18, 9));
        elimPath2.appendNode(new Node(17, 9));
        elimPath2.appendNode(new Node(16, 9));
        elimPath2.appendNode(new Node(15, 9));
        elimPath2.appendNode(new Node(14, 9));
        elimPath2.appendNode(new Node(13, 9));
        elimPath2.appendNode(new Node(12, 9));
        elimPath2.appendNode(new Node(11, 9));

        //Capture the flag paths

        //Path from (1,6) to (4,6)
        ctfPath1 = new Path();
        ctfPath1.appendNode(new Node(2,6));
        ctfPath1.appendNode(new Node(3,6));
        ctfPath1.appendNode(new Node(4,6));

        //Path from (1,6) to (10,3)
        ctfPath2 = new Path();
        ctfPath2.appendNode(new Node(2,5));
        ctfPath2.appendNode(new Node(3,4));
        ctfPath2.appendNode(new Node(4,3));
        ctfPath2.appendNode(new Node(5,3));
        ctfPath2.appendNode(new Node(6,3));
        ctfPath2.appendNode(new Node(7,3));
        ctfPath2.appendNode(new Node(8,3));
        ctfPath2.appendNode(new Node(9,3));
        ctfPath2.appendNode(new Node(10,3));


    }

    /**
     * Tear down the test cases
     * @throws Exception Test teardown failed
     */
    @After
    public void tearDown() throws Exception {
        elimPath1 = null;
        elimPath2 = null;
        ctfPath1 = null;
        ctfPath2 = null;
    }

    @Test
    public void elimPath1Test() {
        Path newPath = elimPathfinder.getPath(1,6,3,6);
        assertTrue(newPath.equals(elimPath1));
    }

    @Test
    public void elimPath2Test() {
        Path newPath = elimPathfinder.getPath(22,10,11,9);
        assertTrue(newPath.equals(elimPath2));
    }

    @Test
    public void ctfPath1Test() {
        Path newPath = ctfPathfinder.getPath(1,6,4,6);
        assertTrue(newPath.equals(ctfPath1));
    }

    @Test
    public void ctfPath2Test() {
        Path newPath = ctfPathfinder.getPath(1,6,10,3);
        assertTrue(newPath.equals(ctfPath2));
    }


}
