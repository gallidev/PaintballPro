package testSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import networking.game.client.TestClientInputSender;
import networking.game.client.TestClientIntegration;
import networking.game.server.TestLobbyIntegration;
import networking.game.server.TestServerGameSimulation;
import networking.game.server.TestServerGameStateSender;
import networking.game.server.TestServerInputReceiver;
import networking.game.server.TestServerIntegration;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	TestClientInputSender.class,
	TestClientIntegration.class,
	TestServerInputReceiver.class,
	TestServerGameSimulation.class,
	TestServerIntegration.class,
	TestLobbyIntegration.class,
	TestServerGameStateSender.class
})

/**
 * Networking game Test Suite.
 *
 * @author Alexandra Paduraru
 */
public class NetworkingGameSuite {
}