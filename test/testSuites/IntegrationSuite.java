package testSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import integrationClient.TestClientInputSender;
import integrationClient.TestClientIntegration;
import integrationServer.TestServerGameSimulation;
import integrationServer.TestServerInputReceiver;
import integrationServer.TestServerIntegration;
import test.TestServerGameStateSender;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	TestClientIntegration.class,
	TestClientInputSender.class,
	TestServerInputReceiver.class,
	TestServerGameSimulation.class,
	TestServerIntegration.class,
	TestServerGameStateSender.class
})

/**
 * Integration Test Suite.
 *
 * @author Alexandra Paduraru
 */
public class IntegrationSuite {
}