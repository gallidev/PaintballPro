package networkingOld;


/**
 * Periodically updates the information in the lobby table - the current client(s) connected.
 */
public class LobbyCheckThread extends Thread {
	
	private ClientLobby lobby;
	private boolean is_running = true;
	
	/**
	 * Initializes passed ClientLobby variable.
	 * @param lobby Class representing the list of clients connected to the Server.
	 */
	public LobbyCheckThread(ClientLobby lobby)
	{
		this.lobby = lobby;
	}
	
	/**
	 * Sets the variable is_running to false. Will stop the thread from running its loop in run().
	 */
	public void stopThread()
	{
		is_running = false;
	}
	
	/**
	 * The main method running in this class, runs when the class is started after initialization.
	 */
	public void run()
	{
		//Does not stop until client quits... this stops everything and therefore also this thread.
		while(is_running)
		{
			//Update the table of clients currently connected to the server.
			lobby.updateText();
			
			try {
				//Update every 30 seconds, if lots of clients this reduces the amount of time and processing it regularly takes.
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				//System.err.println("Interrupted while sleeping");
				return;
			}
		}
	}
	
}
