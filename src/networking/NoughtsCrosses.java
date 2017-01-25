package networking;
import javax.swing.JOptionPane;

/**
 * Class to represent a tic-tac-toe game between two players.
 */
public class NoughtsCrosses
{
	//Set some int values to represent Blank, Cross and Nought positions on board.
	public static final int BLANK = 0;
	public static final int CROSS = 1;
	public static final int NOUGHT = 2;
		
	private boolean crossTurn; //Is it player Cross' turn?
	private int[][] board; //The game board.
	
	private int playerID; //ID of the player.
	private int opponentID; //ID of the opponent.
	
	private int cross; //Client playing as Cross.
	private int nought; //Client playing ad Nought.
	
	private int repeat = 0; //Repeats to only give Win/Loose message box once.
	
	private ClientSender sender; //Send messages to the server.

	private boolean whosGo = !crossTurn; //Who's go it is to go first.
	
	/**
	 * Create a new game with an empty board.
	 * @param playerID Client ID of this player.
	 * @param sender The class which sends messages to the Server.
	 * @param playerNumber The number of the player - 1 or 2.
	 * @param opponentID The ID of the opponent client.
	 */
	public NoughtsCrosses(int playerID,ClientSender sender, int playerNumber, int opponentID)
	{
		this.sender = sender;
		this.playerID = playerID;
		this.opponentID = opponentID;
		
		//If this client is player 1, then they are Cross
		if(playerNumber == 1)
		{
			cross = playerID;
			nought = opponentID;
		}
		//Else they are Nought.
		else
		{
			cross = opponentID;
			nought = playerID;
		}
		//Initialize the board.
		board = new int[3][3];
		
		repeat = 0;
		//Set all board elements to blank.
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				board[i][j] = BLANK;
			}
		}
		//Cross goes first (initializing player)
		crossTurn = true;
	}

	/**
	 * Change who's player's turn it is and send the move to the opponent.
	 * @param i The i co-ordinate of the move - row.
	 * @param j The j co-ordinate of the move - column.
	 */
	public synchronized void updateBoard(int i, int j)
	{
		//Change who's go it is.
		crossTurn = !crossTurn;
		//Send move to the opponent.
		sender.sendMessage("Move:"+opponentID+":"+playerID+":"+i+":"+j);
	}
	
	/**
	* Let the player whose turn it is play at a particular location
	* @param i the row of the turn
	* @param j the column of the turn
	*/
	public void turn(int i, int j)
	{
		//If blank then it can be played for a Cross or Nought.
		if(board[i][j] == BLANK)
		{
			if((playerID == cross) && crossTurn)
			{
				board[i][j] = CROSS; //Set board as a Cross value
				updateBoard(i,j); //Update board
			}
			else if((playerID == nought) && !crossTurn)
			{
				board[i][j] = NOUGHT; //Set board a s a Nought value
				updateBoard(i,j); //Update board
			}
			else
			{
				JOptionPane.showMessageDialog(null, "It is not your turn, wait for the other player.");
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Board not empty at (" + i + ", " + j + ")");
		}
	}
	
	/**
	 * Represent the oppoennt's turn against the client.
	 * @param opponent The ID of the opponent client.
	 * @param i The i co-ordinate they played.
	 * @param j The j co-ordinate they played.
	 */
	public void otherPlayerTurn(int opponent,int i, int j)
	{
		//-2 indicates the board should be reset - 'new game' button has been pressed. 
		if(i == -2 && j == -2)
		{
			blankBoard();
		}
		else
		{
			if(board[i][j] == BLANK)
			{
				if(opponent == cross)
				{
					board[i][j] = CROSS;
				}
				else if(opponent == nought)
				{
					board[i][j] = NOUGHT;
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Board not empty at (" + i + ", " + j + ")");
			}
			
			crossTurn = !crossTurn;
		}
	}

	/**
	* Get symbol at given location
	* @param i the row
	* @param j the column
	* @return the symbol at that location
	*/
	public int get(int i, int j)
	{
		return board[i][j];
	}

	/**
	* Is it cross's turn?
	* @return true if it is cross's turn, false for nought's turn
	*/	
	public boolean isCrossTurn()
	{
		return crossTurn;
	}

	/**
	 * Gets whether a player has won.
	 * @param player The player to compare moves against winning moves.
	 * @return Whether or not the player has won the game.
	 */
	private boolean winner(int player)
	{
		//See if any possible combination has won the game.
		return
			(board[0][0] == player && board[0][1] == player && board[0][2] == player) ||
			(board[1][0] == player && board[1][1] == player && board[1][2] == player) ||
			(board[2][0] == player && board[2][1] == player && board[2][2] == player) ||
			(board[0][0] == player && board[1][0] == player && board[2][0] == player) ||
			(board[0][1] == player && board[1][1] == player && board[2][1] == player) ||
			(board[0][2] == player && board[1][2] == player && board[2][2] == player) ||
			(board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
			(board[0][2] == player && board[1][1] == player && board[2][0] == player);
	}
						
	/**
	* Determine who (if anyone) has won and pop up suitable message box with result.
	* @return CROSS if cross has won, NOUGHT if nought has won, otherwise BLANK
	*/
	public int whoWon()
	{
		/*try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			System.out.println("Sleeping Interrupted");
		}*/
		//If player Cross wins
		if(winner(CROSS))
		{
			//If the client is Cross player and this is first cycle
			if((playerID == cross) && repeat == 0)
			{
				//Sleep - gives time for other client to update.
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					System.out.println("Sleeping Interrupted");
				}
				repeat = 1;
				//Send message to server saying who won... increments their score by 1.
				sender.sendMessage("Winner:"+cross);
				JOptionPane.showMessageDialog(null, "You Won!");
			}
			//If first cycle and player isnt Cross.
			else if(repeat == 0)
			{
				//Sleep - gives time for other client to update.
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					System.out.println("Sleeping Interrupted");
				}
				repeat = 1;
				JOptionPane.showMessageDialog(null, "You Lost!");
			}
			return CROSS;
		}
		//If player Nought wins
		else if(winner(NOUGHT))
		{
			//If first cycle and player is Nought
			if((playerID == nought) && repeat == 0)
			{
				//Sleep - gives time for other client to update.
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					System.out.println("Sleeping Interrupted");
				}
				repeat = 1;
				//Send message to server saying who won... increments their score by 1.
				sender.sendMessage("Winner:"+nought);
				JOptionPane.showMessageDialog(null, "You Won!");
			}
			//If first cycle and player isnt Nought.
			else if(repeat == 0)
			{
				//Sleep - gives time for other client to update.
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					System.out.println("Sleeping Interrupted");
				}
				repeat = 1;
				JOptionPane.showMessageDialog(null, "You Lost!");
			}
			return NOUGHT;
		}
		else
		{
			if(repeat == 0)
			{
				boolean possibleMoves = false;
				for(int i = 0; i < 3; i++)
				{
					for(int j = 0; j < 3; j++)
					{
						if(board[i][j] == BLANK)
						{
							possibleMoves = true;
						}
					}
				}
				if(!possibleMoves)
				{
					JOptionPane.showMessageDialog(null, "It's a draw!");
					repeat = 1;
				}
			}
			return BLANK;
		}
	}
	
	/**
	 * Reset the board to blank and change who's go it is to play.
	 */
	public void blankBoard()
	{
		//Sets the board to blank.
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				board[i][j] = BLANK;
			}
		}
		//Changes who goes first.
		whosGo = !whosGo;
		//Sets crossTurn to who's go it is to go first.
		crossTurn = whosGo;
		//Resets message repeats.
		repeat = 0;
	}
	
	/**
	* Start a new game
	*/
	public void newGame()
	{
		//Tell the opponent a new game is starting.
		sender.sendMessage("Move:"+opponentID+":" + playerID + ":-2:-2");
		//Reset board.
		blankBoard();
	}
}
