package testSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import gui.TestAlertBox;
import gui.TestEndGameMenu;
import gui.TestGUIConnection;
import gui.TestGUIManager;
import gui.TestGameLobbyMenu;
import gui.TestGameTypeMenu;
import gui.TestHelpMenu;
import gui.TestLoadingPane;
import gui.TestMainMenu;
import gui.TestMenuControls;
import gui.TestNicknameServerSelectMenu;
import gui.TestSettingsMenu;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        TestAlertBox.class,
        TestEndGameMenu.class,
        TestGameLobbyMenu.class,
        TestGameTypeMenu.class,
		TestGUIConnection.class,
        TestGUIManager.class,
        TestHelpMenu.class,
		TestLoadingPane.class,
        TestMainMenu.class,
        TestMenuControls.class,
        TestNicknameServerSelectMenu.class,
        TestSettingsMenu.class
})

/**
 * GUI Test Suite.
 *
 * @author Jack Hughes
 */
public class GUISuite {
}