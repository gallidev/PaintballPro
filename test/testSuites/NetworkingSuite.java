package testSuites;

import networking.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	TestConnection.class,
	TestMessages.class,
	TestServerBasicPlayer.class,
	TestLobbies.class,
	TestClientTable.class,
	TestTeamTable.class,
	TestNetworkDiscovery.class,
	TestUDP.class,
	TestServerClient.class
})

/**
 * Networking Test Suite.
 * 
 * @author Matthew Walters
 */
public class NetworkingSuite {   
}  