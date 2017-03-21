package integrationServer;

import logic.GameMode;
import players.EssentialPlayer;
import players.UserPlayer;
import serverLogic.Team;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Class that represents a simulation of the enitre game logic, running on the server.
 * @author Alexandra Paduraru
 * @author Filippo Galli
 */
public class ServerGameSimulation {

    public static final double GAME_HERTZ = 40.0;

	private static final long delayMilliseconds = 17;
	private Team redTeam;
	private Team blueTeam;
	private GameMode game;
	private int frames = 0;

	ArrayList<EssentialPlayer> players;

	private boolean debug = false;

	private boolean running = true;
	private boolean paused = false;

	 Thread loop;

	/**
	 * Initialises a new game simulation, in a given game mode.
	 * @param game The game mode which is played.
	 */
	public ServerGameSimulation(GameMode game){

		this.game = game;

		this.redTeam = game.getRedTeam();
		this.blueTeam = game.getBlueTeam();

	}

	private void updateGame(){
		for(EssentialPlayer player : players)
		{
			player.tick();
		}
	}

	/**
	 * Starts the simulation. Method to be called after the lobby time finishes.
	 */
	public void startExecution(){
		players = new ArrayList<>();
		players.addAll(redTeam.getMembers());
		players.addAll(blueTeam.getMembers());

		game.start();

		ScheduledExecutorService scheduler =
			     Executors.newScheduledThreadPool(1);
		Runnable gameloop = new Runnable() {
		       public void run() {
					for(EssentialPlayer player : players)
					{
						UserPlayer.isTicked = false;
						player.tick();
					}
			    	frames ++;
			    	
			    	if (game.isGameFinished()){
			    		scheduler.shutdown();
			    		stopGameLoop();
			    		System.out.println("game finished");
			    	}
			    	
		       }
		     };

		ScheduledFuture<?> gameHandle =
				scheduler.scheduleAtFixedRate(gameloop, 0, delayMilliseconds, TimeUnit.MILLISECONDS);

//		Runnable frameCounter = new Runnable() {
//		       public void run() {
//		    	   if (debug) System.out.println("server frames " + frames);
//		    	   frames = 0;
//
//		       }
//		     };
//
//		ScheduledFuture<?> frameCounterHandle =
//				scheduler.scheduleAtFixedRate(frameCounter, 0, 1, TimeUnit.SECONDS);

	}


	public void stopGameLoop(){
		running = false;
		try {
			loop.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	 //Starts a new thread and runs the game loop in it.
	   public void runGameLoop()
	   {
			players = new ArrayList<>();
			players.addAll(redTeam.getMembers());
			players.addAll(blueTeam.getMembers());

			  loop = new Thread()
			  {
			     public void run()
			     {
			        gameLoop();
			     }
			  };
			  //loop.setPriority(Thread.MAX_PRIORITY);

			  game.start();
            loop.start();


//			  new java.util.Timer().schedule(
//				        new java.util.TimerTask() {
//				            @Override
//				            public void run() {
//
//				            }
//				        },
//				        2000
//				);
	   }

	   //Only run this in another Thread!
	   private void gameLoop()
	   {

	      //Calculate how many ns each frame should take for our target game hertz.
	      final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
	      //At the very most we will update the game this many times before a new render.
	      //If you're worried about visual hitches more than perfect timing, set this to 1.
	      final int MAX_UPDATES_BEFORE_RENDER = 5;
	      //We will need the last update time.
	      double lastUpdateTime = System.nanoTime();

	      int frameCount = 0;
	      int fps = 0;

	      //Simple way of finding FPS.
	      int lastSecondTime = (int) (lastUpdateTime / 1000000000);



	      while (running)
	      {
//	    	  System.out.println("Paused: " + paused);
	         double now = System.nanoTime();
	         int updateCount = 0;

	         if (!paused)
	         {
	             //Do as many game updates as we need to, potentially playing catchup.
	            while( now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER )
	            {
	               updateGame();
	               lastUpdateTime += TIME_BETWEEN_UPDATES;
	               updateCount++;
	               frameCount++;
	            }

	            //If for some reason an update takes forever, we don't want to do an insane number of catchups.
	            //If you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
	            if ( now - lastUpdateTime > TIME_BETWEEN_UPDATES)
	            {
	               lastUpdateTime = now - TIME_BETWEEN_UPDATES;
	            }

	            //Update the frames we got.
	            int thisSecond = (int) (lastUpdateTime / 1000000000);
	            if (thisSecond > lastSecondTime)
	            {

				System.out.println("NEW SECOND " + thisSecond + " fps: " + frameCount);
	               fps = frameCount;
	               frameCount = 0;
	               lastSecondTime = thisSecond;
	            }

	            //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
	            while (now - lastUpdateTime < TIME_BETWEEN_UPDATES)
	            {
	               Thread.yield();

	               //This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
	               //You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
	               //FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
	               try {Thread.sleep(1);} catch(Exception e) {}

	               now = System.nanoTime();
	            }
	         }
	      }
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
