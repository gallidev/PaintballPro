package networkingDiscovery;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

/**
 * Class to announce a server's presence to a LAN
 */
public class ServerAnnouncer implements Runnable {

    private int portNo;

    /**
     * Create a new announcer
     * @param portNo TCP port that the game is running on
     */
    public ServerAnnouncer(int portNo) {
        this.portNo = portNo;
    }

    /**
     * Run the announcer
     */
    @Override
    public void run() {
        // Loop forever
        while (true) {
            try {

                int serverGamePort = portNo;
                String messageToClients = networkingDiscovery.IPAddress.getLAN() + ":" + serverGamePort;

                InetAddress broadcastAddress = InetAddress.getByName("225.0.0.1");
                MulticastSocket socket = new MulticastSocket(5000);
                socket.setNetworkInterface(NetworkInterface.getByName("en0"));
                socket.joinGroup(broadcastAddress);

                // Keep broadcasting the server, every 2 seconds
                while (true) {
                    DatagramPacket broadcast = new DatagramPacket(messageToClients.getBytes(), messageToClients.length(), broadcastAddress, 5000);
                    socket.send(broadcast);
                    Thread.sleep(2000);
                }

            } catch (Exception e) {
                System.err.println("Socket Server Exception: " + e);
            }
        }
    }
}
