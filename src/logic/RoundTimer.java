package logic;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Creates a timer for a game round.
 * 
 * @author Alexandra Paduraru
 */
public class RoundTimer {

	private int timeLeft;
	private Timer timer;

	/**
	 * Create a timer with a given running time.
	 * 
	 * @param roundTime
	 *            How long should the game last.
	 */
	public RoundTimer(int roundTime) {
		timer = new Timer();
		this.timeLeft = roundTime;
	}

	/**
	 * Decrements the time left to play the round.
	 */
	private void setTimeLeft() {
		if (timeLeft == 1)
			timer.cancel();
		timeLeft--;
	}

	/**
	 * Checks to see if the round has finished.
	 * 
	 * @return Whether or not the game round has finished.
	 */
	public boolean isTimeElapsed() {
		return (timeLeft <= 0);
	}

	/**
	 * Starts the game round and runs for the entire game length.
	 */
	public void startTimer() {
		int delay;
		int period;

		delay = 1000;
		period = 1000;
		timer.scheduleAtFixedRate(new TimerTask() {

			public void run() {
				setTimeLeft();
			}
		}, delay, period);
	}

	/**
	 * Method which retrieves the remaining time.
	 * 
	 * @return The remaining time in seconds.
	 */
	public int getTimeLeft() {
		return timeLeft;
	}

	/**
	 * Changes the remaining time.
	 * 
	 * @param time
	 *            The new time.
	 */
	void setTimeLeft(int time) {
		timeLeft = time;
	}

	// Main method for testing purposes
	/*
	 * public static void main(String[] args) { Scanner sc = new
	 * Scanner(System.in); System.out.print("Run for (Seconds) : "); long
	 * roundTime = sc.nextInt(); RoundTimer rt = new RoundTimer(roundTime);
	 * 
	 * rt.startTimer();
	 * 
	 * System.out.println(rt.isTimeElapsed()); }
	 */

}
