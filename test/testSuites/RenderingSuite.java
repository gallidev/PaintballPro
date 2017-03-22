package testSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import rendering.MapTest;
import rendering.RendererTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        MapTest.class,
        RendererTest.class
})

/**
 * AI Test Suite.
 *
 * @author Artur Komoter
 */
public class RenderingSuite {
}