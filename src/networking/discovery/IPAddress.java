package networking.discovery;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Class to get the LAN IP for a user
 * 
 * @author MattW
 */
public class IPAddress {
	
	/**
	 * Get the LAN IP for the current machine
	 * 
	 * @return LAN IP
	 */
	public static String getLAN() {
		String ret = "";
		try {
			ret = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			//
		}
		return ret;
	}
}