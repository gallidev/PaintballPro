package logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tester class for the timer used as a countdown in all game modes.
 * 
 * @author Alexandra Paduraru
 *
 */
public class TestRoundTimer {

	/**
	 * Creates three timers with different lasting times: one with 0
	 * seconds(extreme case), one with 5 seconds(small case) and what with the
	 * actual game time.
	 * 
	 * Then all methods from the RoundTimer class are tested in order to ensure
	 * that the timer is reliable.
	 */
	@Test
	public void test() throws InterruptedException{
		long noInterval = 0;
		long smallInterval = 2;
		long gameInterval = 4;

		RoundTimer noTimer = new RoundTimer(noInterval);
		RoundTimer smallTimer = new RoundTimer(smallInterval);
		RoundTimer gameTimer = new RoundTimer(gameInterval);
		
		// testing startTimer: only two timers start and the third one is tested
		// to ensure that its time is not running
		noTimer.startTimer();
		smallTimer.startTimer();

		// testing isTimeElapsed and startTimer
		assertTrue(noTimer.isTimeElapsed());
		assertFalse(smallTimer.isTimeElapsed());
		assertFalse(gameTimer.isTimeElapsed());

		// getTimeLeft
		assertTrue(smallTimer.getTimeLeft() <= 2);
		assertTrue(gameTimer.getTimeLeft() <= 4);
		assertTrue(noTimer.getTimeLeft() == 0);

//		try {
			Thread.sleep(2500);
			assertTrue(noTimer.isTimeElapsed());
			assertTrue(smallTimer.isTimeElapsed());
			assertFalse(gameTimer.isTimeElapsed());
//		} catch (InterruptedException e) {
//			System.exit(1);
//		}

		// testing the timer with the actual game time
		System.out.println(gameTimer.getTimeLeft());

		gameTimer.startTimer();

//		try {
			Thread.sleep(4500);
			assertTrue(noTimer.isTimeElapsed());
			assertTrue(smallTimer.isTimeElapsed());
			assertTrue(gameTimer.isTimeElapsed());
//		} catch (InterruptedException e) {
//			System.out.println("Thread couldn't sleep.");
//			System.exit(1);
//		}

		assertTrue(smallTimer.getTimeLeft() == 0);
		assertTrue(gameTimer.getTimeLeft() == 0);
		assertTrue(noTimer.getTimeLeft() <= 0);
		
		//setTimeLeft
		smallTimer.setTimeLeft(10);
		assertTrue(smallTimer.getTimeLeft() == 10);

	}
}
