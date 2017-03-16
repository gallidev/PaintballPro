package networking.discoveryNew;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Class to announce a server's presence to a LAN
 * 
 * @author Matthew Walters
 */
public class DiscoveryServerAnnouncer extends Thread {

	private int portNo;
	public boolean m_running = true;

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
		      //Keep a socket open to listen to all the UDP trafic that is destined for this port
		      DatagramSocket socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
		      socket.setBroadcast(true);

		      while (true) {
		        System.out.println(getClass().getName() + ">>>Ready to receive broadcast packets!");

		        //Receive a packet
		        byte[] recvBuf = new byte[15000];
		        DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
		        socket.receive(packet);

		        //Packet received
		        System.out.println(getClass().getName() + ">>>Discovery packet received from: " + packet.getAddress().getHostAddress());
		        System.out.println(getClass().getName() + ">>>Packet received; data: " + new String(packet.getData()));

		        //See if the packet holds the right command (message)
		        String message = new String(packet.getData()).trim();
		        if (message.equals("DISCOVER_FUIFSERVER_REQUEST")) {
		          byte[] sendData = "DISCOVER_FUIFSERVER_RESPONSE".getBytes();

		          //Send a response
		          DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
		          socket.send(sendPacket);

		          System.out.println(getClass().getName() + ">>>Sent packet to: " + sendPacket.getAddress().getHostAddress());
		        }
		      }
		    } catch (IOException ex) {
		      //
		    }
	}
}