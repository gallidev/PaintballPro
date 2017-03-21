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
 * Testing Node, PropNode and Path methods
 */
public class TestPrimitives {

    PropNode pn;
    Node n;
    Path p;

    /**
     * Setup the test method
     * @throws Exception Test setup failed
     */
    @Before
    public void setUp() throws Exception {
        pn = new PropNode(4,8);
        n = new Node(9,3);
        p = new Path();

    }

    /**
     * Tear down the test cases
     * @throws Exception Test teardown failed
     */
    @After
    public void tearDown() throws Exception {
        pn = null;
        n = null;
        p = null;
    }

    @Test
    public void PropNodeTest() {
        assertTrue(pn.x == 4 && pn.y == 8);
        pn.x = 9;
        pn.y = 10;
        assertTrue(pn.x == 9 && pn.y == 10);
    }

    @Test
    public void NodeCoordinateTest() {
        assertTrue(n.x == 9 && n.y == 3);
    }

    @Test
    public void NodeHeuristicsTest() {
        assertTrue(n.heuristicCost == 0 && n.finalCost == 9999999);
        n.heuristicCost = 5;
        n.finalCost = 100;
        assertTrue(n.heuristicCost == 5 && n.finalCost == 100);
        n.reset();
        assertTrue(n.heuristicCost == 0 && n.finalCost == 9999999);
    }

    @Test
    public void NodeStringTest() {
        String str = "(9, 3)";
        assertTrue(n.toString().equals(str));
    }

    @Test
    public void NodeEqualityTest(){
        Node newNode = new Node(9, 3);
        assertTrue(n.equals(newNode));

        Node anotherNode = new Node(10,5);
        assertTrue(!n.equals(anotherNode));

        assertTrue(!n.equals(pn));
    }

    @Test
    public void NodeHashTest(){
        assertTrue(n.hashCode() == 27);
    }

    @Test
    public void EmptyPathTest() {
        assertTrue(p.getLength() == 0);
        assertTrue(p.toString().equals("[]"));
        assertTrue(!p.contains(n));
    }

    @Test
    public void NonEmptyPathTest(){
        p.appendNode(n);
        assertTrue(p.getNode(0).equals(n));
        assertTrue(p.getX(0) == 9 && p.getY(0) == 3);
        assertTrue(p.getLength() == 1);
        p.removeFirst();
        assertTrue(p.getLength() == 0);
        p.prependNode(n);
        assertTrue(p.hashCode() == 27);
        p.clearPath();
        assertTrue(p.hashCode() == 0);
    }

    @Test
    public void PathEqualityTest(){
        assertTrue(!p.equals(n));
        p.appendNode(n);
        Path newPath = new Path();
        newPath.appendNode(n);
        assertTrue(p.equals(newPath));
    }

    @Test
    public void PointPairEqualityTest(){
        PointPairs pp1 = new PointPairs(1,1,5,5);
        PointPairs pp2 = new PointPairs(18,4,9,5);
        PointPairs pp3 = new PointPairs(1,1,5,5);
        Node n1 = new Node(1,1);
        assertTrue(!pp1.equals(pp2));
        assertTrue(pp1.equals(pp3));
        assertTrue(!pp1.equals(n1));
    }

    @Test
    public void ComparatorTest(){
        CostComparator cc = new CostComparator();

        Node n1 = new Node(1,4);
        n1.finalCost = 100;

        Node n2 = new Node(5,8);
        n2.finalCost = 0;

        Node n3 = new Node(6,3);
        n3.finalCost = 100;

        assertTrue(cc.compare(n1,n2) == 1);
        assertTrue(cc.compare(n2, n3) == -1);
        assertTrue(cc.compare(n1,n3) == 0);
    }





}
