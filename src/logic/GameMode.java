package logic;

import logic.server.Team;

/**
 * Abstraction of all possible game modes. Contains the basic functionality that
 * is needed in all the game modes.
 * 
 * @author Alexandra Paduraru
 *
 */
public abstract class GameMode {

	private Team blue;
	private Team red;

	/**
	 * Creates a new game and initialises the two teams which are playing.
	 * 
	 * @param t1
	 *            The first game team.
	 * @param t2
	 *            The second game team.
	 */
	public GameMode(Team t1, Team t2) {
		super();
		this.red = t1;
		this.blue = t2;
	}

	/**
	 * Stars a game.
	 */
	public abstract void start();

	/**
	 * Checks to see if the game has finished.
	 * 
	 * @return Whether or not the game has finished.
	 */
	public abstract boolean isGameFinished();

	/**
	 * Returns the winning team.
	 * 
	 * @return The team who has won the current game.
	 */
	public abstract Team whoWon();

	/**
	 * Returns the remaining game time.
	 * 
	 * @return The remaining game time in seconds.
	 */
	public abstract int getRemainingTime();

	/**
	 * Method to retrieve the red team in the game.
	 * 
	 * @return The red team.
	 */
	public Team getRedTeam() {
		return red;
	}

	/**
	 * Method to change the red team in the game.
	 * 
	 * @param t1
	 *            The new red team.
	 */
	public void setRedTeam(Team t1) {
		this.red = t1;
	}

	/**
	 * Method to retrieve the blue team in the game.
	 * 
	 * @return The red team.
	 */
	public Team getBlueTeam() {
		return blue;
	}

	/**
	 * Method to change the red team in the game.
	 * 
	 * @param t1
	 *            The new red team.
	 */
	public void setBlueTeam(Team t2) {
		this.blue = t2;
	}

}
