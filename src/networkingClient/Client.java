package networkingClient;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import networkingSharedStuff.Message;
import networkingSharedStuff.MessageQueue;


/**
 * Class to represent a connecting client to a running server.
 */
public class Client {
	ClientSender sender;
	ClientReceiver receiver;
	int clientID;
	
	/**
	 * Constructor method to run when the client starts, sets up connections and runs appropriate GUI.
	 * @param args Command line arguments for use in the method.
	 */
	public Client(String passedNickname, int portNum, String machName) {
		//Sets client nickname from the command line argument.
		String nickname = passedNickname;

		//If nickname does not contain a : - if it did program wouldnt work as : is used for a separator.
		if(!nickname.contains(":"))
		{
			int portNumber = portNum;
			String hostname = machName;

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

			// Create two client threads, one for sending and one for receiving messages:
			MessageQueue msgQueue = new MessageQueue();
			sender = new ClientSender(msgQueue,toServer,nickname);
			receiver = new ClientReceiver(portNum, fromServer, sender, msgQueue);

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
			clientID = 0;
			
			//Get messages to this client and look for a response in a specific format.
			boolean found = false;
			while(!found)
			{
				Message msg = msgQueue.take();
				String text  = msg.getText();
				if(text.contains("UserID is:"))
				{
					//Set client id as returned value.
					clientID = Integer.parseInt(text.substring(10));
					found = true;
				}
				else
					msgQueue.offer(msg);
			}
			
			System.out.println("Client has id:"+clientID);
			//ClientGUI CGUI = new ClientGUI();
			//CGUI.loadClient(clientID,nickname,sender,receiver);

			// Wait for them to endnd close sockets.
			try {
				System.out.println("Client Started");
				sender.join(); //Wait for sender to close
				toServer.close(); //Close connection to server
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
	
	public int getClientID()
	{
		return clientID;
	}
	
	public ClientSender getSender()
	{
		return sender;
	}
	public ClientReceiver getReceiver()
	{
		return receiver;
	}
}