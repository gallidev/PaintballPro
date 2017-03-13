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
public class Server extends Thread {
	
	
	static boolean singlePlayer;
	
	public boolean isRunning = true;
	
	private int portNumber;
	private InetAddress listenAddress;
	private ServerView gui;
	
	public boolean testing = false;
	
	public Server(int portNumber, String host, ServerView gui)
	{
		this.portNumber = portNumber;
		try {
			this.listenAddress = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			//
		}
		this.gui = gui;
	}
	
	/**
	 * Main implementation method, handles connecting clients.
	 */
	public void run() {
		
		// This will be shared by the server threads:
		ClientTable clientTable = new ClientTable();
		// Create a new lobby instance.
		LobbyTable gameLobbies = new LobbyTable();
		// Open a server socket:
		ServerSocket serverSocket = null;

		// We must try because it may fail with a checked exception:
		try {
			// Open server socket
			serverSocket = new ServerSocket(portNumber, 1, listenAddress);
		} catch (IOException e) {
			System.err.println("Couldn't listen on port " + portNumber);
			System.exit(1); // Exit.
		}
		// Good. We succeeded. But we must try again for the same reason:
		
		// We start a new UDP server receiver to receive all UDP messages.
		UDPServer udpReceiver = new UDPServer(clientTable, gameLobbies,19876);
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
				
				while (!isInterrupted() && listener.isAlive()) {
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
					//gui.addMessage(clientName + " connected");
					
					// We add the client to the table. Returns a unique client
					// id
					clientID = clientTable.add(clientName);
					// We create and start a new thread to write to the client:
					ServerSender sender = new ServerSender(clientTable.getQueue(clientID), toClient, socket, clientName, clientID);
					sender.start();
					
					// We start a new UDP server sender to send messages to a client.
					// UDPServerSender udpSender = new UDPServerSender(clientTable.getUDPqueue(clientID));
					
					// We create and start a new thread to read from the client:
					ServerReceiver reciever = new ServerReceiver(clientID, fromClient, clientTable, sender, gameLobbies, udpReceiver, singlePlayer);
					reciever.start();
			
					// For debugging
					text = "UserID is:" + clientID;
					if(!testing) gui.addMessage(text);
					
					if (singlePlayer)
						singlePlayerIntegration();
					
					// Sends a message to the client detailing their unique user
					// id.
					Message msg = new Message(text);
					MessageQueue recipientsQueue = clientTable.getQueue(clientID);
					recipientsQueue.offer(msg);
				}
				udpReceiver.interrupt();
			// Catch some possible errors - IO.
			} catch (IOException e) {
				System.err.println("IO error " + e.getMessage() + ". Attempting to re-establish...");
				try {
					serverSocket = new ServerSocket(portNumber);
					if(!testing) gui.addMessage("Connection re-established.");
				} catch (IOException f) {
					System.err.println("Couldn't listen on port " + portNumber + ". Giving up.");
					System.exit(1); // Give up.
				}
			}
		}
	}
	
	/**
	 * Method to start a game in the single player mode. ie The Server is local
	 * 
	 * @author Alexandra Paduraru
	 */
	private static void singlePlayerIntegration() {
		System.out.println("Local server starts game ... ");
		
		
	}
	
	public  void setSinglePlayer(boolean b){
		singlePlayer = b;
	}
}