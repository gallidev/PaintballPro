package gui;

import enums.Menu;
import helpers.GUIManagerTestHelper;
import helpers.JavaFXTestHelper;
import javafx.application.Platform;
import javafx.scene.Scene;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the Help Menu
 */
public class TestHelpMenu {

    /**
     * Create the JavaFX Application Thread
     * @throws Exception test failed
     */
    @Before
    public void setUp() throws Exception {
        JavaFXTestHelper.setupApplication();
    }

    /**
     * Test for the scene being correctly created, and for the back button
     * @throws Exception
     */
    @Test
    public void getScene() throws Exception {
        GUIManagerTestHelper g = new GUIManagerTestHelper();
        Platform.runLater(() -> {

            Scene s = HelpMenu.getScene(g);

            GUIManagerTestHelper.findButtonByTextInParent("Back", s.getRoot()).fire();

        });
        Thread.sleep(1000);
        assertTrue(g.currentMenu == Menu.MainMenu);


    }

}