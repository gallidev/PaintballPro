package logic;

import java.util.ArrayList;

import physics.OfflinePlayer;
import players.AIPlayer;
import players.GeneralPlayer;

public abstract class OfflineGameMode {

		private OfflinePlayer player;
		private ArrayList<AIPlayer> myTeam;
		private ArrayList<AIPlayer> enemies;
		private int myTeamScore;
		private int enemiesScore;
				
		public OfflineGameMode(OfflinePlayer player) {
			super();
			this.player = player;
			myTeam = new ArrayList<>();
			enemies = new ArrayList<>();

			
			for(GeneralPlayer p : player.getTeamPlayers())
				myTeam.add((AIPlayer) p);
			
			for(GeneralPlayer p : player.getEnemies())
				enemies.add((AIPlayer) p);
			
			myTeamScore = 0;
			enemiesScore = 0;
		}
		
		public void updateMyTeamScore(int additionalScore){
			myTeamScore += additionalScore;
		}
		
		public void updateEnemiesScore(int additionalScore){
			enemiesScore += additionalScore;
		}
		
		public void incrementMyTeamScore(){
			myTeamScore++;
		}
		
		public void incrementEnemiesScore(){
			enemiesScore++;
		}
		
		public abstract void start();
		
		/**
		 * Checks to see if the game has finished.
		 * @return Whether or not the game has finished.
		 */
		public abstract boolean isGameFinished();

		/** Getters and setters */
		
		public OfflinePlayer getPlayer() {
			return player;
		}
		public void setPlayer(OfflinePlayer player) {
			this.player = player;
		}
		public ArrayList<AIPlayer> getMyTeam() {
			return myTeam;
		}
		public void setMyTeam(ArrayList<AIPlayer> myTeam) {
			this.myTeam = myTeam;
		}
		public ArrayList<AIPlayer> getEnemies() {
			return enemies;
		}
		public void setEnemies(ArrayList<AIPlayer> enemies) {
			this.enemies = enemies;
		}

		public int getMyTeamScore() {
			return myTeamScore;
		}

		public void setMyTeamScore(int myTeamScore) {
			this.myTeamScore = myTeamScore;
		}

		public int getEnemiesScore() {
			return enemiesScore;
		}

		public void setEnemiesScore(int eneimiesScore) {
			this.enemiesScore = eneimiesScore;
		}
		
		
		
		
		
}
