package logic;

import java.util.ArrayList;

import javafx.scene.image.Image;
import networkingServer.ServerMsgReceiver;
import physics.Bullet;

/**
 * Class to represent the server version of a player currently in a game. Stores
 * only a selected amount of data, which is strictly necessary to the server.
 * 
 * @author Alexandra Paduraru
 */
public class ServerPlayer {

	private int id;
	/* The location */
	private double x;
	private double y;

	/* list of bullets */
	protected ArrayList<Bullet> firedBullets = new ArrayList<Bullet>();

	public ServerPlayer(ServerMsgReceiver receiver, int id){
		
	}

}
