package networkingClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import gui.AlertBox;
import gui.GUIManager;
import networkingGame.UDPClientReceiver;
import networkingShared.MessageQueue;

public class Client {
	ClientSender sender;
	ClientReceiver receiver;
	int clientID;
	PrintStream toServer = null;
	BufferedReader fromServer = null;
	Socket server = null;

	public Client(String passedNickname, int portNum, String machName, GUIManager m) throws Exception {
		
		String nickname = passedNickname;

		// We check that nickname does not contain - or : as these are used in our protocols.
		if (!nickname.contains(":") || !nickname.contains("-")) {
			
			int portNumber = portNum;
			String hostname = machName;

			// Open sockets:
			try {
				// Connect to server
				server = new Socket(hostname, portNumber);
				// Get output and input streams from the server.
				toServer = new PrintStream(server.getOutputStream());
				fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
			}
			// If host cannot be found
			catch (UnknownHostException e) {
				AlertBox.showAlert("Connection Failed", "Please check that the server is running, and the IP address is correct.");
				System.err.println("Unknown host: " + hostname);
//				System.exit(1); // Exit
				throw new Exception();
			}
			// If server isn't running.
			catch (IOException e) {
				AlertBox.showAlert("Connection Failed", "Please check that the server is running, and the IP address is correct.");
				System.err.println("The server doesn't seem to be running " + e.getMessage());
//				System.exit(1); // Exit
				throw new Exception();
			}

			// Create two client threads, one for sending and one for receiving
			// messages, first one is done here:
			MessageQueue msgQueue = new MessageQueue();
			sender = new ClientSender(msgQueue, toServer, nickname);

			// Run them in parallel, first one started here:
			sender.start();

			clientID = 0; // Set temp client ID.

			// Get messages to this client and look for a response in a specific
			// format.
			boolean found = false;
			while (!found) {
				String text = "";
				try {
					text = fromServer.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (text.contains("UserID is:")) {
					// Set client id as returned value.
					clientID = Integer.parseInt(text.substring(10));
					found = true;
				}
			}
			// Sanity output.
			System.out.println("Client has id:" + clientID);
			
			TeamTable teams = new TeamTable();
			
			//Make a UDP Receiver and Sender for low-latency in-game.
			UDPClientReceiver udpReceiver = new UDPClientReceiver(clientID,hostname,m,teams);
			udpReceiver.start();

			// We can now set up the message received for the client.
			receiver = new ClientReceiver(clientID, fromServer, sender, msgQueue, m, udpReceiver,teams);
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
						fromServer.close(); // Close connection from server
						server.close(); // Close server socket
						// Acknowledge to the client that everything has stopped.
						System.out.println("Client has been stopped."); 
					// Catch possible errors.
					} catch (IOException e) {
						System.err.println("Something wrong " + e.getMessage());
						System.exit(1); // Give up.
					} catch (InterruptedException e) {
						System.err.println("Unexpected interruption " + e.getMessage());
						System.exit(1); // Give up.
					}
				}
			});
			// Run the thread.
			t.start();

		}
		// If username contains the character : (used for a string information
		// separator so cannot be in a nickname.
		else {
			System.out.println("Error: Username cannot contain character ':', please change it.");
			AlertBox.showAlert("Username error", "Your username cannot contain a ':' character");
			throw new Exception();
//			System.exit(1);
		}
	}

	public int getClientID() {
		return clientID;
	}

	public ClientSender getSender() {
		return sender;
	}

	public ClientReceiver getReceiver() {
		return receiver;
	}
}