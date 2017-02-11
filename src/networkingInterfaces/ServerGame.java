package networkingInterfaces;

import logic.GameMode;

/**
 * Server side integration to play a game in a specific game mode. Will  be instantiated in the lobby.
 * @author Alexandra Paduraru
 *
 */
public class ServerGame {

		private GameMode game;
		
		/**
		 * The server will run a specific game mode, given as argument in this constructor.
		 * @param game The game mode that will be started.
		 */
		 public ServerGame(GameMode game){
			 this.game = game;
			 game.start();
		 }
		 
		 public GameMode getGame(){
			 return game;
		 }
}
