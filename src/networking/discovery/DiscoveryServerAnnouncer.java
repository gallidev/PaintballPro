package networking.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Class to announce a server's presence to a LAN
 * 
 * @author MattW
 */
public class DiscoveryServerAnnouncer implements Runnable {

    private int portNo;

    /**
     * Create a new announcer
     * @param portNo TCP port that the game is running on
     */
    public DiscoveryServerAnnouncer(int portNo) {
        this.portNo = portNo;
    }

    /**
     * Run the announcer
     */
    @Override
    public void run() {
        try {

            int serverGamePort = portNo;
            String messageToClients = networking.discovery.IPAddress.getLAN() + ":" + serverGamePort;

            InetAddress broadcastAddress = InetAddress.getByName("225.0.0.1");
			System.setProperty("java.net.preferIPv4Stack", "true");
			MulticastSocket socket = new MulticastSocket(25566);
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
	        while (networkInterfaces.hasMoreElements()) {
	            NetworkInterface iface = networkInterfaces.nextElement();
	            try {
	                socket.setNetworkInterface(iface);
	            } catch (IOException e) {
	            	//e.printStackTrace();
	            }
	        }
            socket.joinGroup(broadcastAddress);

            // Keep broadcasting the server, every 5 seconds.
            while (true) {
                DatagramPacket broadcast = new DatagramPacket(messageToClients.getBytes(), messageToClients.length(), broadcastAddress, 25566);
                socket.send(broadcast);
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            System.err.println("Socket Server Exception: " + e);
        }
    }
}
