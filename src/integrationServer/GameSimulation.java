package integrationServer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class GameSimulation {

	private long delayMilliseconds = 33;
	private int frames = 0;
	public static void main(String[] args){
		new GameSimulation();
	}

	public GameSimulation(){
		ScheduledExecutorService scheduler =
			     Executors.newScheduledThreadPool(1);
		Runnable game = new Runnable() {
		       public void run() {
		    	   frames ++;

		       }
		     };

		ScheduledFuture<?> gameHandle =
				scheduler.scheduleAtFixedRate(game, 0, delayMilliseconds, TimeUnit.MILLISECONDS);

		Runnable frameCounter = new Runnable() {
		       public void run() {
		    	   System.out.println("server frames " + frames);
		    	   frames = 0;

		       }
		     };

		ScheduledFuture<?> frameCounterHandle =
				scheduler.scheduleAtFixedRate(frameCounter, 0, 1, TimeUnit.SECONDS);

	}


}
