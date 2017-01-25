package networking;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.util.Observer;
import java.util.Observable;

import java.awt.Font;
import java.awt.GridLayout;

/**
 * Class to represent the buttons to play the tic-tac-toe game.
 */
public class BoardView extends JPanel implements Observer
{
	private NoughtsCrossesModel model;
	private JButton[][] cell;
	private int BoardSize = 3;

	/**
	 * Constructs the view, adding buttons and their listeners.
	 * @param model The model of the game.
	 * @param reciever The class which handles messages received from the Server.
	 * @param frame The GUI display JFrame
	 */
	public BoardView(NoughtsCrossesModel model,ClientReceiver reciever,JFrame frame)
	{
		super();
		
		this.model = model;
		//Create a new JButton object.
		cell = new JButton[BoardSize][BoardSize];
		//3x3 Grid layout for the buttons.
		setLayout(new GridLayout(BoardSize,BoardSize));
		//Cycle through each button
		for(int i = 0; i < BoardSize; i++) // y
		{
			for(int j = 0; j < BoardSize; j++) // x
			{
				cell[i][j] = new JButton(""); //Set the button object to be a JButton with no text.
				final int i2 = i; //Must be final to use in Action listener.
				final int j2 = j; 
				cell[i2][j2].addActionListener(e -> this.model.turn(i2,j2)); //When pressed simulate a turn.
				cell[i2][j2].setFont(new Font("Arial",Font.BOLD,40)); //Set button text font, boldness and size - big X and Os.
				add(cell[i2][j2]); //Add to the grid.
			} 	
		}
	}

	/**
	 * When a change occurs, updates the board display and checks if any client has won.
	 * @param obs An observable object.
	 * @param obj An Object object.
	 */
	public void update(Observable obs, Object obj)
	{
		//Cycle through each button
		for(int i = 0; i < BoardSize; i++) // y
		{
			for(int j = 0; j < BoardSize; j++) // x
			{
				//If the button has been pressed by player Nought.
				if(model.get(i,j) == NoughtsCrosses.NOUGHT) // NOUGHT
				{
					cell[i][j].setText("O");
					cell[i][j].setEnabled(false);
					model.whoWon();
				}
				//If the button has been pressed by player Cross.
				else if(model.get(i,j) == NoughtsCrosses.CROSS) // CROSSES
				{
					cell[i][j].setText("X");
					cell[i][j].setEnabled(false);
					model.whoWon();
				}
				//If neither player has pressed the button yet.
				else 
				{
					cell[i][j].setText("");
					boolean notOver = (model.whoWon() == NoughtsCrosses.BLANK);
					cell[i][j].setEnabled(notOver);
				}
			} 	
		}
		//Redisplay the updated buttons.
		repaint();
	}	
}
