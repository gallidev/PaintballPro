package players;

import enums.Team;
import networking.server.ServerReceiver;
/**
 * Class to represent the server version of a player currently in a game. Stores
 * only a selected amount of data, which is strictly necessary to the server.
 *
 * @author Alexandra Paduraru
 */
public class ServerPlayer{
	private ServerReceiver receiver;

	private double x, y;
	private int id;
	private double angle;
	private Team team;

	/**
	 * Creates a new server player with a given id, location and an assigned team colour.
	 * @param id The player's id.
	 * @param receiver The server receiver.
	 * @param x The x coordinate of the player.
	 * @param y The y coordinate of the player.
	 * @param color The player's team colour.
	 */
	public ServerPlayer(int id, ServerReceiver receiver, int x, int y, Team color){
		this.id = id;
		this.receiver = receiver;
		this.team = color;
		this.x = x;
		this.y = y;
		angle = 0.0;
	}

	/**
	 * Changes the x coordinate of the player.
	 * @param x The new x coordinate of the player's location.
	 */
	public void setX(double x){
		this.x = x;
	}
	
	/**
	 * Changes the y coordinate of the player.
	 * @param x The new y coordinate of the player's location.
	 */
	public void setY(double y){
		this.y = y;
	}
	
	/**
	 * Changes the player's angle.
	 * @param newAngle The new angle of the player.
	 */
	public void setAngle(double newAngle){
		angle = newAngle;
	}
	
	
	/**
	 * Changes the team's colour.
	 * @param team The new team colour.
	 */
	public void setTeam (Team team){
		this.team = team;
	}
	
	/**
	 * Retrieves the player's server receiver.
	 * @return The server receiver.
	 */
	public ServerReceiver getServerReceiver(){
		return receiver;
	}
	
	/**
	 * Retrieves the colour of the team that the player is playing in.
	 * @return The player's team colour.
	 */
	public Team getColour(){
		return team;
	}
	
	/**
	 * Retrieves the player's id.
	 * @return The player id.
	 */
	public int getPlayerId(){
		return id;
	}
}