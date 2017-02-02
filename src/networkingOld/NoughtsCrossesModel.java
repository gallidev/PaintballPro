package networkingOld;


import java.util.Observable;

/**
 * Class to model a tic-tac-toe game.
 */
public class NoughtsCrossesModel extends Observable
{
	private NoughtsCrosses oxo;

	/**
	 * Constructs the model, adding the passed game variable.
	 * @param oxo The tic-tac-toe game.
	 */
	public NoughtsCrossesModel(NoughtsCrosses oxo)
	{
		super();
		this.oxo = oxo;
	}

	/**
	* Wrapper Method - Get symbol at given location
	* @param i the row
	* @param j the column
	* @return the symbol at that location
	*/
	public int get(int i, int j)
	{
		return oxo.get(i,j);
	}

	/**
	* Wrapper Method - Is it cross's turn?
	* @return true if it is cross's turn, false for nought's turn
	*/
	public boolean isCrossTurn()
	{
		return oxo.isCrossTurn();
	}

	/**
	 * Wrapper Method - represents the opponent client's move.
	 * @param opponent The ID of the opponent client.
	 * @param i the row of the move.
	 * @param j the column of the move.
	 */
	public void otherPlayerTurn(int opponent, int i, int j)
	{
		//Call method to reset the board.
		if(i == -2 && j == -2)
			oxo.blankBoard();
		//Call method to represent opponent's move.
		else
			oxo.otherPlayerTurn(opponent, i, j);

		setChanged();
		notifyObservers();
	}
	
	/**
	 * Resets the state of the board and alternates the starting player's turn.
	 */
	public void blankBoard()
	{
		oxo.blankBoard();
	}
	
	/**
	* Wrapper Method - Let the player whose turn it is to play at a particular location
	* @param i the row
	* @param j the column
	*/
	public void turn(int i, int j)
	{
		//Call method to represent a client's turn.
		oxo.turn(i,j);
		setChanged();
		notifyObservers();
	}

	/**
	* Wrapper Method - Determine who (if anyone) has won
	* @return CROSS if cross has won, NOUGHT if nought has won, otherwise BLANK
	*/
	public int whoWon()
	{
		return oxo.whoWon();
	}
	
	/**
	* Wrapper Method - Start a new game
	*/
	public void newGame()
	{
		//Call method to start a new game for client.
		oxo.newGame();
		setChanged();
		notifyObservers();
	}
}
