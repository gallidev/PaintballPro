package networkingClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import networkingInterfaces.ClientPlayer;
import networkingSharedStuff.Message;
import networkingSharedStuff.MessageQueue;


// Gets messages from client and puts them in a queue, for another
// thread to forward to the appropriate client.

/**
 * Class to get messages from client, process and put appropriate message for a client.
 */
public class ClientReceiver extends Thread {
	
	private int clientID;
	private BufferedReader fromServer;
	private ClientSender sender;
	private MessageQueue myMsgQueue;
	private Message msg;
	
	/**
	 * Construct the class, setting passed variables to local objects.
	 * @param clientID The ID of the client.
	 * @param reader Input stream reader for data.
	 * @param table Table storing client information.
	 * @param sender Sender class for sending messages to the client.
	 */
	public ClientReceiver(int Cid, BufferedReader reader, ClientSender sender, MessageQueue msgQueue)
	{
		clientID = Cid;
		fromServer = reader;
		this.sender = sender;
		myMsgQueue = msgQueue;
	}
	
	/**
	 * The main method running in this class, runs when the class is started after initialization.
	 */
	public void run() {
		try {
			while (true) {
				//Get input from the client read stream.
				String text = fromServer.readLine();
				
				//If text isn't null and does not read "Exit:Client" do...
				if(text != null && text.compareTo("Exit:Client") != 0){
					
					//    Protocols
					if(text.contains("GameStart"))
					{
						ClientPlayer cPlayer = new ClientPlayer(sender,this); // Using 'this' is ugly code but currently can't think of another way.
						// Do stuff here.
					}
					
				}
				else // if the client wants to exit the system. 
				{
					sender.stopThread();
					return;
				}
			}
		}
		catch (IOException e) {
			//If there is something wrong... exit cleanly.
			sender.stopThread();
			return;
		}
	}
}
