package networking.discovery;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

/**
 * Class to broadcast presence to the LAN in search of a running server.
 * 
 * @author Matthew Walters
 */
public class DiscoveryClient extends Thread {

	public String retVal = "";
	
	private boolean debug = false;

	/**
	 * Get the IP address and port of the first server found
	 * 
	 * @return IP address and port, separated by a colon, e.g. 192.168.0.1:80
	 */
	public String findServer() {
		// Find the server using UDP broadcast
		try {
			// Open a random port to send the package
			DatagramSocket clientSocket = new DatagramSocket();
			byte[] sendData = "discover_server".getBytes();
			Enumeration<NetworkInterface> interfaces;
			byte[] receivedMessage;
			DatagramPacket receivePacket;
			String message;
			
			clientSocket.setBroadcast(true);

			// Try multicast ip first.
			try {
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
						InetAddress.getByName("255.255.255.255"), 25561);
				clientSocket.send(sendPacket);
			} catch (Exception e) {
			}

			// Broadcast the message over all the network interfaces
			interfaces = NetworkInterface.getNetworkInterfaces();
			
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();

				if (networkInterface.isLoopback() || !networkInterface.isUp()) {
					continue;
				}

				for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
					InetAddress broadcast = interfaceAddress.getBroadcast();
					if (broadcast == null) {
						continue;
					}

					// Send the broadcast package
					try {
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 25561);
						clientSocket.send(sendPacket);
					} catch (Exception e) {
					}
				}
			}

			try {
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
						InetAddress.getByName(networking.discovery.IPAddress.getLAN()), 25561);
				clientSocket.send(sendPacket);
				if(debug)
					System.out.println("::: " + InetAddress.getByName("127.0.0.1").toString());
			} catch (Exception e) {
			}

			// Wait for a response from the Server.
			receivedMessage = new byte[240];
			receivePacket = new DatagramPacket(receivedMessage, receivedMessage.length);
			
			while (true) {
				clientSocket.receive(receivePacket);
				if(debug)
					System.out.println("Received a packet from server.");
				if (!receivePacket.getAddress().getHostAddress().contains("192.168.56"))
					break;
			}

			// Check if the message is correct
			message = new String(receivePacket.getData()).trim();
			if (message.equals("discover_response")) {
				clientSocket.close();
				return receivePacket.getAddress().getHostAddress() + ":25566";
			}
			clientSocket.close();

			// Close the port!
		} catch (IOException ex) {
			//
		}
		return "";
	}

	/**
	 * Run the main method of this thread.
	 */
	public void run() {
		retVal = this.findServer();
	}
}