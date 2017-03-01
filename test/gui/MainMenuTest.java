package gui;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by jack on 01/03/2017.
 */
public class MainMenuTest {

    @Before
    public void setUp() throws Exception {
        // Setup JavaFX
        Thread t = new Thread(() -> new JFXPanel());
        t.start();
        t.join();
    }

    @Test
    public void singleButton() throws Exception {
        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene mainMenu = MainMenu.getScene(m);

        (GUIManagerTestHelper.findButtonByTextInParent("Single player", mainMenu.getRoot())).fire();

        assertTrue(m.currentMenu.equals("Singleplayer"));
    }

    @Test
    public void multiButton() throws Exception {
        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene mainMenu = MainMenu.getScene(m);

        (GUIManagerTestHelper.findButtonByTextInParent("Multiplayer", mainMenu.getRoot())).fire();

        assertTrue(m.currentMenu.equals("Nickname"));
    }

    @Test
    public void settingsButton() throws Exception {
        GUIManagerTestHelper m = new GUIManagerTestHelper();
        Scene mainMenu = MainMenu.getScene(m);

        (GUIManagerTestHelper.findButtonByTextInParent("Settings", mainMenu.getRoot())).fire();

        assertTrue(m.currentMenu.equals("Settings"));
    }

}