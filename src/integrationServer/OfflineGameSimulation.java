package integrationServer;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import logic.GameMode;
import serverLogic.Team;

public class OfflineGameSimulation {

	private Team redTeam;
	private Team blueTeam;
	private GameMode game;

	private long delayMilliseconds = 17;
	private int frames = 0;

	private boolean debug = false;

	public OfflineGameSimulation(GameMode game){

		this.game = game;

		this.redTeam = game.getRedTeam();
		this.blueTeam = game.getBlueTeam();

	}

	public void startExecution(){
		ArrayList<ServerMinimumPlayer> players = new ArrayList<>();
		players.addAll(redTeam.getMembers());
		players.addAll(blueTeam.getMembers());

		game.start();


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

	public GameMode getGame(){
		return game;
	}
}
