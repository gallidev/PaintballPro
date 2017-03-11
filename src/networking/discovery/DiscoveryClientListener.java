package networking.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Class to listen out for servers in the LAN
 * 
 * @author MattW
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
	
	public boolean test() {
		Thread t = new Thread(new DiscoveryServerAnnouncer(25566));
		t.start();
		String ret = this.findServer();

		if(ret.split(":")[1].contains("25566"))
			return true;
		else
			return false;
	}
	
}