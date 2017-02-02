package networkingOld;
import javax.swing.JFrame;


/**
 * Class representing the tic-tac-toe game in a GUI.
 */
public class NoughtsCrossesGUI
{
	/**
	 * Constructs the GUI frame adding all the required components.
	 * @param playerName The name of the client.
	 * @param playerID The ID of the client.
	 * @param opponent The name of the opponent client.
	 * @param opponentID The ID of the oppoent client.
	 * @param sender The class which sends messages to the Server.
	 * @param reciever The class which handles messages received from the Server.
	 * @param playerNumber The number that the player is - 1 or 2.
	 */
	public void playGame(String playerName,int playerID,String opponent,int opponentID,ClientSender sender,ClientReceiver reciever, int playerNumber)
	{
		JFrame frame = new JFrame();

		//Create the game object.
		NoughtsCrosses game = new NoughtsCrosses(playerID,sender,playerNumber,opponentID);
		
		//Create the component object for the game.
		NoughtsCrossesComponent comp = new NoughtsCrossesComponent(game,frame,opponent,opponentID,reciever,sender);

		//Set frame properties.
		frame.setSize(500,500); //Set frame size
		frame.setTitle("Noughts and Crosses : " + playerName); //Frame title - with player name.
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //Stop X from being pressed and not doing what we want. There is an exit button.
		frame.setResizable(false); //Dont allow to be resizable as that would mess up the display.

		//Add component to the frame.
		frame.add(comp);
		//Set frame visibility.
		frame.setVisible(true);
	}
}
