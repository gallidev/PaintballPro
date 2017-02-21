package logic;

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

}
