package logic;

public class TeamMatchMode extends GameMode {
	

	public TeamMatchMode(Team t1, Team t2) {
		super(t1, t2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isGameFinished() {
		// TODO Auto-generated method stub
		return false;
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
