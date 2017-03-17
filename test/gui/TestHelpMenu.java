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
 * Created by jack on 17/03/2017.
 */
public class TestHelpMenu {

    @Before
    public void setUp() throws Exception {
        JavaFXTestHelper.setupApplication();
    }

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