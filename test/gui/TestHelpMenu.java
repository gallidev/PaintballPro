package gui;

import enums.Menu;
import helpers.GUIManagerTestHelper;
import helpers.JavaFXTestHelper;
import javafx.application.Platform;
import javafx.scene.Scene;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the Help Menu
 *
 * @author Jack Hughes
 */
public class TestHelpMenu {

    GUIManagerTestHelper guiManager = new GUIManagerTestHelper();
    Scene scene;

    /**
     * Create the JavaFX Application Thread
     * @throws Exception test failed
     */
    @Before
    public void setUp() throws Exception {
        JavaFXTestHelper.setupApplication();
        JavaFXTestHelper.waitForPlatform();
        guiManager.currentMenu = Menu.Help;
    }

    /**
     * Test for the scene being correctly created, and for the back button
     * @throws Exception
     */
    @Test
    public void getScene() throws Exception {
        scene = HelpMenu.getScene(guiManager);
        GUIManagerTestHelper.findButtonByTextInParent("Back", scene.getRoot()).fire();

        Thread.sleep(1000);

        assertTrue(guiManager.currentMenu == Menu.MainMenu);
    }

    /**
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        guiManager = null;
        scene = null;
    }
}