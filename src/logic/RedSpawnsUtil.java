package logic;

public class RedSpawnsUtil {
	
	private static int redPlayersNo;
	private static int nextIndex;
	
	public RedSpawnsUtil(int redPlayersNo){
		this.redPlayersNo = redPlayersNo;
		nextIndex = redPlayersNo -1;
	}


	public static void setRedPlayersNo(int n) {
		redPlayersNo = n;
	}

	public static int getNextIndex(int no) {
		setRedPlayersNo(no);
		return nextIndex;
	}

}
