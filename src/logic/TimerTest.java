package logic;

import javax.swing.Timer;

public class TimerTest {
	
	static Timer t = new Timer(500, null);

	public static void main(String[] args) {
		System.out.println("The timer is starting now");
		t.start();
		
		while(t.isRunning()){
			
		}
		
		if (!t.isRunning())
			System.out.println("Timer stopped");
		
	}

}
