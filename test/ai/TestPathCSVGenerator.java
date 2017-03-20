package ai;

import helpers.JavaFXTestHelper;
import javafx.application.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertTrue;

public class TestPathCSVGenerator {

    // Wrapper thread updates this if
    // the JavaFX application runs without a problem.
    // Declared volatile to ensure that writes are visible to every thread.
    private volatile boolean success = false;

    /**
     * Setup the test method
     * @throws Exception Test setup failed
     */
    @Before
    public void setUp() throws Exception {
        //JavaFXTestHelper.setupApplication();
    }

    /**
     * Tear down the test cases
     * @throws Exception Test teardown failed
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test that a JavaFX application launches.
     */
    @Test
    public void testMain() {
        // Wrapper thread.
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Application.launch(GeneratePaths.class);
                    success = true;
                } catch(Throwable t) {
                    if(t.getCause() != null && t.getCause().getClass().equals(InterruptedException.class)) {
                        success = true;
                        return;
                    }
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        try {
            Thread.sleep(1000000);  // Leave enough time for generator to finish before interrupting JavaFX application
        } catch(InterruptedException ex) {
            // We don't care if we wake up early.
        }
        thread.interrupt();
        try {
            thread.join(1); // Wait 1 second for our wrapper thread to finish.
        } catch(InterruptedException ex) {
            // We don't care if we wake up early.
        }
        assertTrue(success);
    }

}
