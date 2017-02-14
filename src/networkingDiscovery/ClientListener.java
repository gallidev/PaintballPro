package networkingDiscovery;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

/**
 * Class to listen out for servers in the LAN
 */
public class ClientListener {

    /**
     * Get the IP address and port of the first server found
     * @return IP address and port, separated by a colon
     */
    public static String findServer() {
        while (true) {
            try {
                InetAddress broadcastAddress = InetAddress.getByName("225.0.0.1");

                MulticastSocket socket = new MulticastSocket(5000);
                socket.setNetworkInterface(NetworkInterface.getByName("en0"));
                socket.joinGroup(broadcastAddress);

                byte[] buf = new byte[1023];
                DatagramPacket packetFromServer = new DatagramPacket(buf, buf.length);

                socket.receive(packetFromServer);
                String data = new String(packetFromServer.getData(), packetFromServer.getOffset(), packetFromServer.getLength());
                socket.leaveGroup(broadcastAddress);

                socket.close();

                return data;

            } catch (Exception e) {
                System.err.println("Socket Client Exception!" + e);
            }
        }
    }
}
