package networkingOld;

import javax.swing.JPanel;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Creates and adds the buttons required for selecting and unselecting a client to play against.
 */
public class PlayButtons extends JPanel
{
	/**
	 * Creates and adds the buttons required for selecting and unselecting a client to play against.
	 * @param frame The GUI display JFrame.
	 * @param lobby Class representing the list of clients connected to the Server.
	 */
	public PlayButtons(JFrame frame, ClientLobby lobby)
	{
		super();

		//Request to play against a client button
		JButton sendRequest = new JButton("Send Request to Play");
		sendRequest.addActionListener(e -> lobby.sendRequest());
		
		//Unselect player button
		JButton unselect = new JButton("Unselect Player");
		unselect.addActionListener(e -> lobby.unselect());
		
		//Add to the JPanel
		add(sendRequest);
		add(unselect);
	}
}