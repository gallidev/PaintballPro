package logic;

/**
 * Abstraction of all possible game modes. Contains the basic functionality that is needed in all the game modes.
 * @author Alexandra Paduraru
 *
 */
public abstract class GameMode {
	
	private Team t1;
	private Team t2;
	
	/**
	 * Creates a new game and initialises the two teams which are playing.
	 * @param t1 The first game team.
	 * @param t2 The second game team.
	 */
	public GameMode(Team t1, Team t2) {
		super();
		this.t1 = t1;
		this.t2 = t2;
	}
	
	public void shoot(){
	
		
		//respawn the player that was hit
	}
	
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
	
	
	/** Getters and setters */
	
	public Team getFirstTeam() {
		return t1;
	}

	public void setFirstTeam(Team t1) {
		this.t1 = t1;
	}

	public Team getSecondTeam() {
		return t2;
	}

	public void setSecondTeam(Team t2) {
		this.t2 = t2;
	}
	
}
