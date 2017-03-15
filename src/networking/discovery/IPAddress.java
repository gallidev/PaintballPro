package networking.discovery;

import java.net.InetAddress;

/**
 * Class to get the LAN IP for a user
 *
 * @author Matthew Walters
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
			// Will never be reached.
		}
		return ret;
	}
}