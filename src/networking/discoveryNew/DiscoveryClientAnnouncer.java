package networking.discoveryNew;

/**
 * Class to broadcast a client's presence on the LAN
 * 
 * @author Matthew Walters
 */
public class DiscoveryClientAnnouncer {

	private DiscoveryClient client;

	/**
	 * Get the IP address and port of the first server found
	 * 
	 * @return IP address and port, separated by a colon
	 */
	public String findServer() {
		client = new DiscoveryClient();
		client.start();
		try {
			client.join(10000); // Timeout after 10 seconds.
		} catch (InterruptedException e) {
			// Close Client Thread.

			return client.retVal;
		}
		return client.retVal;
	}

	/**
	 * Test method to test finding a running server.
	 * 
	 * @return Test result - did it pass?
	 */
	public boolean test() {
		DiscoveryServerListener annoc = new DiscoveryServerListener();
		String ret;
		annoc.start();
		ret = this.findServer();
		annoc.m_running = false;
		client.interrupt();
		if (ret.split(":")[1].contains("25566"))
			return true;
		else
			return false;
	}
}