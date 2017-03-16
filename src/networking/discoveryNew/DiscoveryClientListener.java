package networking.discoveryNew;

/**
 * Class to listen out for servers in the LAN
 * 
 * @author Matthew Walters
 */
public class DiscoveryClientListener {

	DiscoveryClient client;

	/**
	 * Get the IP address and port of the first server found
	 * 
	 * @return IP address and port, separated by a colon
	 */
	public String findServer() {
		client = new DiscoveryClient();
		client.start();
		try {
			client.join(10000);
		} catch (InterruptedException e) {
			return client.retVal;
		}
		return client.retVal;
	}

	/**
	 * Test method to test finding a running server.
	 * @return Test result - did it pass?
	 */
	public boolean test() {
		DiscoveryServerAnnouncer annoc = new DiscoveryServerAnnouncer(25561);
		annoc.start();
		String ret = this.findServer();
		annoc.m_running = false;
		client.interrupt();
		if(ret.split(":")[1].contains("25561"))
			return true;
		else
			return false;
	}
}