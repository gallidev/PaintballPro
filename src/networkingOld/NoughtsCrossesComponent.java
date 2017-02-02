package networkingOld;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;

/**
 * Class to hold the components of the tic-tac-toe GUI.
 */
public class NoughtsCrossesComponent extends JPanel
{
	/**
	 * Constructs the component adding the necessary objects.
	 * @param game The tic-tac-toe game instance.
	 * @param frame The GUI display JFrame
	 * @param opponent The opponent client's name.
	 * @param opponentID The opponent client's ID.
	 * @param reciever The class which handles messages received from the Server.
	 * @param sender The class which sends messages to the Server.
	 */
	public NoughtsCrossesComponent(NoughtsCrosses game,JFrame frame,String opponent,int opponentID,ClientReceiver reciever,ClientSender sender)
	{
		super();
		//Model of Noughts and Crosses Game
		NoughtsCrossesModel model = new NoughtsCrossesModel(game);
		
		//Thread that checks for user moves and other messages from opponent..
		CheckUserGoThread userGoThread = new CheckUserGoThread(model,reciever,frame);
		userGoThread.start();
		
		//View of the game board.
		BoardView view = new BoardView(model,reciever,frame);
		
		//Buttons that go on the board.
		ButtonPanel buttons = new ButtonPanel(model,frame,sender,opponentID,userGoThread);
		
		//Label saying who opponent is.
		JLabel opponentText = new JLabel("Opponent is:" + opponent);
		
		//Add observer to model.
		model.addObserver(view);
		
		//Add components to the JPanel.
		setLayout(new BorderLayout());
			add(opponentText, BorderLayout.NORTH);
			add(view, BorderLayout.CENTER);
			add(buttons, BorderLayout.SOUTH);
	}
}
