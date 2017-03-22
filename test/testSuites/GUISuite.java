package testSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import gui.TestAlertBox;
import gui.TestEndGameMenu;
import gui.TestGUIManager;
import gui.TestGameLobbyMenu;
import gui.TestGameTypeMenu;
import gui.TestHelpMenu;
import gui.TestMainMenu;
import gui.TestMenuControls;
import gui.TestNicknameServerSelectMenu;
import gui.TestServerGUI;
import gui.TestSettingsMenu;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        TestAlertBox.class,
        TestEndGameMenu.class,
        TestGameLobbyMenu.class,
        TestGameTypeMenu.class,
        TestGUIManager.class,
        TestHelpMenu.class,
        TestMainMenu.class,
        TestMenuControls.class,
        TestNicknameServerSelectMenu.class,
        TestServerGUI.class,
        TestSettingsMenu.class
})

/**
 * AI Test Suite.
 *
 * @author Sivarjuen Ravichandran
 */
public class GUISuite {
}