package integrationServer;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import networking.server.ServerReceiver;
import serverLogic.Team;
import players.ServerMinimumPlayer;


public class GameSimulation {

	private Team redTeam;
	private Team blueTeam;

	private long delayMilliseconds = 33;
	private int frames = 0;
	
	private boolean debug = false;

	public GameSimulation(ServerReceiver receiver, Team redTeam, Team blueTeam){

		this.redTeam = redTeam;
		this.blueTeam = blueTeam;

		startExecution();
	}

	public void startExecution(){
		ArrayList<ServerMinimumPlayer> players = new ArrayList<>();
		players.addAll(redTeam.getMembers());
		players.addAll(blueTeam.getMembers());


		ScheduledExecutorService scheduler =
			     Executors.newScheduledThreadPool(1);
		Runnable game = new Runnable() {
		       public void run() {
				for(ServerMinimumPlayer player : players)
				{
					player.tick();
				}
		    	frames ++;
		       }
		     };

		ScheduledFuture<?> gameHandle =
				scheduler.scheduleAtFixedRate(game, 0, delayMilliseconds, TimeUnit.MILLISECONDS);

		Runnable frameCounter = new Runnable() {
		       public void run() {
		    	   if (debug) System.out.println("server frames " + frames);
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

	public void stopExecution(){

	}

}
