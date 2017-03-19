package gui;

import helpers.JavaFXTestHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for the Server's GUI
 */
public class TestServerGUI {

    @Before
    public void setUp() throws Exception {
        JavaFXTestHelper.setupApplication();
    }

    @Test
    public void addMessage() throws Exception {
        ServerGUI sGUI = new ServerGUI();

        Thread.sleep(2000);

        sGUI.addMessage("Testing");
        sGUI.addMessage("Testing 2");
    }

}