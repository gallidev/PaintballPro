package networking.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import gui.AlertBox;
import gui.ServerGUI;
import javafx.application.Platform;
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
	public ServerSocket serverSocket;

	private ArrayList<Socket> sockets;
	private boolean debug = false;
	private InetAddress listenAddress;
	private int portNumber;	
	private int testing = 0;
	private ServerGUI gui;
	
	public Server(int portNumber, String host, ServerGUI gui, int testing) {
		this.testing = testing;
		try {
			this.portNumber = portNumber;
			this.listenAddress = InetAddress.getByName(host);
			this.gui = gui;
		} catch (UnknownHostException e) {
			if (testing == 0) {
				(new AlertBox("Connection Failed", "Unknown host.")).showAlert(true);
				System.exit(1);
			} else
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
		serverSocket = null;
		sockets = new ArrayList<Socket>();

		// We must try because it may fail with a checked exception:
		try {

			if(debug)
				System.out.println("Running server on:" + listenAddress.getHostAddress() + " on port:" + portNumber);

			// Open server socket
			serverSocket = new ServerSocket(portNumber, 1, listenAddress);

			// We start a new UDP server receiver to receive all UDP messages.
			UDPServer udpServer = null;
			udpServer = new UDPServer(clientTable, gameLobbies, 19857);
			udpServer.start();

			while (isRunning) {
				try {
					// We loop for-ever, as servers usually do, we can exit by
					// typing Exit into command line though.
					while (!isInterrupted()) {

						if (testing == 2)
							throw new IOException();
						
						Socket socket;
						BufferedReader fromClient;
						String clientName;
						PrintStream toClient;
						boolean usernameAvailable;
						
						// Listen to the socket, accepting connections from new
						// clients:
						socket = serverSocket.accept();
						sockets.add(socket);
						
						// This is so that we can use readLine():
						fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						
						// We ask the client what its name is:
						clientName = fromClient.readLine();
						toClient = new PrintStream(socket.getOutputStream());
 
						usernameAvailable = clientTable.checkUsernameAvailable(clientName);

						if (usernameAvailable) {
							
							String text = "";
							int clientID;
							ServerSender sender;
							ServerReceiver receiver;
							Message msg;
							MessageQueue recipientsQueue;

							// For debugging:
							if (testing == 0)
								gui.addMessage(clientName + " connected");

							// We add the client to the table. Returns a unique
							// client
							// id
							clientID = clientTable.add(clientName);
							
							// We create and start a new thread to write to the
							// client:
							sender = new ServerSender(clientTable.getQueue(clientID), toClient, socket,
									clientName, clientID);
							sender.start();

							// We create and start a new thread to read from the
							// client:
							receiver = new ServerReceiver(clientID, fromClient, clientTable, sender,
									gameLobbies, udpServer, singlePlayer);
							receiver.start();

							// For debugging
							text = "UserID is:" + clientID;
							if (testing == 0)
								gui.addMessage(text);

							if (singlePlayer)
								singlePlayerIntegration();

							// Sends a message to the client detailing their
							// unique user id.
							msg = new Message(text);
							recipientsQueue = clientTable.getQueue(clientID);
							recipientsQueue.offer(msg);
						} else {
							toClient.write("UsernameInUse".getBytes());
							toClient.close();
							fromClient.close();
						}
					}
					udpServer.interrupt();
					// Catch some possible errors - IO.
				} catch (IOException e) {
					if (testing == 0) {
						//(new AlertBox("Connection Failed", "Couldn't listen on port " + portNumber)).showAlert(true);
					} else {
						//System.out.println("3 Couldn't listen on port " + portNumber);
						exceptionCheck = 3;
					}
					
					for (int i = 0; i < sockets.size(); i++) {
						// If socket isn't already closed, close.
						if (!sockets.get(i).isClosed()) {
							try {
								sockets.get(i).close();
							} catch (IOException f) {
								(new AlertBox("Error", "There was an error while closing the client.")).showAlert(true);
							}
						}
					}
					isRunning = false;
					udpServer.interrupt();
					
					if(testing == 0)
						System.exit(1);
				}
			}
			udpServer.interrupt();

		} catch (IOException e) {
			// e.printStackTrace();
			if (testing == 0) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						(new AlertBox("Connection Failed", "Couldn't listen on port " + portNumber)).showAlert(true);
					}
				});
			} else {
				//System.out.println("2 Couldn't listen on port " + portNumber);
				exceptionCheck = 2;
			}
			
			for (int i = 0; i < sockets.size(); i++) {
				// If socket isn't already closed, close.
				if (!sockets.get(i).isClosed()) {
					try {
						sockets.get(i).close();
					} catch (IOException f) {
						(new AlertBox("Error", "There was an error while closing the client.")).showAlert(true);
					}
				}
			}
			if(testing == 0)
				System.exit(1);
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

	/**
	 * Are we in Single Player mode?
	 * @param setSinglePlayer Whether or not we are in Single Player mode.
	 * 
	 * @author Alexandra Paduraru
	 */
	public void setSinglePlayer(boolean setSinglePlayer) {
		singlePlayer = setSinglePlayer;
	}
}