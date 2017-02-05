package networkingServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;

import networkingSharedStuff.Message;
import networkingSharedStuff.MessageQueue;

/**
 * Class to represent a running server that connects to multiple clients via sockets.
 */
public class Server {
	/**
	 * Main implementation method, handles connecting clients.
	 * @param args Command line arguments passed through from the user.
	 */
	public static void main(String [] args) {

		// This will be shared by the server threads:
		ClientTable clientTable = new ClientTable();

		// Open a server socket:
		ServerSocket serverSocket = null;

		//Port number
		int portNumber;

		//If number of arguments does not match expected, provide correct usage.
		if(args.length != 1)
		{
			throw new IllegalArgumentException
			("Usage: java Server portNumber");
		}
		else
		{
			//Parse passed string to an Integer for port number.
			portNumber = Integer.parseInt(args[0]);
		}

		// We must try because it may fail with a checked exception:
		try {
			//Open server socket
			serverSocket = new ServerSocket(portNumber);
		} 
		catch (IOException e) {
			System.err.println("Couldn't listen on port " + portNumber);
			System.exit(1); //Exit.
		}

		// Good. We succeeded. But we must try again for the same reason:
		
		boolean isRunning = true;
		
		while(isRunning)
		{
			try { 
				// We loop for ever, as servers usually do, we can exit by typing Exit into command line though.
				
				//Server input stream.
				BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
				//Creates a thread which looks for messages of a certain format and acts accordingly if they match.
				ServerExitListener listener = new ServerExitListener(input);
				listener.start();
				
				while (true && listener.isAlive()) {
					
						// Listen to the socket, accepting connections from new clients:
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
						System.out.println(clientName + " connected");
		
						// We add the client to the table. Returns a unique client id.
						clientID = clientTable.add(clientName);
		
						// We create and start a new thread to write to the client:
						ServerMsgSender sender = new ServerMsgSender(clientTable.getQueue(clientID), toClient,socket,clientName,clientID);
						sender.start();
		
						// We create and start a new thread to read from the client:
						ServerMsgReceiver reciever = new ServerMsgReceiver(clientID, fromClient, clientTable,sender);
						reciever.start();
		
						//For debugging
						text = "UserID is:" + clientID;
						System.out.println(text);
						
						//Sends a message to the client detailing their unique user id.
						Message msg = new Message(text);
						MessageQueue recipientsQueue = clientTable.getQueue(clientID);
						recipientsQueue.offer(msg);
				}
			} 
			catch (IOException e) {
				System.err.println("IO error " + e.getMessage() + ". Attempting to re-establish...");
				try {
					serverSocket = new ServerSocket(portNumber);
					System.out.println("Connection re-established.");
				} 
				catch (IOException f) {
					System.err.println("Couldn't listen on port " + portNumber + ". Giving up.");
					System.exit(1); // Give up.
				}
			}
		}
	}
}