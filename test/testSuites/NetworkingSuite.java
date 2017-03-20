package testSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import networking.TestClientTable;
import networking.TestLobbies;
import networking.TestMessages;
import networking.TestNetworkDiscovery;
import networking.TestServerBasicPlayer;
import networking.TestServerClient;
import networking.TestTeamTable;
import networking.TestUDP;

@RunWith(Suite.class)

@Suite.SuiteClasses({
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