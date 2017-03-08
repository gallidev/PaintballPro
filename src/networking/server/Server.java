package networking.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import gui.ServerGUI;
import gui.ServerView;
import networking.game.UDPServer;
import networking.shared.Message;
import networking.shared.MessageQueue;
/**
 * Class to represent a running server that connects to multiple clients via
 * sockets.
 * 
 * @author MattW
 */
public class Server {
	/**
	 * Main implementation method, handles connecting clients.
	 * 
	 * @param args
	 *            Arguments passed through from the user.
	 *            0 - port number
	 *            1 - listen address
	 */
	public static void main(String[] args, ServerView gui) {
		
		// This will be shared by the server threads:
		ClientTable clientTable = new ClientTable();
		// Create a new lobby instance.
		LobbyTable gameLobbies = new LobbyTable();
		// Open a server socket:
		ServerSocket serverSocket = null;
		// Port number to connect through to.
		int portNumber;
		InetAddress listenAddress;
		// If number of arguments does not match expected, provide correct
		// usage.
		if (args.length != 2) {
			throw new IllegalArgumentException("Usage: java Server portNumber");
		} else {
			// Parse passed string to an Integer for port number.
			portNumber = Integer.parseInt(args[0]);
			try {
				listenAddress = InetAddress.getByName(args[1]);
			} catch (UnknownHostException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		// We must try because it may fail with a checked exception:
		try {
			// Open server socket
			serverSocket = new ServerSocket(portNumber, 1, listenAddress);
		} catch (IOException e) {
			System.err.println("Couldn't listen on port " + portNumber);
			System.exit(1); // Exit.
		}
		// Good. We succeeded. But we must try again for the same reason:
		boolean isRunning = true;
		
		// We start a new UDP server receiver to receive all UDP messages.
		UDPServer udpReceiver = new UDPServer(clientTable, gameLobbies);
		udpReceiver.start();
		while (isRunning) {
			try {
				// We loop for ever, as servers usually do, we can exit by
				// typing Exit into command line though.
				// Server input stream.
				BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
				// Creates a thread which looks for messages of a certain format
				// and acts accordingly if they match.
				ServerExitListener listener = new ServerExitListener(input);
				listener.start();
				
				while (true && listener.isAlive()) {
					// Listen to the socket, accepting connections from new
					// clients:
					Socket socket = serverSocket.accept();
					listener.addSocket(socket);
					// This is so that we can use readLine():
					BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					// We ask the client what its name is:
					String clientName = fromClient.readLine();
					PrintStream toClient = new PrintStream(socket.getOutputStream());
					String text = "";
					int clientID;
					// For debugging:
					gui.addMessage(clientName + " connected");
					
					// We add the client to the table. Returns a unique client
					// id
					clientID = clientTable.add(clientName);
					// We create and start a new thread to write to the client:
					ServerSender sender = new ServerSender(clientTable.getQueue(clientID), toClient, socket, clientName, clientID);
					sender.start();
					
					// We start a new UDP server sender to send messages to a client.
					// UDPServerSender udpSender = new UDPServerSender(clientTable.getUDPqueue(clientID));
					
					// We create and start a new thread to read from the client:
					ServerReceiver reciever = new ServerReceiver(clientID, fromClient, clientTable, sender, gameLobbies, udpReceiver);
					reciever.start();
			
					// For debugging
					text = "UserID is:" + clientID;
					gui.addMessage(text);
					// Sends a message to the client detailing their unique user
					// id.
					Message msg = new Message(text);
					MessageQueue recipientsQueue = clientTable.getQueue(clientID);
					recipientsQueue.offer(msg);
				}
			// Catch some possible errors - IO.
			} catch (IOException e) {
				System.err.println("IO error " + e.getMessage() + ". Attempting to re-establish...");
				try {
					serverSocket = new ServerSocket(portNumber);
					gui.addMessage("Connection re-established.");
				} catch (IOException f) {
					System.err.println("Couldn't listen on port " + portNumber + ". Giving up.");
					System.exit(1); // Give up.
				}
			}
		}
	}
}