package gui;

import enums.Menu;
import enums.TeamEnum;
import helpers.GUIManagerTestHelper;
import helpers.JavaFXTestHelper;
import javafx.scene.Scene;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by jack on 02/03/2017.
 */
public class TestEndGameMenu {
    @Test
    public void getScene() throws Exception {
        JavaFXTestHelper.setupApplication();

        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene s = EndGameMenu.getScene(m, "1,0", TeamEnum.BLUE);
        m.currentMenu = Menu.EndGame;
        GUIManagerTestHelper.findButtonByTextInParent("Continue", s.getRoot()).fire();
        Thread.sleep(2000);
        assertTrue(m.currentMenu == Menu.MultiplayerGameType);
    }

}