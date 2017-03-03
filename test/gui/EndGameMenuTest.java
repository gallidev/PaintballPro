package gui;

import enums.MenuEnum;
import helpers.GUIManagerTestHelper;
import helpers.JavaFXTestHelper;
import javafx.scene.Scene;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jack on 02/03/2017.
 */
public class EndGameMenuTest {
    @Test
    public void getScene() throws Exception {
        JavaFXTestHelper.setupApplication();

        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene s = EndGameMenu.getScene(m);
        m.currentMenu = MenuEnum.EndGame;
        GUIManagerTestHelper.findButtonByTextInParent("Main Menu", s.getRoot()).fire();
        assertTrue(m.currentMenu == MenuEnum.MainMenu);
    }

}