package networkingOld;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class to retrieve message from the other clients via the server (from ServerSender thread).
 */
public class ClientReceiver extends Thread {

	private BufferedReader server;
	public ArrayList<String> messages;
	private ClientSender sender;
	private boolean m_running = true;

	/**
	 * Constructs the class, setting variables used.
	 * @param server Stream information from the server.
	 * @param sender The client thread to send messages to the server.
	 */
	public ClientReceiver(BufferedReader server, ClientSender sender) {
		this.server = server;
		this.sender = sender;
	}

	/**
	 * Remove messages from the message ArrayList which stores retrieved messages.
	 * @param start Start index of message removal.
	 * @param finish End index of message removal.
	 */
	public void removeMessages(int start,int finish)
	{
		for(int i = finish; i >= start; i--)
		{
			messages.remove(i);
		}
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
		// Print to the user whatever we get from the server:
		try {
			messages = new ArrayList<String>();
			while (m_running) {
				String s = server.readLine();
				if (s != null)
				{
					if (s.compareTo("Exit:Client") != 0)
					{
						messages.add(s);
					}
					else
					{
						System.out.println("Got exit message");
						//stopThread();
						return;
					}
				}
				else 
				{
					server.close(); // Probably no point.
					throw new IOException("Got null from server"); // Caught below.
				}
			}
		}
		catch (IOException e) {
			System.out.println("Everything has stopped on server.");
			sender.sendMessage("Exit:Client");
			return;
		}
	}
}
