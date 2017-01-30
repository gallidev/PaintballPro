package logic;

public abstract class GameMode {
	
	private Team t1;
	private Team t2;
	
	public GameMode(Team t1, Team t2) {
		super();
		this.t1 = t1;
		this.t2 = t2;
	}
	
	

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

	public void updatePlayLocation(int newXCoord, int newYCoord){
		
	}
	
	public void updateScore(int newScore, Team t){
		
	}
	
	public void fire(){
		
	}
	
	public abstract boolean isGameFinished();
	
	public abstract Team whoWon();

}
