package networkingClient;



import java.io.PrintStream;

/**
 * Class to send messages from the client to other clients via the server.
 */
public class ClientSender extends Thread {

	private String nickname;
	private PrintStream server;
	private String text = ""; //Text to send to the server from the client.
	private boolean m_running = true;
	
	/**
	 * Constructs the class, setting variables used.
	 * @param nickname Nickname of the connected client.
	 * @param server Stream information to send to the server.
	 */
	public ClientSender(String nickname, PrintStream server) {
		this.nickname = nickname;
		this.server = server;
	}

	/**
	 * Set text that will be sent to the server from the client.
	 * @param text The text to send.
	 */
	public void sendMessage(String text)
	{
		this.text = text;
	}

	/**
	 * Sets the variable is_running to false. Will stop the thread from running its loop in run().
	 */
	public void stopThread()
	{
		m_running = false;
	}
	
	/**
	 * The main method running in this class, runs when the class is started after initialization.
	 */
	public void run() {
		// Tell the server what the client's nickname is:
		server.println(nickname);

		//Loop until a variable, m_running, set to false. Sending messages to server for handling.
		while (m_running) {
			//If text contains more then an empty string - "".
			if(text.compareTo("") != 0)
			{
				//If text is normal and not "Exit:Client".
				if(text.compareTo("Exit:Client") != 0)
				{
					server.println(text);
					text = "";
				}
				else
				{
					//Stop this thread from running with 'return'.
					return;
				}
			}
			//Sleep and wait to give other processes time.
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				System.out.println("ClientSender interrupted while sleeping");
			}
		}
	}
}

