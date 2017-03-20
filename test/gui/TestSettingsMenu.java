package gui;

import helpers.GUIManagerTestHelper;
import helpers.JavaFXTestHelper;
import javafx.scene.Scene;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for the Settings Menu class
 */
public class TestSettingsMenu {
    @Before
    public void setUp() throws Exception {
        JavaFXTestHelper.setupApplication();
    }

    @Test
    public void getScene() throws Exception {
        GUIManagerTestHelper g = new GUIManagerTestHelper();
        Scene s = SettingsMenu.getScene(g);
    }

}