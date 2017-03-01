package offlineLogic;

import java.util.ArrayList;

import enums.TeamEnum;
import players.AIPlayer;
import players.GeneralPlayer;
import players.OfflinePlayer;

/**
 * Represents a game mode played in a Single Player game. The game contains 
 * just one actual player(controlled by a user) and two teams consisting only of AI players.
 * @author Alexandra Paduraru
 *
 */
public abstract class OfflineGameMode {

		
		private OfflinePlayer player;
		private OfflineTeam myTeam;
		private OfflineTeam enemies;

				
		/**
		 * Creates a new game mode which contains only one actual player and
		 * fills the rest of the player's team and the opponent team with AI players.
		 * @param player The player which will be controlled by the user in this game.
		 */
		public OfflineGameMode(OfflinePlayer player) {
			super();
			this.player = player;
			
//			//create the player's team
//			ArrayList<AIPlayer> myTeamMembers = new ArrayList<>();
//			
//			for(GeneralPlayer p : player.getTeamPlayers()){
//				myTeamMembers.add((AIPlayer)p);
//			}
//			
//			myTeam = new OfflineTeam(myTeamMembers, myTeamMembers.get(0).getTeam());
//			
//			//create the opponent team
//			ArrayList<AIPlayer> enemiesMembers = new ArrayList<>();
//			
//			for(GeneralPlayer p : player.getEnemies())
//				enemiesMembers.add((AIPlayer)p);
//			
//			enemies = new OfflineTeam(enemiesMembers, enemiesMembers.get(0).getTeam());
//			
		}
		
		/**
		 * Checks if a member of the opponent team has been eliminated.
		 * @return Whether or not one opponent has been eliminated.
		 */
		public boolean isOpponentEliminated(){
			for(AIPlayer p : enemies.getMembers())
				if (p.isEliminated())
					return true;
			return false;
		}
		
		/**
		 * Starts a game in the chosen game mode.
		 */
		public abstract void start();
		
		/**
		 * Checks to see if the game has finished.
		 * @return Whether or not the game has finished.
		 */
		public abstract boolean isGameFinished();

		/**
		 * Computes the winning team in the game.
		 * @return The colour of the winning team.
		 */
		public abstract TeamEnum whoWon();
		
		
		
		/** Getters and setters */
		
		/**
		 * Returns the player controlled by the user.
		 * @return The only user-controlled player in the game.
		 */
		public OfflinePlayer getPlayer() {
			return player;
		}
		
		/**
		 * Sets the player controlled by the user.
		 * @param player
		 */
		public void setPlayer(OfflinePlayer player) {
			this.player = player;
		}
		
		/**
		 * Returns all AI players in the same team as the user player.
		 * @return The team containing only the players in the same team as the user.
		 */
		public OfflineTeam getMyTeam() {
			return myTeam;
		}
		
		/**
		 * Changes the team of the user player.
		 * @param myTeam The new team of AI players that will play together with the user.
		 */
		public void setMyTeam(OfflineTeam myTeam) {
			this.myTeam = myTeam;
		}
		
		/**
		 * Returns a team of AI players playing against the user player.
		 * @return The opponent team.
		 */
		public OfflineTeam getEnemies() {
			return enemies;
		}
		
		/**
		 * Changes the opponent team.
		 * @param enemies A team of AI players that will play agains the user player.
		 */
		public void setEnemies(OfflineTeam enemies) {
			this.enemies = enemies;
		}
		
}
