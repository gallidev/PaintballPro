package networking.client;

import java.io.PrintStream;

import networking.shared.Message;
import networking.shared.MessageQueue;

// Continuously reads from message queue for a particular client,
// forwarding to the client.
/**
 * Class to send messages to a client.
 * 
 * @author Matthew Walters
 */
public class ClientSender extends Thread {

	private MessageQueue queue;
	private PrintStream server;
	private boolean m_running = true;
	private String clientNickname;

	/**
	 * Constructor of client sender thread. We set global variables passed in.
	 * @param queue Message queue for client.
	 * @param serverStream Stream to server.
	 * @param nickname Nickname of client.
	 */
	public ClientSender(MessageQueue queue, PrintStream serverStream, String nickname) {
		this.queue = queue;
		this.server = serverStream;
		this.clientNickname = nickname;
	}

	/**
	 * Sets the global variable is_running to false. Will stop the thread from
	 * running its loop in run().
	 */
	public void stopThread() {
		m_running = false;
	}

	/**
	 * Places a message to send to the server.
	 * @param text Message to send to the server.
	 */
	public void sendMessage(String text) {
		queue.offer(new Message(text));
	}

	/**
	 * The main method running in this class, runs when the class is started
	 * after initialisation.
	 */
	public void run() {
		server.println(clientNickname);
		while (m_running) {
			// Get messages from the message queue.
			Message msg = queue.take();
			String text = msg.getText();
			// Print to the client stream.
			server.println(text);
		}
		// If stopped, close the server stream.
		server.close();
		return;
	}

	/**
	 * We return the message queue of the client.
	 * @return Client's message queue.
	 */
	public MessageQueue getQueue() {
		return queue;
	}
}
