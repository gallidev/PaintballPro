package networking.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import gui.AlertBox;
import gui.GUIManager;
import networking.game.UDPClient;
import networking.shared.MessageQueue;

/**
 * Controls main client code - holds server sender and receiver threads.
 *
 * @author Matthew Walters
 */
public class Client {

	private ClientSender sender;
	private ClientReceiver receiver;
	private int clientID = 0;
	private PrintStream toServer = null;
	private BufferedReader fromServer = null;
	private Socket server = null;
	private String nickname;
	private boolean singlePlayer;
	private boolean testing;
	public int exceptionCheck = 0; 

	/**
	 * Sets up Client, starts up threads and connects to the server, retrieving
	 * an id for this client.
	 * 
	 * @param passedNickname
	 *            Nickname that the user wants to be set as.
	 * @param portNum
	 *            Port number on server to connect to.
	 * @param serverIP
	 *            IP address of the server.
	 * @param guiManager
	 *            GUI Manager object.
	 * @param udpPortSenderNum
	 *            Port Number to send UDP messages on.
	 */
	public Client(String passedNickname, int portNum, String serverIP, GUIManager guiManager, int udpPortSenderNum,
			boolean testing) {

		this.testing = testing;

		nickname = passedNickname;

		// We check that nickname does not contain - or : as these are used in
		// our protocols.
		if (!nickname.contains(":") && !nickname.contains("-")) {

			int portNumber = portNum;
			String hostname = serverIP;

			try {
				// Connect to server
				server = new Socket(hostname, portNumber);
				// Get output and input streams from the server.
				toServer = new PrintStream(server.getOutputStream());
				fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));

				// Create two client threads, one for sending and one for
				// receiving
				// messages, first one is done here:
				MessageQueue msgQueue = new MessageQueue();
				sender = new ClientSender(msgQueue, toServer, nickname);

				// Run them in parallel, first one started here:
				sender.start();

				clientID = 0; // Set temporary client ID.

				// Get messages to this client and look for a response in a
				// specific format.
				String text = "";
				try {
					boolean found = false;
					while (!found) {
						text = fromServer.readLine();
						if (text.contains("UserID is:")) {
							// Set client id as returned value.
							clientID = Integer.parseInt(text.substring(10));
							found = true;
						}
					}

					// Sanity output.
					System.out.println("Client has id:" + clientID);

					TeamTable teams = new TeamTable();

					// Make a UDP Receiver and Sender for low-latency in-game.
					UDPClient udpReceiver = new UDPClient(clientID, hostname, 19876, guiManager, teams,
							udpPortSenderNum, nickname);
					udpReceiver.start();

					// We can now set up the message received for the client.
					receiver = new ClientReceiver(clientID, fromServer, sender, guiManager, udpReceiver, teams);
					receiver.start();

					// Wait for them to end and then close sockets.
					Thread t = new Thread(new Runnable() {
						// Define our thread.
						@Override
						public void run() {
							try {
								System.out.println("Client Started");
								sender.join(); // Wait for sender to close
								toServer.close(); // Close connection to server
								receiver.join(); // Wait for receiver to stop
								fromServer.close(); // Close connection from
													// server
								server.close(); // Close server socket
								// Acknowledge to the client that everything has
								// stopped.
								System.out.println("Client has been stopped.");
								// Catch possible errors.
							} catch (InterruptedException | IOException e) {
								if (!testing)
									AlertBox.showAlert("Connection Failed",
											"Something went wrong, please try again.");
								else
									exceptionCheck = 5;
							}
						}
					});
					// Run the thread.
					t.start();

				} catch (IOException e) {
					if (!testing)
						AlertBox.showAlert("Connection Failed",
								"Cannot read from the server, please try again.");
					else
						exceptionCheck = 4;
				}
			}
			// If host cannot be found
//			catch (UnknownHostException e) {
//				if (!testing)
//					AlertBox.showAlert("Connection Failed",
//							"Please check that the server is running, and the IP address is correct.");
//				else
//					exceptionCheck = 3;
//			}
			// If server isn't running.
			catch (IOException e) {
				if (!testing)
					AlertBox.showAlert("Connection Failed",
							"Please check that the server is running, and the IP address is correct.");
				else
					exceptionCheck = 2;
			}
		}
		// If username contains the character : or - (used for a string information
		// separator so cannot be in a nickname).
		else {
			if (!testing)
				AlertBox.showAlert("Username error", "Your username cannot contain ':' or '-' characters, please try again.");
			else
				exceptionCheck = 1;
		}
	}

	/**
	 * Returns the id given by server for the client.
	 * 
	 * @return Client's id.
	 */
	public int getClientID() {
		return clientID;
	}

	/**
	 * Returns the sender thread to send messages to the server.
	 * 
	 * @return Sender thread.
	 */
	public ClientSender getSender() {
		return sender;
	}

	/**
	 * Returns the receiver thread to receive messages from the server.
	 * 
	 * @return Receiver thread.
	 */
	public ClientReceiver getReceiver() {
		return receiver;
	}
}