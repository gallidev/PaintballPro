package testSuites;

import ai.TestPathCSVGenerator;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ai.TestPathHashMapGenerators;
import ai.TestPathFinding;
import ai.TestPrimitives;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        TestPathHashMapGenerators.class,
        TestPathFinding.class,
        TestPrimitives.class,
        TestPathCSVGenerator.class
})

/**
 * AI Test Suite.
 *
 * @author Sivarjuen Ravichandran
 */
public class AISuite {
}