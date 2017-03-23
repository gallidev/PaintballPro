package testSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import integrationClient.TestClientInputSender;
import integrationClient.TestClientIntegration;
import integrationServer.TestLobbyIntegration;
import integrationServer.TestServerGameSimulation;
import integrationServer.TestServerInputReceiver;
import integrationServer.TestServerIntegration;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	TestClientInputSender.class,
	TestClientIntegration.class,
	TestClientIntegration.class,
	TestServerInputReceiver.class,
	TestServerGameSimulation.class,
	TestServerIntegration.class,
	TestLobbyIntegration.class,
})

/**
 * Logic Test Suite.
 *
 * @author Alexandra Paduraru
 */
public class IntegrationSuite {
}