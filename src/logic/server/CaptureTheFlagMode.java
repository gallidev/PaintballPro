package logic.server;

import logic.GameMode;
import logic.RoundTimer;

/**
 * Class that implements the Capture the Flag game mode logic.
 * 
 * @author Alexandra Paduraru
 *
 */
public class CaptureTheFlagMode extends GameMode {

	public static final int FLAG_SCORE = 5;
	public static final int LOST_FLAG_SCORE = 2;

	private static final int ROUND_TIME = 300;

	private RoundTimer timer;

	/**
	 * Initialises the Capture the Flag game mode with two given teams that are
	 * playing in the game.
	 * 
	 * @param t1
	 *            The first team playing in the game.
	 * @param t2
	 *            The first team playing in the game.
	 */
	public CaptureTheFlagMode(Team t1, Team t2) {
		super(t1, t2);
		timer = new RoundTimer(ROUND_TIME);
	}

	/**
	 * Updates the logic when a team captures the flag.
	 * 
	 * @param t
	 *            The team which has captured the flag and whose score is going
	 *            to be incremented.
	 */
	public void flagCaptured(Team t) {
		if (t == getRedTeam()) {
			getRedTeam().incrementScore(FLAG_SCORE);
		} else if (t == getBlueTeam()) {
			getBlueTeam().incrementScore(FLAG_SCORE);
		}
	}

	/**
	 * Returns whether or not the current game time is over.
	 */
	@Override
	public boolean isGameFinished() {
		return timer.isTimeElapsed();
	}

	/**
	 * Returns the winning team in the game.
	 */
	@Override
	public Team whoWon() {
		if (getRedTeam().getScore() > getBlueTeam().getScore())
			return getRedTeam();
		else if (getRedTeam().getScore() < getBlueTeam().getScore())
			return getBlueTeam();
		else {
			timer = new RoundTimer(30);
			timer.startTimer();
			return null;

		}
	}

	/**
	 * Starts the game timer.
	 */
	@Override
	public void start() {
		timer.startTimer();
	}

	/**
	 * Returns the remaining game time.
	 * 
	 * @return The remaining time in seconds.
	 */
	@Override
	public int getRemainingTime() {
		return timer.getTimeLeft();
	}

}
