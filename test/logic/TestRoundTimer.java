package logic;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;

/**
 * Test class for the timer used as a countdown in all game modes.
 * Class tested - {@link RoundTimer}
 * @author Alexandra Paduraru
 *
 */
public class TestRoundTimer {

	//time intervals
	int gameInterval = 4;
	int noInterval = 0;
	int smallInterval = 2;

	//timers
	RoundTimer gameTimer;
	RoundTimer noTimer;
	RoundTimer smallTimer;

	/**
	 * Set up 3 timers, one which should not run at all, two small timers of differentl length for testing.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		noTimer = new RoundTimer(noInterval);
		smallTimer = new RoundTimer(smallInterval);
		gameTimer = new RoundTimer(gameInterval);
	}
	
	@After
	public void tearDown(){
		noTimer = null;
		smallTimer = null;
		gameTimer = null;
	}
	
	/**
	 * Method that tests if timers return the correct remaining time.
	 * @throws InterruptedException
	 */
	@Test
	public void getTimeLeftTest() throws InterruptedException{
		noTimer.startTimer();
		smallTimer.startTimer();
		gameTimer.startTimer();
		
		assertTrue(smallTimer.getTimeLeft() <= 2);
		assertTrue(gameTimer.getTimeLeft() <= 4);
		assertTrue(noTimer.getTimeLeft() == 0);
		
		Thread.sleep(2500);
		assertTrue(noTimer.isTimeElapsed());
		assertTrue(smallTimer.isTimeElapsed());
		assertFalse(gameTimer.isTimeElapsed());
	}
	
	/**
	 * Method that tests if timers run for as long as they have been declared.
	 *
	 * @throws InterruptedException
	 */
	@Test
	public void gameTimingsTest() throws InterruptedException{

		// testing startTimer: only two timers start and the third one is tested
		// to ensure that its time is not running
		noTimer.startTimer();
		smallTimer.startTimer();

		assertTrue(noTimer.isTimeElapsed());
		assertFalse(smallTimer.isTimeElapsed());
		assertFalse(gameTimer.isTimeElapsed());

		gameTimer.startTimer();

		Thread.sleep(4500);
		assertTrue(noTimer.isTimeElapsed());
		assertTrue(smallTimer.isTimeElapsed());
		assertTrue(gameTimer.isTimeElapsed());

		assertTrue(smallTimer.getTimeLeft() == 0);
		assertTrue(gameTimer.getTimeLeft() == 0);
		assertTrue(noTimer.getTimeLeft() <= 0);

	}

	/**
	 * Method to test if the remaining time is changed correctly using the corresponding set method.
	 */

	public void setTimeLeftTest(){
		assertTrue(smallTimer.getTimeLeft() == 0);
		assertTrue(gameTimer.getTimeLeft() == 0);
		assertTrue(noTimer.getTimeLeft() <= 0);

		smallTimer.setTimeLeft(10);
		assertTrue(smallTimer.getTimeLeft() == 10);

	}

}
