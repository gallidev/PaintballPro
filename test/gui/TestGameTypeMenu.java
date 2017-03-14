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
 * Created by jack on 02/03/2017.
 */
public class TestGameTypeMenu {

    @Before
    public void setUp() throws Exception {
        JavaFXTestHelper.setupApplication();
    }

    @Test
    public void selectEliminationSingle() throws Exception {
        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene s = GameTypeMenu.getScene(m, GameLocation.SingleplayerLocal);
        assertTrue(m.currentMenu == Menu.MainMenu);
        GUIManagerTestHelper.findButtonByTextInParent("Elimination", s.getRoot()).fire();
        assertTrue(m.currentMenu == Menu.EliminationSingle);
    }

    @Test
    public void selectEliminationMulti() throws Exception {
        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene s = GameTypeMenu.getScene(m, GameLocation.MultiplayerServer);
        assertTrue(m.currentMenu == Menu.MainMenu);
        GUIManagerTestHelper.findButtonByTextInParent("Elimination", s.getRoot()).fire();
        assertTrue(m.currentMenu == Menu.Lobby);
    }

    @Test
    public void selectCTFSingle() throws Exception {
        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene s = GameTypeMenu.getScene(m, GameLocation.SingleplayerLocal);
        assertTrue(m.currentMenu == Menu.MainMenu);
        GUIManagerTestHelper.findButtonByTextInParent("Capture The Flag", s.getRoot()).fire();
        assertTrue(m.currentMenu == Menu.CTFSingle);
    }

    @Test
    public void selectCTFMulti() throws Exception {
        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene s = GameTypeMenu.getScene(m, GameLocation.MultiplayerServer);
        assertTrue(m.currentMenu == Menu.MainMenu);
        GUIManagerTestHelper.findButtonByTextInParent("Capture The Flag", s.getRoot()).fire();
        assertTrue(m.currentMenu == Menu.Lobby);
    }

    @Test
    public void selectBack() throws Exception {
        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene s = GameTypeMenu.getScene(m, GameLocation.MultiplayerServer);
        m.currentMenu = Menu.SingleplayerGameType;
        GUIManagerTestHelper.findButtonByTextInParent("Back", s.getRoot()).fire();
        assertTrue(m.currentMenu == Menu.MainMenu);
    }

}