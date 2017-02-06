package logic;

import javax.swing.Timer;

public class TeamMatchMode extends GameMode {
	
	//private Timer gameTimer;
	
	public TeamMatchMode(Team t1, Team t2) {
		super(t1, t2);
		//gameTimer = new Timer(arg0, arg1);
		//gameTimer.start();
	}

	@Override
	public boolean isGameFinished() {
		return !gameTimer.isRunning();
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
