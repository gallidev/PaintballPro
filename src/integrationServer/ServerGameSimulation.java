package integrationServer;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import logic.GameMode;
import players.EssentialPlayer;
import serverLogic.Team;

/**
 * Class that represents a simulation of the enitre game logic, running on the server.
 * @author Alexandra Paduraru
 * @author Filippo Galli
 */
public class ServerGameSimulation {

	private Team redTeam;
	private Team blueTeam;
	private GameMode game;

	private long delayMilliseconds = 17;
	private int frames = 0;

	private boolean debug = false;

	/**
	 * Initialises a new game simulation, in a given game mode.
	 * @param game The game mode which is played.
	 */
	public ServerGameSimulation(GameMode game){

		this.game = game;

		this.redTeam = game.getRedTeam();
		this.blueTeam = game.getBlueTeam();

	}

	/**
	 * Starts the simulation. Method to be called after the lobby time finishes.
	 */
	public void startExecution(){
		ArrayList<EssentialPlayer> players = new ArrayList<>();
		players.addAll(redTeam.getMembers());
		players.addAll(blueTeam.getMembers());

		game.start();

		ScheduledExecutorService scheduler =
			     Executors.newScheduledThreadPool(1);
		Runnable game = new Runnable() {
		       public void run() {
					for(EssentialPlayer player : players)
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

	/**
	 * Retrieves the current game mode.
	 * @return The game mode which is played in the particular game.
	 */
	public GameMode getGame(){
		return game;
	}

}
