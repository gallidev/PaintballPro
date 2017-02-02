package networkingOld;
import java.awt.BorderLayout;

import javax.swing.JFrame;


/**
 * Displays connected client(s) and score information to the client and allows them to request a tic-tac-toe game.
 */
public class ClientGUI
{
	
	private ScoreBoard board;
	private ClientLobby lobby;
	private ClientButtons clientButtons;
	private PlayButtons playButtons;
	private JFrame frame;
	
	/**
	 * Sets up everything for the GUI to look and function correctly.
	 * @param clientID The id of the connected client.
	 * @param clientName The nickname of the connected client.
	 * @param sender The class which sends messages to the Server.
	 * @param reciever The class which handles messages received from the Server.
	 */
	public void loadClient(int clientID,String clientName,ClientSender sender,ClientReceiver reciever)
	{
		frame = new JFrame();
		
		//Initialize needed components.
		board = new ScoreBoard(sender,reciever);
		lobby = new ClientLobby(sender, reciever,frame,clientName,clientID);
		clientButtons = new ClientButtons(lobby,board,frame,sender);
		playButtons = new PlayButtons(frame, lobby);
		
		//Set frame properties
        	frame.setSize(650, 500);
        	frame.setTitle("Client GUI:" + clientID + " - " + clientName);
        	frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //So that the user must press the Exit button
        	frame.setResizable(false); //So that the screen is a constant size and doesn't look weird.
        
        	//Set layout and add components
        	frame.setLayout(new BorderLayout());
		frame.add(board, BorderLayout.EAST);
		frame.add(lobby, BorderLayout.WEST);
		frame.add(clientButtons, BorderLayout.SOUTH);
		frame.add(playButtons, BorderLayout.CENTER);
        	frame.setVisible(true);
	}
	
	/**
	 * Stops the threads running on the client so that it can terminate smoothly and disposes frame.
	 */
	public void stopThreads()
	{
		clientButtons.stopThreads(lobby, board); //Calls method in a class that stops all running threads.
		frame.dispose(); //Disposes the running frame.
	}
}
