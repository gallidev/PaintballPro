package testSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import logic.TestGameMode;
import logic.TestRoundTimer;
import serverLogic.TestCaptureTheFlagMode;
import serverLogic.TestTeam;
import serverLogic.TestTeamMatchMode;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	TestRoundTimer.class,
	TestGameMode.class,
	TestTeamMatchMode.class,
	TestCaptureTheFlagMode.class,
	TestTeam.class,
})

/**
 * Logic Test Suite.
 * 
 * @author Alexandra Paduraru
 */
public class LogicSuite {   
}  