package testSuites;

import ai.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        TestPathHashMapGenerators.class,
        TestPathFinding.class,
        TestPrimitives.class,
        TestPathCSVGenerator.class,
        TestAIManager.class,
        TestCTFBehaviour.class,
        TestEliminationBehaviour.class
})

/**
 * AI Test Suite.
 *
 * @author Sivarjuen Ravichandran
 */
public class AISuite {
}