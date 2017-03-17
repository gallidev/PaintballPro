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

	private int portNo;
	private DatagramSocket socket;
	public boolean m_running = true;

	/**
	 * Create a new announcer
	 * @param portNo TCP port that the game is running on
	 */
	public DiscoveryServerAnnouncer(int portNo) {
		this.portNo = portNo;
	}

	/**
	 * Run the listener
	 */
	@Override
	public void run() {
		try {
			//Keep a socket open to listen to all the UDP traffic that is destined for this port
			socket = new DatagramSocket(25561, InetAddress.getByName(IPAddress.getLAN()));
			socket.setBroadcast(true);

			while (true) {
				//Receive a packet
				byte[] received = new byte[240];
				DatagramPacket packet = new DatagramPacket(received, received.length);
				socket.receive(packet);

				if(!m_running)
					break;
				
				//Packet received - see if the packet has the right message
				String message = new String(packet.getData()).trim();
				if (message.equals("discover_server")) {
					byte[] sendData = "discover_response".getBytes();

					//Send a response
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
					socket.send(sendPacket);
				}
			}
			socket.close();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		return;
	}
	
	public void stopThread()
	{
		m_running = false;
		socket.close();
	}
}