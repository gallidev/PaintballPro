package networkingClient;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Class to represent a connecting client to a running server.
 */
public class Client {
	/**
	 * Main method to run when the client starts, sets up connections and runs appropriate GUI.
	 * @param args Command line arguments for use in the method.
	 */
	public static void main(String[] args) {
		// Check correct usage:
		if (args.length != 3) {
			throw new IllegalArgumentException
			("Usage: java Client <nickname> <port number> <server machine name>");
		}

		//Sets client nickname from the command line argument.
		String nickname = args[0];

		//If nickname does not contain a : - if it did program wouldnt work as : is used for a separator.
		if(!nickname.contains(":"))
		{
			int portNumber = Integer.parseInt(args[1]);
			String hostname = args[2];

			// Open sockets:
			PrintStream toServer = null;
			BufferedReader fromServer = null;
			Socket server = null;

			try {
				//Connect to server
				server = new Socket(hostname, portNumber);
				//Get output and input streams from the server.
				toServer = new PrintStream(server.getOutputStream());
				fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
			} 
			//If host cannot be found
			catch (UnknownHostException e) {
				System.err.println("Unknown host: " + hostname);
				System.exit(1); //Exit
			} 
			//If server isn't running.
			catch (IOException e) { 
				System.err.println("The server doesn't seem to be running " + e.getMessage());
				System.exit(1); //Exit
			}

			// Create two client threads, one for sending and one for recieving messages:
			ClientSender sender = new ClientSender(nickname,toServer);
			ClientReceiver receiver = new ClientReceiver(fromServer,sender);

			// Run them in parallel:
			sender.start();
			receiver.start();

			//Give time for nickname to be sent to server and appropriate response to be received.
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			String requestStr;
			int clientID = 0;
			
			//Get messages to this client and look for a response in a specific format.
			for(int i = 0; i < receiver.messages.size(); i++)
			{
				if((requestStr = receiver.messages.get(i)).contains("UserID is:"))
				{
					//Set client id as returned value.
					clientID = Integer.parseInt(requestStr.substring(10));
					break;
				}
			}
			
			System.out.println("Client has id:"+clientID);
			ClientGUI CGUI = new ClientGUI();
			//CGUI.loadClient(clientID,nickname,sender,receiver);

			// Wait for them to end and close sockets.
			try {
				System.out.println("Client Started");
				sender.join(); //Wait for sender to close
				toServer.close(); //Close connection to server
				receiver.stopThread(); //Stop receiver
				receiver.join(); //Wait for receiver to stop
				fromServer.close(); //Close connection from server
				server.close(); //Close server socket
				//CGUI.stopThreads(); //Stops the threads and GUI from running.
				System.out.println("Client has been stopped."); //Acknowledge to the client that everything has been stopped.
			}
			catch (IOException e) {
				System.err.println("Something wrong " + e.getMessage());
				System.exit(1); // Give up.
			}
			catch (InterruptedException e) {
				System.err.println("Unexpected interruption " + e.getMessage());
				System.exit(1); // Give up.
			}
		}
		//If username contains the character : (used for a string information separator so cannot be in a nickname.
		else
		{
			System.out.println("Error: Username cannot contain character ':', please change it.");
			System.exit(1);
		}
	}
}