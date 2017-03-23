package networking.discoveryNew;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Class to wait for a client's announcement of presence to a LAN
 * 
 * @author Matthew Walters
 */
public class DiscoveryServerAnnouncer extends Thread {

	public boolean m_running = true;

	private DatagramSocket socket;

	/**
	 * Create a new announcer
	 * 
	 * @param portNo
	 *            TCP port that the game is running on
	 */
	public DiscoveryServerAnnouncer() {
	}

	/**
	 * Run the listener
	 */
	@Override
	public void run() {
		try {
			// Keep a socket open to listen to all the UDP traffic that is
			// destined for this port
			socket = new DatagramSocket(25561, InetAddress.getByName(IPAddress.getLAN()));
			socket.setBroadcast(true);

			while (true) {
				// Receive a packet
				byte[] received = new byte[240];
				DatagramPacket packet = new DatagramPacket(received, received.length);
				String message;
				
				socket.receive(packet);

				if (!m_running)
					break;

				// Packet received - see if the packet has the right message
				message = new String(packet.getData()).trim();
				if (message.equals("discover_server")) {
					byte[] sendData = "discover_response".getBytes();

					// Send a response
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(),
							packet.getPort());
					socket.send(sendPacket);
				}
			}
			socket.close();
		} catch (IOException ex) {
			//
		}
		return;
	}

	public void stopThread() {
		m_running = false;
		socket.close();
	}
}