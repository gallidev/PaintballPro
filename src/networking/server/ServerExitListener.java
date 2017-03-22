package networking.server;

import java.util.ArrayList;

import gui.AlertBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

/**
 * Class to wait for an Exit input in the command line of the server and act
 * accordingly.
 * 
 * @author Matthew Walters
 */
public class ServerExitListener extends Thread {

	private ArrayList<Socket> sockets;
	private BufferedReader myClient;

	/**
	 * Constructs the class, assigning passed variables and creating a new array
	 * to store sockets.
	 * 
	 * @param clientReader
	 *            Reader of string input on the command line of Server.
	 */
	public ServerExitListener(BufferedReader clientReader) {
		myClient = clientReader;
		sockets = new ArrayList<Socket>();
	}

	/**
	 * The main method running in this class, runs when the class is started
	 * after initialisation.
	 */
	public void run() {
		try {
			// Doesn't need to stop.
			while (true) {
				// If client types 'Exit' onto server command line...
				if (myClient.readLine().compareTo("Exit") == 0) {
					stopServer();
				}
				// If typed input does not match expected
				else {
					(new AlertBox("Unrecognised Command",
							"You attempted an unrecognised command, please press the Exit button or type Exit in the terminal."))
									.showAlert();
				}
				// Give other things a chance to run.
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					//
				}
			}
		} catch (IOException e) {
			(new AlertBox("Error", "There was an error with the server.")).showAlert();
		}
	}

	/**
	 * Adds a passed client Socket to an ArrayList of Socket objects.
	 * 
	 * @param socket
	 *            A socket that a client has connected through.
	 */
	public void addSocket(Socket socket) {
		sockets.add(socket);
	}

	/**
	 * Stops the Server from running.
	 */
	public void stopServer() {
		// Cycle through all sockets...
		for (int i = 0; i < sockets.size(); i++) {
			// If socket isn't already closed, close.
			if (!sockets.get(i).isClosed()) {
				try {
					sockets.get(i).close();
				} catch (IOException e) {
					(new AlertBox("Error", "There was an error while closing the client.")).showAlert();
				}
			}

		}
		// Give everything enough time to close.
		try {
			sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		(new AlertBox("Notification", "All clients were disconnected safely, closing nowa.")).showAlert();
		// Exit.
		System.exit(0);
	}
}