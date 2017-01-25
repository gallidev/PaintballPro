package networking;
import javax.swing.JPanel;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Class containing the buttons that the Client uses to control the system's operations/requests.
 */
public class ClientButtons extends JPanel
{
	/**
	 * Constructor for the button containing class. Creates the buttons and adds to a JPanel.
	 * @param lobby Class representing the list of clients connected to the Server.
	 * @param board Class representing the list of scores for the clients connected to the Server.
	 * @param frame The GUI display JFrame.
	 * @param sender The class which sends messages to the Server.
	 */
	public ClientButtons(ClientLobby lobby, ScoreBoard board, JFrame frame,ClientSender sender)
	{
		//Creates the necessary buttons - refreshing score/client lists and exiting the system.
		//Also adds to the JPanel.
		
		super();

		//Refresh scores button
		JButton refreshBoard = new JButton("Refresh Scores");
		refreshBoard.addActionListener(e -> board.updateText());
		
		//Refresh lobby button
		JButton refreshLobby = new JButton("Refresh Lobby");
		refreshLobby.addActionListener(e -> lobby.updateText());
		
		//Refresh Lobby and Scores button
		JButton refreshBoth = new JButton("Refresh Lobby and Scores");
		refreshBoth.addActionListener(e -> {
							lobby.updateText(); //Update list of clients connected.
							board.updateText(); //Update scorebaord.
		});
		
		//Exit button
		JButton close = new JButton("Exit");
		close.addActionListener(e -> {
						sender.sendMessage("Exit:Client"); //Send exit message to server.
		});
		
		//Add to JPanel
		add(refreshLobby);
		add(refreshBoth);
		add(close);
		add(refreshBoard);
	}
	
	/**
	 * Stops the threads running in the Lobby and Board classes for smooth program termination.
	 * @param lobby Class representing the list of clients connected to the Server.
	 * @param board Class representing the list of scores for the clients connected to the Server.
	 */
	public void stopThreads(ClientLobby lobby,ScoreBoard board)
	{
		lobby.stopThread(); //Stop threads running on lobby.
		board.stopThread(); //Stop threads running on board.
	}
}
