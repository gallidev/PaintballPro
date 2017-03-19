package gui;

import enums.GameLocation;
import enums.Menu;
import helpers.GUIManagerTestHelper;
import helpers.JavaFXTestHelper;
import javafx.scene.Scene;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for the Game Type menu
 */
public class TestGameTypeMenu {

    /**
     * Setup the JavaFX thread
     * @throws Exception test failed
     */
    @Before
    public void setUp() throws Exception {
        JavaFXTestHelper.setupApplication();
    }

    /**
     * Test for selecting Team Match in singleplayer mode
     * @throws Exception test failed
     */
    @Test
    public void selectEliminationSingle() throws Exception {
        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene s = GameTypeMenu.getScene(m, GameLocation.SingleplayerLocal);
        assertTrue(m.currentMenu == Menu.MainMenu);
        GUIManagerTestHelper.findButtonByTextInParent("Team Match", s.getRoot()).fire();
        Thread.sleep(1000);
        assertTrue(m.currentMenu == Menu.EliminationSingle);
    }

    /**
     * Test for selecting Team Match in multiplayer mode
     * @throws Exception test failed
     */
    @Test
    public void selectEliminationMulti() throws Exception {
        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene s = GameTypeMenu.getScene(m, GameLocation.MultiplayerServer);
        assertTrue(m.currentMenu == Menu.MainMenu);
        GUIManagerTestHelper.findButtonByTextInParent("Team Match", s.getRoot()).fire();
        assertTrue(m.currentMenu == Menu.Lobby);
    }

    /**
     * Test for selecting Capture the Flag in singleplayer mode
     * @throws Exception test failed
     */
    @Test
    public void selectCTFSingle() throws Exception {
        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene s = GameTypeMenu.getScene(m, GameLocation.SingleplayerLocal);
        assertTrue(m.currentMenu == Menu.MainMenu);
        GUIManagerTestHelper.findButtonByTextInParent("Capture The Flag", s.getRoot()).fire();
        Thread.sleep(4000);
        assertTrue(m.currentMenu == Menu.CTFSingle);
    }

    /**
     * Test for selecting Capture the Flag in multiplayer mode
     * @throws Exception test failed
     */
    @Test
    public void selectCTFMulti() throws Exception {
        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene s = GameTypeMenu.getScene(m, GameLocation.MultiplayerServer);
        assertTrue(m.currentMenu == Menu.MainMenu);
        GUIManagerTestHelper.findButtonByTextInParent("Capture The Flag", s.getRoot()).fire();
        assertTrue(m.currentMenu == Menu.Lobby);
    }

    /**
     * Test for selecting the back button
     * @throws Exception test failed
     */
    @Test
    public void selectBack() throws Exception {
        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene s = GameTypeMenu.getScene(m, GameLocation.MultiplayerServer);
        m.currentMenu = Menu.SingleplayerGameType;
        GUIManagerTestHelper.findButtonByTextInParent("Back", s.getRoot()).fire();
        assertTrue(m.currentMenu == Menu.MainMenu);
    }

}