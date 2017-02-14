package networkingServer;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import networkingSharedStuff.Message;
import networkingSharedStuff.MessageQueue;

// Continuously reads from message queue for a particular client,
// forwarding to the client.

/**
 * Class to send messages to a client.
 */
public class ServerMsgSender extends Thread {
	
	private MessageQueue queue;
	private PrintStream client;
	private boolean m_running = true;
	private Socket socket;
	private int clientID;
	private String clientName;
	
	/**
	 * Construct the class, setting passed variables to local objects.
	 * @param queue Message queue for a client.
	 * @param clientStream Stream to print out to the client.
	 * @param socket Socket to the client.
	 * @param clientName Name of the client.
	 * @param ClientID ID of the client.
	 */
	public ServerMsgSender(MessageQueue queue, PrintStream clientStream, Socket socket, String clientName, int ClientID) {
		this.queue = queue;   
		this.client = clientStream;
		this.socket = socket;
		this.clientName = clientName;
		this.clientID = ClientID;
	}
	
	/**
	 * Sets the global variable is_running to false. Will stop the thread from running its loop in run().
	 */
	public void stopThread()
	{
		m_running = false;
	}

	/**
	 * The main method running in this class, runs when the class is started after initialisation.
	 */
	public void run() {
		while (m_running) {
			//Get messages from the message queue.
			Message msg = queue.take();
			//System.out.println("Sending:" + msg.getText());
			//Print to the client stream.
			client.println(msg.getText());
		}
		//If stopped, close the client stream.
		client.close();
		try {
			//Attempt to close the socket to the client.
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(clientID + " " + clientName + " disconnected.");
		return;
	}
}
