package logic;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Creates a timer for a game round.
 * @author Alexandra Paduraru
 */
public class RoundTimer {
	
	private static int interval = 1;
	private static Timer timer;
	private long timeLeft;
	
	/**
	 * Create a timer with a given running time.
	 * @param roundTime How long should the game last.
	 */
	public RoundTimer(long roundTime){
		timer = new Timer();
		this.timeLeft = roundTime;
	}
	
	/**
	 * Decrements the time left to play the round.
	 */
	private void setTimeLeft() {
	    if (timeLeft == 1)
	        timer.cancel();
	    System.out.println(timeLeft + " seconds left");
	    timeLeft--;
	}

	/**
	 * Checks to see if the round has finished.
	 * @return Whether or not the game round has finished.
	 */
	public boolean isTimeElapsed(){
		return (timeLeft == 0);
	}
	
	/**
	 * Starts the game round and runs for the entire game length.
	 */
	public void startTimer(){
		int delay = 1000;
	    int period = 1000;
	    timer.scheduleAtFixedRate(new TimerTask() {

	        public void run() {
	            	setTimeLeft();
	        }
	    }, delay, period);
	}
	
	//Main method for testing purposes
/*	public static void main(String[] args) {
	    Scanner sc = new Scanner(System.in);
	    System.out.print("Run for (Seconds) : ");
	    long roundTime = sc.nextInt();
	    RoundTimer rt = new RoundTimer(roundTime);
	    
	    rt.startTimer();
	    
	    System.out.println(rt.isTimeElapsed());
	}
*/
	
}
