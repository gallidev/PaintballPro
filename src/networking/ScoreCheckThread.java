package networking;


/**
 * Periodically updates the information in the score table - the current client(s) connected scores.
 */
public class ScoreCheckThread extends Thread {
	
	private ScoreBoard scoreboard;
	private boolean is_running = true;
	
	/**
	 * Constructor - sets passed ScoreBoard variable.
	 * @param scoreboard
	 */
	public ScoreCheckThread(ScoreBoard scoreboard)
	{
		this.scoreboard = scoreboard;
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
			//Update the table of scores for the clients currently connected to the server.
			scoreboard.updateText();
			
			try {
				//Update every 30 seconds, if lots of clients this reduces the amount of time and processing it regularly takes.
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				//System.err.println("Interrupted while sleeping.");
				return;
			}
		}
	}
}
