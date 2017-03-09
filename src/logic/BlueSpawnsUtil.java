package logic;

public class BlueSpawnsUtil {
	
	//spawn indexes for blue team: 4..7
	
	private static int bluePlayersNo;
	private static int nextIndex;
	
	public BlueSpawnsUtil(int bluePlayersNo){
		this.bluePlayersNo = bluePlayersNo;
		nextIndex = bluePlayersNo + 3;
	}


	public static void setBluePlayersNo(int n) {
		bluePlayersNo = n;
	}

	public static int getNextIndex(int n) {
		setBluePlayersNo(n);
		return nextIndex;
	}
	
}
