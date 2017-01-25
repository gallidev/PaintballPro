package networking;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Class to store the game action buttons, to play a new game or exit the game.
 */
public class ButtonPanel extends JPanel
{
	/**
	 * Constructs the required objects and adds to the JPanel
	 * @param model The model of a tic-tac-toe game.
	 * @param frame The GUI display JFrame
	 * @param sender The class which sends messages to the Server.
	 * @param opponentID ID of the opponent Client.
	 * @param UserGo Thread which checks for moves made by the opponent client.
	 */
	public ButtonPanel(NoughtsCrossesModel model, JFrame frame, ClientSender sender, int opponentID, CheckUserGoThread UserGo)
	{
		super();

		//New game button
		JButton reset = new JButton("New Game");
		reset.addActionListener(e -> model.newGame());

		//Exit button
		JButton close = new JButton("Exit");
		close.addActionListener(e -> {
						model.blankBoard(); //Set game board to blank.
						sender.sendMessage("EXIT:GAME:"+opponentID); //Send exit game message to server.
						UserGo.m_running=false; //Set user go monitor thread variable to false to terminate it.
						frame.dispose(); //Dispose frame.
		});
		
		//Add buttons to the JPanel
		add(reset);
		add(close);
	}
}
