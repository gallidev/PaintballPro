package networking.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

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

	public int exceptionCheck = 0;
	
	private boolean debug = false;
	private BufferedReader fromServer = null;
	private ClientReceiver clientReceiver;
	private ClientSender sender;
	private int clientID = 0;
	private PrintStream toServer = null;
	private Socket server = null;
	private String nickname;
	
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
	 * @param testing
	 *            Is this class under testing?
	 */
	public Client(String passedNickname, int portNum, String serverIP, GUIManager guiManager, int udpPortSenderNum,
			boolean testing) {

		nickname = passedNickname;

		// We check that nickname does not contain - or : as these are used in
		// our protocols.
		if (!nickname.contains(":") && !nickname.contains("-")) {
			try {
				// Connect to server
				server = new Socket(serverIP, portNum);
				toServer = new PrintStream(server.getOutputStream());
				fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));

				// Create two client threads, one for sending and one for
				// receiving messages, first one is done here:
				MessageQueue msgQueue = new MessageQueue();
				sender = new ClientSender(msgQueue, toServer, nickname);
				
				clientID = 0; // Set temporary client ID.
				String text = ""; 

				// Run them in parallel, first one started here:
				sender.start();

				// Get messages to this client and look for a response in a
				// specific format.
				try {
					boolean found = false;
					while (!found) {
						text = fromServer.readLine();
						if (text.contains("UserID is:")) {
							// Set client id as returned value.
							clientID = Integer.parseInt(text.substring(10));
							found = true;

							// Sanity output.
							if (debug)
								System.out.println("Client has id:" + clientID);

							TeamTable teams = new TeamTable();

							// Make a UDP Receiver and Sender for low-latency
							// in-game.
							UDPClient udpReceiver = new UDPClient(clientID, serverIP, 19857, guiManager, teams,
									udpPortSenderNum, nickname);
							
							// We can now set up the message receiver for the
							// client.
							clientReceiver = new ClientReceiver(clientID, fromServer, sender, guiManager, udpReceiver, teams);
							
							udpReceiver.start();
							clientReceiver.start();

							// Wait for them to end and then close sockets.
							Thread t = new Thread(new Runnable() {
								// Define our thread.
								@Override
								public void run() {
									try {
										if (debug)
											System.out.println("Client Started");
										
										sender.join(); // Wait for sender to close
										toServer.close(); // Close connection to server
										
										if (debug) 
											System.out.println("Sender and Sender Stream closed");
										
										clientReceiver.join(); // Wait for receiver to stop
										fromServer.close(); // Close connection from server
										
										if (debug) 
											System.out.println("Receiver and Receiver Stream closed");
										
										server.close(); // Close server socket
										
										if (debug)
											System.out.println("Server closed.");
										
										udpReceiver.active = false;
										udpReceiver.join(500);
										
										if(debug)
											System.out.println("UDP Client closed.");

										if (debug)
											System.out.println("Client has been stopped.");
									// Catch possible errors.
									} catch (InterruptedException | IOException e) {
										// Close threads smoothly.
										clientReceiver.interrupt();
										toServer.close();
										udpReceiver.active = false;

										if (!testing)
											(new AlertBox("Communication Failed",
													"Paintball Pro could not talk to the server. Ensure the server is running and try again."))
															.showAlert();
										exceptionCheck = 5;
									}
									if (debug) 
										System.out.println("All closed.");
								}
							});
							// Run the thread.
							t.start();
						} else if (text.contains("UsernameInUse")) {
							if (debug) 
								System.out.println("Username already in use.");
							
							sender.m_running = false;
							
							try {
								sender.interrupt();
								sender.join(1000);
							} catch (InterruptedException e) {
								// 
							}
							
							toServer.close();
							fromServer.close();
							server.close();
							
							if (!testing)
								(new AlertBox("Username Error",
										"Your username is already used by another player. Please choose another username."))
												.showAlert();
							
							if (exceptionCheck == 0)
								exceptionCheck = 6;
						}
					}
				} catch (IOException e) {
					// Close threads smoothly.
					toServer.close();
					fromServer.close();
					server.close();

					if (!testing && exceptionCheck == 0)
						(new AlertBox("Connection Failed",
								"Paintball Pro could not talk to the server. Ensure the server is running and try again."))
										.showAlert();
					
					if (exceptionCheck == 0)
						exceptionCheck = 4;
				}
			}
			// If server isn't running.
			catch (IOException e) {
				if (!testing && exceptionCheck == 0)
					(new AlertBox("Connection Failed",
							"Paintball Pro could not talk to the server. Ensure the server is running and try again."))
									.showAlert();
				
				if (exceptionCheck == 0)
					exceptionCheck = 2;
			}
		}
		// If username contains the character : or - (used for a string
		// information
		// separator so cannot be in a nickname).
		else {
			if (!testing && exceptionCheck == 0)
				(new AlertBox("Username error",
						"Your username cannot contain ':' or '-' characters. Please choose another username."))
								.showAlert();
			
			if (exceptionCheck == 0)
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
		return clientReceiver;
	}
}