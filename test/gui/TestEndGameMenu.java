package gui;

import enums.Menu;
import enums.TeamEnum;
import helpers.GUIManagerTestHelper;
import helpers.JavaFXTestHelper;
import javafx.scene.Scene;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for the end game menu
 *
 * @author Jack Hughes
 */
public class TestEndGameMenu {

    /**
     * Test the end game menu
     * @throws Exception test failed
     */
    @Test
    public void getScene() throws Exception {
        JavaFXTestHelper.setupApplication();

        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene s = EndGameMenu.getScene(m, "1,0", TeamEnum.BLUE);
        m.currentMenu = Menu.EndGame;
        GUIManagerTestHelper.findButtonByTextInParent("Continue", s.getRoot()).fire();
        JavaFXTestHelper.waitForPlatform();
        assertTrue(m.currentMenu == Menu.SingleplayerGameType);
    }

}