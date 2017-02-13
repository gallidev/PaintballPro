package networkingClient;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import gui.GUIManager;
import networkingSharedStuff.Message;
import networkingSharedStuff.MessageQueue;


/**
 * Class to represent a connecting client to a running server.
 */
public class Client {
	ClientSender sender;
	ClientReceiver receiver;
	int clientID;
	PrintStream toServer = null;
	BufferedReader fromServer = null;
	Socket server = null;
	
	/**
	 * Constructor method to run when the client starts, sets up connections and runs appropriate GUI.
	 */
	public Client(String passedNickname, int portNum, String machName, GUIManager m) {
		//Sets client nickname from the command line argument.
		String nickname = passedNickname;

		//If nickname does not contain a : - if it did program wouldnt work as : is used for a separator.
		if(!nickname.contains(":") || !nickname.contains("-"))
		{
			int portNumber = portNum;
			String hostname = machName;

			// Open sockets:


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

			// Run them in parallel:
			sender.start();

			String requestStr;
			clientID = 0;
			
			//Get messages to this client and look for a response in a specific format.
			boolean found = false;
			while(!found)
			{
				String text = "";
				try {
					text = fromServer.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(text.contains("UserID is:"))
				{
					//Set client id as returned value.
					clientID = Integer.parseInt(text.substring(10));
					found = true;
				}
			}
			
			System.out.println("Client has id:"+clientID);
			
			receiver = new ClientReceiver(clientID, fromServer, sender, msgQueue, m);
			receiver.start();
			
			//ClientGUI CGUI = new ClientGUI();
			//CGUI.loadClient(clientID,nickname,sender,receiver);

			// Wait for them to endnd close sockets.

			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
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
			});
			t.start();

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