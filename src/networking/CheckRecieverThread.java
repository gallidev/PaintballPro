package networking;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Thread class for checking ClientReceiver thread for specific messages and acting accordingly.
 */
public class CheckRecieverThread extends Thread {
	
	private ClientSender sender;
	private ClientReceiver reciever;
	private String clientName;
	private int clientID;
	private int playerNumber = 1;
	private boolean is_running = true;
	
	/**
	 * Constructor for the class, sets variables.
	 * @param sender The class which sends messages to the Server.
	 * @param reciever The class which handles messages received from the Server.
	 * @param frame The GUI display JFrame.
	 * @param clientName The nickname of the Client.
	 * @param clientID The ID of the Client.
	 */
	public CheckRecieverThread(ClientSender sender, ClientReceiver reciever,JFrame frame, String clientName, int clientID)
	{
		this.sender = sender;
		this.reciever = reciever;
		this.clientName = clientName;
		this.clientID = clientID;
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
		//A continuous loop checking using ClientReceiver for messages matching a particular pattern,
		//if it does, the appropriate actions are performed.
		//Looks for: Game request from another client.
		//			 Response to a game request from another client.
		//			 Indication that the starting of a game should begin.
		while(is_running)
		{
			String requestStr;
			String[] requestStrVals;
			int userID;
			String Message;
			String responseVals;
			
			//Cycle through all messages available for the client.
			for(int i = 0; i < reciever.messages.size(); i++)
			{
				if((requestStr = reciever.messages.get(i)).contains("Request:"))
				{
					//Remove the message from the message queue of the client.
					reciever.removeMessages(i, i);
					//Split sting by separator :
					requestStrVals = requestStr.split(":");
					
					userID = Integer.parseInt(requestStrVals[1]);
					Message = requestStrVals[2];
					
					//Get Yes/No result of a option message box
					int dialogResult = JOptionPane.showConfirmDialog (null, Message,"Game Request",JOptionPane.YES_NO_OPTION);
					
					if(dialogResult == JOptionPane.YES_OPTION)
					{
						//Start the game as second player
						sender.sendMessage("Game:"+userID+":Yes");
						playerNumber = 2;
					}
					else if(dialogResult == JOptionPane.NO_OPTION)
					{
						//Tell other client request was declined.
						sender.sendMessage("Game:"+userID+":No");
					}

					//Check messages from this position again.
					i--;
				}
				else if((requestStr = reciever.messages.get(i)).contains("Response:"))
				{
					reciever.removeMessages(i, i);
					
					responseVals = requestStr.substring(9);
					//Response message from the other client.
					JOptionPane.showMessageDialog(null, responseVals);
					
					i--;
				}
				else if((requestStr = reciever.messages.get(i)).contains("Start Game:"))
				{
					reciever.removeMessages(i, i);
					
					String values[] = requestStr.split(":");
					int opponentID = Integer.parseInt(values[1]);
					String opponentName = values[2];

					//Start the Noughts and Crosses game GUI
					NoughtsCrossesGUI gui = new NoughtsCrossesGUI();
					gui.playGame(clientName,clientID,opponentName,opponentID,sender,reciever,playerNumber);

					//Start as first player.
					playerNumber = 1;
				}
			}
			//Sleep to give other processes time.
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				//System.err.println("CheckRecieverThread interrupted while sleeping.");
				return;
			}
		}
	}
}
