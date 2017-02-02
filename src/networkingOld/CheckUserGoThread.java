package networkingOld;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


/**
 * Thread to monitor retrieved messages from the server to find messages matching specific formats from the opponent client.
 */
public class CheckUserGoThread extends Thread {

	private NoughtsCrossesModel model;
	private ClientReceiver reciever;
	private JFrame frame;

	/**
	 * Takes the passed variables and adds to the local variables for use in other methods within this thread.
	 * @param model The model of a tic-tac-toe game.
	 * @param reciever The class which handles messages received from the Server.
	 * @param frame The GUI display JFrame
	 */
	public CheckUserGoThread(NoughtsCrossesModel model,ClientReceiver reciever,JFrame frame)
	{
		this.model = model;
		this.reciever = reciever;
		this.frame = frame;
	}

	public boolean m_running = true;

	/**
	 * The main method running in this class, runs when the class is started after initialization.
	 */
	public void run()
	{
		String requestStr;
		String[] requestVals = new String[5]; //String sent consists of 5 pieces of information, with a separator between (:).
		
		while(m_running)
		{
			try{
				//Cycle through all messages for the client.
				for(int i = 0; i < reciever.messages.size(); i++)
				{
					//If string contains a specific string representing a move...
					if((requestStr = reciever.messages.get(i)).contains("Move:"))
					{
						reciever.removeMessages(i, i); //Remove the message so that it is not picked up again.
						requestVals = requestStr.split(":"); //Information is split by :.
						model.otherPlayerTurn(Integer.parseInt(requestVals[2]), Integer.parseInt(requestVals[3]), Integer.parseInt(requestVals[4])); //Make the other player's turn.
						break; //Stop looking for move messages.
					}
					//If string contains a specific string representing an exit...
					else if((requestStr = reciever.messages.get(i)).contains("EXIT:GAME"))
					{
						reciever.removeMessages(i, i); // Remove the message so that it is not picked up again.
						JOptionPane.showMessageDialog(null, "Sorry, the other player has exited the game."); //Message box giving an error as the player has left.
						model.blankBoard(); //Set game board to blank.
						m_running = false; //Stop running this thread after these commands have taken place.
						frame.dispose(); //Dispose frame.
						break; //Break from FOR loop as nothing else needs to be checked.
					}
				}
			}
			//If messages contains no messages.
			catch(NullPointerException e)
			{
				System.out.println("Tried accessing empty list");
			}
			//Sleep to give other processes a chance to run.
			try {
				sleep(50);
			} catch (InterruptedException e) {
				System.out.println("I got interrupted while sleeping");
			}
		}
	}
}