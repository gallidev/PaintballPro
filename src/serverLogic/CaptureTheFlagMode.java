package serverLogic;

import logic.GameMode;
import logic.RoundTimer;

/**
 * Class that implements the Capture the Flag game mode logic.
 * @author Alexandra Paduraru
 *
 */
public class CaptureTheFlagMode extends GameMode {
	
	private RoundTimer timer;
	private static final long roundTime = 30; 
	private static final int flagScore = 5;
	
	/**
	 * Initialises the Capture the Flag game mode with two given teams that are playing in the game.
	 * @param t1 The first team playing in the game.
	 * @param t2 The first team playing in the game.
	 */
	public CaptureTheFlagMode(Team t1, Team t2) {
		super(t1, t2);
		timer = new RoundTimer(roundTime);
	}
	
	/**
	 * Updates the logic when a team captures the flag.
	 * @param t The team which has captured the flag and whose score is going to be incremented.
	 */
	public void flagCaptured(Team t){
		if ( t == getFirstTeam())
			getFirstTeam().incrementScore(flagScore);
		else if (t == getSecondTeam())
			getSecondTeam().incrementScore(flagScore);
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
		if (getFirstTeam().getScore() > getSecondTeam().getScore())
			return getFirstTeam();
		else if (getFirstTeam().getScore() < getSecondTeam().getScore())
			return getSecondTeam();
		else{
			long delayTime = 30;
			RoundTimer newTimer = new RoundTimer(delayTime);
			newTimer.startTimer();
			while(!newTimer.isTimeElapsed()){}
			return whoWon();
				
		}
	}

	/**
	 * Starts the game timer.
	 */
	@Override
	public void start() {
		timer.startTimer();
	}

}
