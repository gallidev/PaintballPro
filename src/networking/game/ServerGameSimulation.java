package networking.game;

import logic.GameMode;
import logic.server.Team;
import players.EssentialPlayer;

import java.util.ArrayList;

/**
 * Class that represents a simulation of the enitre game logic, running on the
 * server.
 *
 * @author Alexandra Paduraru
 * @author Filippo Galli
 */
public class ServerGameSimulation {

	public static final double GAME_HERTZ = 40.0;

	// game inforation
	private Team blueTeam;
	private GameMode game;
	private ArrayList<EssentialPlayer> players;
	private Team redTeam;

	private boolean debug = false;
	private Thread loop;
	private boolean paused = false;
	private boolean running = true;

	/**
	 * Initialises a new game simulation, in a given game mode.
	 *
	 * @param game
	 *            The game mode which is played.
	 */
	public ServerGameSimulation(GameMode game) {
		this.game = game;
		this.redTeam = game.getRedTeam();
		this.blueTeam = game.getBlueTeam();
	}

	/**
	 * Ticks all game players.
	 */
	private void updateGame() {
		for (EssentialPlayer player : players) {
			player.tick();
		}
	}

	public void stopGameLoop() {
		running = false;
		try {
			loop.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starts the game loop. Method to be called after the lobby time finishes.
	 */
	public void runGameLoop() {
		players = new ArrayList<>();
		players.addAll(redTeam.getMembers());
		players.addAll(blueTeam.getMembers());

		loop = new Thread() {
			public void run() {
				gameLoop();
			}
		};

		game.start();
		loop.start();
	}

	/**
	 * Starts the simulation.
	 */
	private void gameLoop() {

		// Calculate how many ns each frame should take for our target game
		// hertz.
		final double TIME_BETWEEN_UPDATES;
		final int MAX_UPDATES_BEFORE_RENDER;
		double lastUpdateTime;

		TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
		MAX_UPDATES_BEFORE_RENDER = 5;
		lastUpdateTime = System.nanoTime();

		int frameCount = 0;
		int fps = 0;

		// finding FPS.
		int lastSecondTime = (int) (lastUpdateTime / 1000000000);

		while (running) {
			// System.out.println("Paused: " + paused);
			double now = System.nanoTime();
			int updateCount = 0;

			if (!paused) {
				// Do as many game updates as we need to, potentially playing
				// catchup.
				while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
					updateGame();
					lastUpdateTime += TIME_BETWEEN_UPDATES;
					updateCount++;
					frameCount++;
				}

				if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
					lastUpdateTime = now - TIME_BETWEEN_UPDATES;
				}

				// Update the frames we got.
				int thisSecond = (int) (lastUpdateTime / 1000000000);
				if (thisSecond > lastSecondTime) {
					if(debug) System.out.println("NEW SECOND " + thisSecond + " fps: " + frameCount);
					fps = frameCount;
					frameCount = 0;
					lastSecondTime = thisSecond;
				}

				// Yield until it has been at least the target time between
				// renders. This saves the CPU from hogging.
				while (now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
					Thread.yield();

					// Stops the app from consuming all your CPU.
					try {
						Thread.sleep(1);
					} catch (Exception e) {
						e.printStackTrace();
					}

					now = System.nanoTime();
				}

				if (game.isGameFinished()) {
					stopGameLoop();
				}
			}
		}
	}

	/**
	 * Retrieves the current game mode.
	 *
	 * @return The game mode which is played in the particular game.
	 */
	public GameMode getGame() {
		return game;
	}

}
