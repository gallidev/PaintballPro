package networking.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import gui.AlertBox;
import gui.ServerView;
import networking.game.UDPServer;
import networking.shared.Message;
import networking.shared.MessageQueue;

/**
 * Class to represent a running server that connects to multiple clients via
 * sockets.
 * 
 * @author Matthew Walters
 */
public class Server extends Thread {
	
	static boolean singlePlayer;
	
	public boolean isRunning = true;
	public int exceptionCheck = 0;
	
	private int portNumber;
	private InetAddress listenAddress;
	private ServerView gui;
	private ServerExitListener exitListener;
	private int testing = 0;
	
	public Server(int portNumber, String host, ServerView gui, int testing)
	{
		this.testing = testing;
		try {
			this.portNumber = portNumber;
			this.listenAddress = InetAddress.getByName(host);
			this.gui = gui;
		} catch (UnknownHostException e) {
			if(testing == 0) 
			{
				AlertBox.showAlert("Connection Failed","Unknown host.");
				System.exit(1);
			}
			else
				exceptionCheck = 1;
		}
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
			
			// We start a new UDP server receiver to receive all UDP messages.
			UDPServer udpServer = null;
			udpServer = new UDPServer(clientTable, gameLobbies,19876);
			udpServer.start();

			while (isRunning) {
				try {
					// We loop for-ever, as servers usually do, we can exit by
					// typing Exit into command line though.
					
					// Server input stream.
					BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
					// Creates a thread which looks for messages of a certain format
					// and acts accordingly if they match.
					exitListener = new ServerExitListener(input);
					exitListener.start();

					while (!isInterrupted() && exitListener.isAlive()) {
						
						if(testing == 2)
							throw new IOException();
						
						// Listen to the socket, accepting connections from new
						// clients:
						Socket socket = serverSocket.accept();
						exitListener.addSocket(socket);
						// This is so that we can use readLine():
						BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						// We ask the client what its name is:
						String clientName = fromClient.readLine();
						PrintStream toClient = new PrintStream(socket.getOutputStream());
						String text = "";
						int clientID;
						
						// For debugging:
						if(testing == 0) gui.addMessage(clientName + " connected");

						// We add the client to the table. Returns a unique client
						// id
						clientID = clientTable.add(clientName);
						// We create and start a new thread to write to the client:
						ServerSender sender = new ServerSender(clientTable.getQueue(clientID), toClient, socket, clientName, clientID);
						sender.start();

						// We create and start a new thread to read from the client:
						ServerReceiver reciever = new ServerReceiver(clientID, fromClient, clientTable, sender, gameLobbies, udpServer, singlePlayer);
						reciever.start();

						// For debugging
						text = "UserID is:" + clientID;
						if(testing == 0) gui.addMessage(text);

						if (singlePlayer)
							singlePlayerIntegration();

						// Sends a message to the client detailing their unique user id.
						Message msg = new Message(text);
						MessageQueue recipientsQueue = clientTable.getQueue(clientID);
						recipientsQueue.offer(msg);
					}
					udpServer.interrupt();
				// Catch some possible errors - IO.
				} catch (IOException e) {
					if(testing == 0) 
					{
						AlertBox.showAlert("Connection Failed","Couldn't listen on port "+portNumber);
						System.exit(1);
					}
					else
					{
						System.out.println("3 Couldn't listen on port "+portNumber);
						exceptionCheck = 3;
					}
				}
			}
			udpServer.interrupt();
			
		} catch (IOException e) {
			if(testing == 0) 
			{
				AlertBox.showAlert("Connection Failed","Couldn't listen on port "+portNumber);
				System.exit(1);
			}
			else
			{
				System.out.println("2 Couldn't listen on port "+portNumber);
				exceptionCheck = 2;
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

	public ServerExitListener getExitListener() {
		return exitListener;
	}
}