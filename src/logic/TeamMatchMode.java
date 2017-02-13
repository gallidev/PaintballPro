package logic;

/**
 * Creates a new game in the Team Match mode. In the Team Match mode, the teams
 * play against each other for a set amount of time. The winning team is the one
 * with the biggest number of points.
 * 
 * @author Alexandra Paduraru
 */
public class TeamMatchMode extends GameMode {

	private RoundTimer timer;
	private static final long gameTime = 10; // in seconds

	/**
	 * Initialises the game with two teams and starts the count-down.
	 * 
	 * @param t1
	 *            The first team.
	 * @param t2
	 *            The second team.
	 */
	public TeamMatchMode(Team t1, Team t2) {
		super(t1, t2);
		timer = new RoundTimer(gameTime);
	}

	/**
	 * Checks if the game has finished.
	 */
	@Override
	public boolean isGameFinished() {
		return timer.isTimeElapsed();
	}

	// Doesn't consider ties!!
	/**
	 * Returns the winner team.
	 * 
	 * @return The team who has won the game. The method returns null in case if
	 *         the game is a draw. However, the game should not stop, as 30 more
	 *         seconds are allocated for the game to finish.
	 */
	@Override
	public Team whoWon() {
		if (super.getFirstTeam().getScore() > super.getSecondTeam().getScore())
			return super.getFirstTeam();
		else if (super.getFirstTeam().getScore() < super.getSecondTeam().getScore())
			return super.getSecondTeam();
		else {
			// Give players 30 more seconds to play if it is a draw
			timer = new RoundTimer(30);
			return null;
		}

	}

	@Override
	public void start() {
		timer.startTimer();		
	}

}
