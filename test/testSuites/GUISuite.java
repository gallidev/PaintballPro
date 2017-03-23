package testSuites;

import gui.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

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