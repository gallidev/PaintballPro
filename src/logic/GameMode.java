package logic;

import serverLogic.Team;

/**
 * Abstraction of all possible game modes. Contains the basic functionality that is needed in all the game modes.
 * @author Alexandra Paduraru
 *
 */
public abstract class GameMode {
	
	private Team red;
	private Team blue;
	
	/**
	 * Creates a new game and initialises the two teams which are playing.
	 * @param t1 The first game team.
	 * @param t2 The second game team.
	 */
	public GameMode(Team t1, Team t2) {
		super();
		this.red = t1;
		this.blue = t2;
	}
	
	public abstract void start();
	
	
	/**
	 * Checks to see if the game has finished.
	 * @return Whether or not the game has finished.
	 */
	public abstract boolean isGameFinished();
	
	/**
	 * Returns the winning team.
	 * @return The team who has won the current game.
	 */
	public abstract Team whoWon();
	
	
	public abstract long getRemainingTime();
	
	/** Getters and setters */
	
	public Team getRedTeam() {
		return red;
	}

	public void setRedTeam(Team t1) {
		this.red = t1;
	}

	public Team getBlueTeam() {
		return blue;
	}

	public void setBlueTeam(Team t2) {
		this.blue = t2;
	}
	
}
