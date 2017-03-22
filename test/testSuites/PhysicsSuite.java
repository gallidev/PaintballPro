package testSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import physics.TestCollisionsHandler;
import physics.TestInputHandler;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        TestCollisionsHandler.class,
        TestInputHandler.class
})

/**
 * AI Test Suite.
 *
 * @author Fillipo Galli
 */
public class PhysicsSuite {
}