package testSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import rendering.TestMap;
import rendering.TestRendererMultiplayer;
import rendering.TestRendererSingleplayer;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        TestMap.class,
        TestRendererSingleplayer.class,
		TestRendererMultiplayer.class
})

/**
 * AI Test Suite.
 *
 * @author Artur Komoter
 */
public class RenderingSuite {
}