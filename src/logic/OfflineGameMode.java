package logic;

import java.util.ArrayList;

import enums.TeamEnum;
import physics.OfflinePlayer;
import players.AIPlayer;
import players.GeneralPlayer;

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
			
			//create the player's team
			ArrayList<AIPlayer> myTeamMembers = new ArrayList<>();
			
			for(GeneralPlayer p : player.getTeamPlayers()){
				myTeamMembers.add((AIPlayer)p);
			}
			
			myTeam = new OfflineTeam(myTeamMembers, myTeamMembers.get(0).getTeam());
			
			//create the opponent team
			ArrayList<AIPlayer> enemiesMembers = new ArrayList<>();
			
			for(GeneralPlayer p : player.getEnemies())
				enemiesMembers.add((AIPlayer)p);
			
			enemies = new OfflineTeam(enemiesMembers, enemiesMembers.get(0).getTeam());
			
		}
		
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
		
		public OfflinePlayer getPlayer() {
			return player;
		}
		
		public void setPlayer(OfflinePlayer player) {
			this.player = player;
		}
		
		public OfflineTeam getMyTeam() {
			return myTeam;
		}
		
		public void setMyTeam(OfflineTeam myTeam) {
			this.myTeam = myTeam;
		}
		
		public OfflineTeam getEnemies() {
			return enemies;
		}
		
		public void setEnemies(OfflineTeam enemies) {
			this.enemies = enemies;
		}
		
}
