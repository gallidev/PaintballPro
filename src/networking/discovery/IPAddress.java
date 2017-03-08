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
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			while (true) {
				try {
					Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
					while (en.hasMoreElements()) {
						NetworkInterface ni = (NetworkInterface) en.nextElement();
						Enumeration<InetAddress> ee = ni.getInetAddresses();
						while (ee.hasMoreElements()) {
							InetAddress ia = (InetAddress) ee.nextElement();
							if (!ia.isLinkLocalAddress() && !ia.isMulticastAddress() && !ia.isLoopbackAddress()) {
								return ia.getHostAddress();
							}
						}
					}
				} catch (Exception e2) {
					// Loop if exception
				}
			}
		}
	}
}