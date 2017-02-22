package logic;

import enums.TeamEnum;
import physics.OfflinePlayer;

public class OfflineTeamMatchMode extends OfflineGameMode {

	private RoundTimer timer;
	private static final long gameTime = 180; // in seconds
	
	public OfflineTeamMatchMode(OfflinePlayer player) {
		super(player);
		timer = new RoundTimer(gameTime);
	}

	
	@Override
	public void start() {
		timer.startTimer();
	}

	@Override
	public boolean isGameFinished() {
		return timer.isTimeElapsed();
	}


	@Override
	public TeamEnum whoWon() {
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
