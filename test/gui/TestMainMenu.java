package gui;

import enums.Menu;
import helpers.GUIManagerTestHelper;
import helpers.JavaFXTestHelper;
import javafx.scene.Scene;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for the main menu
 *
 * @author Jack Hughes
 */
public class TestMainMenu {

    /**
     * Create the JavaFX Application Thread
     * @throws Exception test failed
     */
    @Before
    public void setUp() throws Exception {
        JavaFXTestHelper.setupApplication();
        JavaFXTestHelper.waitForPlatform();
    }

    /**
     * Test for selecting singleplayer
     * @throws Exception test failed
     */
    @Test
    public void singleButton() throws Exception {
        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene mainMenu = MainMenu.getScene(m);

        (GUIManagerTestHelper.findButtonByTextInParent("Single Player", mainMenu.getRoot())).fire();

        assertTrue(m.currentMenu == Menu.SINGLEPLAYER_GAME_TYPE);
    }

    /**
     * Test for selecting multiplayer
     * @throws Exception test failed
     */
    @Test
    public void multiButton() throws Exception {
        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene mainMenu = MainMenu.getScene(m);

        (GUIManagerTestHelper.findButtonByTextInParent("Multiplayer", mainMenu.getRoot())).fire();

        assertTrue(m.currentMenu == Menu.NICKNAME_SERVER_CONNECTION);
    }

    /**
     * Test for selecting settings
     * @throws Exception test failed
     */
    @Test
    public void settingsButton() throws Exception {
        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene mainMenu = MainMenu.getScene(m);

        (GUIManagerTestHelper.findButtonByTextInParent("Settings", mainMenu.getRoot())).fire();

        assertTrue(m.currentMenu == Menu.SETTINGS);
    }

    /**
     * Test for selecting the help menu
     * @throws Exception test failed
     */
    @Test
    public void helpButton() throws Exception {
        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene mainMenu = MainMenu.getScene(m);

        (GUIManagerTestHelper.findButtonByTextInParent("Help", mainMenu.getRoot())).fire();

        assertTrue(m.currentMenu == Menu.HELP);
    }

}