package integrationServer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import serverLogic.Team;


public class GameSimulation {

	private Team redTeam;
	private Team blueTeam;
	
	private long delayMilliseconds = 33;
	private int frames = 0;


	public GameSimulation(Team redTeam, Team blueTeam){
		
		this.redTeam = redTeam;
		this.blueTeam = blueTeam;
		
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

	//For testing purposes
	//	public static void main(String[] args){
	//	new GameSimulation();
	//}

}
