package networking;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import networking.discoveryNew.DiscoveryClientAnnouncer;

/**
 * Test class to test Network Discovery.
 * Tests - DiscoveryClient.java, DiscoveryClientListener.java, DiscoveryServerAnnouncer.java, IPAddress.java
 * 
 * @author Matthew Walters
 */
public class TestNetworkDiscovery {

	DiscoveryClientAnnouncer client;
	
	@Before
	public void setUp() throws Exception {
		client = new DiscoveryClientAnnouncer();
	}

	@After
	public void tearDown() throws Exception {
		client = null;
	}

	@Test
	public void test() {
		assertTrue(client.test());
	}

}
