package networking.game;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import integration.server.ServerInputReceiver;
import networking.server.ClientTable;
import networking.server.LobbyTable;
import players.ServerBasicPlayer;

/**
 * Server-side Sender and Receiver using UDP protocol for in-game transmission.
 * One per server.
 *
 * @author Matthew Walters
 */
public class UDPServer extends Thread {

	private boolean debug = true;
	private ClientTable clients;
	private DatagramSocket serverSocket;
	private int sIP;
	
	public String winnerTest;
	
	private LobbyTable lobbyTab;
	private ServerInputReceiver inputReceiver;
	
	public boolean m_running = true;

	/**
	 * Constructor, sets global variables to those passed for the UDP Server.
	 * 
	 * @param clientTable
	 *            Table storing all necessary client information.
	 * @param lobby
	 *            Table storing all necessary lobby information.
	 * @param sIP
	 *            Server IP Address.
	 */
	public UDPServer(ClientTable clientTable, LobbyTable lobby, int sIP) {
		clients = clientTable;
		this.lobbyTab = lobby;
		this.sIP = sIP;
	}

	/**
	 * Loop through, reading messages from the server. Main method, ran when the
	 * thread is started.
	 */
	public void run() {
		try {
			if (debug)
				System.out.println("Starting server");

			serverSocket = new DatagramSocket(sIP);

			if (debug)
				System.out.println("Opened socket on port " + serverSocket.getLocalPort() + " with ip addr:"
						+ serverSocket.getInetAddress());
			
			byte[] receiveData = new byte[1024];
			
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			
			while (m_running) {
				
				if (debug)
					System.out.println("Waiting to receive packet");

				serverSocket.receive(receivePacket);

				if (debug)
					System.out.println("packetLength: " + receivePacket.getLength());

				String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());

				if (debug)
					System.out.println("Packet received with text:" + sentence);

				InetAddress IPAddress;
				int port;

				if (sentence.contains("ExitUDP"))
					break;
				else if (sentence.contains("Connect:")) {
					if (debug)
						System.out.println("Trying to connect now");
					
					IPAddress = receivePacket.getAddress();
					port = receivePacket.getPort();
					
					if (debug)
					{
						System.out.println("Received message from:" + IPAddress.toString() + " on port:" + port);
						System.out.println("Attempting to parse client id");	
					}
						

					int clientID = Integer.parseInt(sentence.substring(8));

					if (debug)
						System.out.println("Their ip is:" + IPAddress.toString());
					
					String ipStr = IPAddress.toString().substring(1, IPAddress.toString().length());
					String ipAdd = ipStr + ":" + port;
					byte[] sendData;
					
					clients.addNewIP(ipAdd, clientID);
					clients.addUDPQueue(ipAdd);
					
					sendData = new byte[1024];
					sendData = "Successfully Connected".getBytes();
					IPAddress = InetAddress.getByName(ipStr);
					
					if (debug)
						System.out.println("Sending packet back");
					
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
					serverSocket.send(sendPacket);
				
				} else {
					// We assume that all players in a game are now connected
					// and their ip addresses inserted into clientTable.
					// if they are not, we will not be able to send messaged to
					// them using sendToAll.
					String ip = receivePacket.getAddress().toString();
					String ipFrom = ip.substring(1, ip.length());
					
					if (debug)
						System.out.println("Message was received from:" + ipFrom);
					
					ipFrom = ipFrom + ":" + receivePacket.getPort();
					sentence = sentence.trim();

					/* In-game actions */

					switch (sentence.charAt(0)) {
					case '0':
						playerInputChanged(sentence);
						break;
					case '2':
						getWinner(sentence);
						break;
					case '3':
						sendBackTime(sentence);
						break;
					default:
						break;

					}
				}
			}
		} catch (Exception e) {
			if (debug)
				System.err.println(e.getMessage());
		} finally {
			if (debug)
				System.out.println("Closing Server");
			serverSocket.close();
		}
	}

	/**
	 * Send a message to all clients in a game - based on Lobby.
	 * 
	 * @param toBeSent
	 *            Message to be sent to all clients.
	 * @param lobbyID
	 *            ID of game lobby.
	 */
	public void sendToAll(String toBeSent, int lobbyID) {
		byte[] sendData = new byte[1024];
		ServerBasicPlayer[] players;
		sendData = toBeSent.getBytes();

		// We get all players in the same game as the transmitting player.
		players = lobbyTab.getLobby(lobbyID).getPlayers();
		
		// Let's send a message to them all.
		for (ServerBasicPlayer player : players) {
			int id = player.getID();
			String playerIP = clients.getIP(id);
			
			// Parse IP to get first part and port number.
			String ipAddr = playerIP.split(":")[0];
			int port = Integer.parseInt(playerIP.split(":")[1]);
			
			try {
				// Let's send the message.
				InetAddress sendAddress = InetAddress.getByName(ipAddr);
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, sendAddress, port);
				serverSocket.send(sendPacket);
			} catch (Exception e) {
				if (debug)
					System.out.println("Cannot send message:" + toBeSent + ", to:" + ipAddr);
			}
		}
	}

	/**
	 * Send a message to all clients in a game - based on Lobby.
	 * 
	 * @param toBeSent
	 *            Message to be sent to all clients.
	 * @param ip
	 *            IP of a particular client in the lobby we want to send
	 *            messages to.
	 */
	public void sendToAll(String toBeSent, String ip) {
		if (debug) {
			System.out.println("Trying to get lobby id");
			System.out.println("client id is:" + clients.getID(ip));
		}

		// we get the lobby id.
		int lobbyID = clients.getPlayer(clients.getID(ip)).getAllocatedLobby();
		
		if (debug)
			System.out.println("The lobby id is:" + lobbyID);
		
		// we can now send to all clients in the same lobby as the origin
		// client.
		sendToAll(toBeSent, lobbyID);
	}

	/**
	 * Method to send to specific client.
	 * 
	 * @param clientID
	 *            ID of client to send to.
	 * @param toBeSent
	 *            Message to send to client.
	 */
	public void sendToSpec(int clientID, String toBeSent) {
		byte[] sendData = new byte[1024];
		String playerIP;
		String ipAddr;
		int port;
		
		sendData = toBeSent.getBytes();

		playerIP = clients.getIP(clientID);
		ipAddr = playerIP.split(":")[0];
		port = Integer.parseInt(playerIP.split(":")[1]);
		
		try {
			// Let's send the message.
			InetAddress sendAddress = InetAddress.getByName(ipAddr);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, sendAddress, port);
			serverSocket.send(sendPacket);
		} catch (Exception e) {
			if (debug)
				System.out.println("Cannot send message:" + toBeSent + ", to:" + ipAddr);
		}
	}

	// -------------------------------------
	// -----------Game Methods--------------
	// -------------------------------------

	/**
	 * Interprets the client message containing the user inputs and calls the
	 * corresponding method in the ServerInputReceiver class, which computes the
	 * player's new location.
	 * 
	 * @param text
	 *            The protocol string received from the client.
	 *
	 * @author Alexandra Paduraru
	 */
	public void playerInputChanged(String text) {
		// Protocol: "O:<id>:Up:Down:Left:Right:Shoot:Mouse:<mX>:<mY>"

		if (debug)
			System.out.println("Input Received: " + text);
		
		String[] actions = text.split(":");

		int id = Integer.parseInt(actions[1]);

		boolean up = false;
		boolean down = false;
		boolean left = false;
		boolean right = false;
		boolean shoot = false;
		double angle = 0;

		for (int i = 0; i < actions.length; i++) {
			String act = actions[i];
			switch (act) {
			case "Up":
				up = true;
				break;
			case "Down":
				down = true;
				break;
			case "Left":
				left = true;
				break;
			case "Right":
				right = true;
				break;
			case "Angle":
				angle = Double.parseDouble(actions[i + 1]);
				i++;
				break;
			case "Shoot":
				shoot = true;
				break;
			default:
				break;
			}
		}
		System.out.println(inputReceiver);
		inputReceiver.updatePlayer(id, up, down, left, right, shoot, angle);
	}

	/**
	 * Receives the game winner from the clients.
	 * 
	 * @param text
	 *            The protocol string containing the winner.
	 *
	 * @author Alexandra Paduraru
	 */
	public void getWinner(String text) {
		String winner = text.split(":")[1];

		
		winnerTest = winner;
		if (debug)
			System.out.println("The winner is : " + winner);
	}

	private void sendBackTime(String text) {
		if (debug)
			System.out.println("Input Received: " + text);
		
		String[] actions = text.split(":");
		int id = Integer.parseInt(actions[1]);
		String toBeSent = "T:" + id;
		toBeSent += ":" + actions[2];
		toBeSent += ":" + System.currentTimeMillis();

		sendToSpec(id, toBeSent);
	}

	/* Getters and setters below */

	/**
	 * Sets the input receiver.
	 * 
	 * @param inputReceiver
	 *            The new ServerInputReceiver.
	 */
	public void setInputReceiver(ServerInputReceiver inputReceiver) {
		this.inputReceiver = inputReceiver;
	}
}