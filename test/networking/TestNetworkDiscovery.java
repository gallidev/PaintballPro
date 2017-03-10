package networking;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import networking.discovery.DiscoveryClientListener;

/**
 * Test class to test Network Discovery.
 * Tests - DiscoveryClient.java, DiscoveryClientListener.java, DiscoveryServerAnnouncer.java, IPAddress.java
 * 
 * @author Matthew Walters
 */
public class TestNetworkDiscovery {

	DiscoveryClientListener client;
	
	@Before
	public void setUp() throws Exception {
		client = new DiscoveryClientListener();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertTrue(client.test());
	}

}
