package offlineLogic;

import enums.Team;
import logic.RoundTimer;
import physics.OfflinePlayer;

/**
 * The Team Match Mode for a single player mode.
 * @author Alexandra Paduraru
 *
 */
public class OfflineTeamMatchMode extends OfflineGameMode {

	private static final long gameTime = 180; // in seconds
	private RoundTimer timer;
	
	/**
	 * Initialises the game mode with the user player. This will also create 
	 * the rest of the team and the opponent team(both filled with AI players).
	 * @param player The player that will be controlled by the user.
	 */
	public OfflineTeamMatchMode(OfflinePlayer player) {
		super(player);
		timer = new RoundTimer(gameTime);
	}

	
	/**
	 * Starts the timer for the game mode.
	 */
	@Override
	public void start() {
		timer.startTimer();
	}

	/**
	 * Checks if the game has finished.
	 * @return Whether or not the current game has finished.
	 */
	@Override
	public boolean isGameFinished() {
		return timer.isTimeElapsed();
	}


	/**
	 * Returns the colour of the winning team as a team enum.
	 * If the scores are equal, the timer is restarted, to allow players to
	 * continue the game for 30 more seconds, until one team wins.
	 */
	@Override
	public Team whoWon() {
		if (getMyTeam().getScore() > getEnemies().getScore())
			return getMyTeam().getColour();
		else if (getMyTeam().getScore() < getEnemies().getScore())
			return getEnemies().getColour();
		else{
			//allocate 30 more seconds to the game.
			RoundTimer delay = new RoundTimer(30);
			delay.startTimer();
			while (!delay.isTimeElapsed()){}
			return whoWon();
		}
	}

	/**
	 * Returns the remaining time to play in the game.(in seconds)
	 * @return The number of seconds until the game finishes.
	 */
	public long remainingTime(){
		return timer.getTimeLeft();
	}
}
