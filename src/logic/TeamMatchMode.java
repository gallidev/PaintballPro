package logic;

public class TeamMatchMode extends GameMode {
	
	private RoundTimer timer;
	private static final long gameTime = 180; //in seconds
	
	public TeamMatchMode(Team t1, Team t2) {
		super(t1, t2);
		timer = new RoundTimer(gameTime);
		timer.startTimer();
	}

	@Override
	public boolean isGameFinished() {
		return timer.isTimeElapsed();
	}
	
	//Doesn't consider ties!!
	@Override
	public Team whoWon() {
		if (super.getFirstTeam().getScore() > super.getSecondTeam().getScore())
			return super.getFirstTeam();
		else 
			return super.getSecondTeam();
		
	}

}
