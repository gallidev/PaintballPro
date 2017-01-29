package logic;

public class Team {

	private Player[] members;
	private int membersNo;
	private int score;
	
	public Team(){
		members = new Player[4];
		membersNo = 0;
	}
	
	public int getMembersNo() {
		return membersNo;
	}

	public int getScore() {
		return score;
	}
	
	public void setScore(int newScore){
		score = newScore;
	}

	
	public void addMember(Player p){
		members[membersNo] = p;
		membersNo++;
	}
	
	public void removeMember(){
		
	}
	
	
}
