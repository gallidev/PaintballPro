package gui;

import helpers.GUIManagerTestHelper;
import javafx.scene.Scene;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for Nickname and Server Selection Menu class
 */
public class TestNicknameServerSelectMenu {
    @Test
    public void getScene() throws Exception {
        GUIManagerTestHelper g = new GUIManagerTestHelper();
        Scene s = NicknameServerSelectMenu.getScene(g);
    }

}