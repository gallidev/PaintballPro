package testSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import integrationClient.TestClientInputSender;
import integrationClient.TestClientIntegration;
import integrationServer.TestServerGameSimulation;
import integrationServer.TestServerInputReceiver;
import integrationServer.TestServerIntegration;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	TestClientInputSender.class,
	TestClientIntegration.class,
	TestServerInputReceiver.class,
	TestServerGameSimulation.class,
	TestServerIntegration.class,
})

/**
 * Logic Test Suite.
 * 
 * @author Alexandra Paduraru
 */
public class IntegrationSuite {   
}  