package networkingClient;

import java.io.PrintStream;

import networkingShared.Message;
import networkingShared.MessageQueue;

// Continuously reads from message queue for a particular client,
// forwarding to the client.

/**
 * Class to send messages to a client.
 */
public class ClientSender extends Thread {

	private MessageQueue queue;
	private PrintStream server;
	private boolean m_running = true;
	private String clientNickname;

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

	public MessageQueue getQueue() {
		return queue;
	}
}
