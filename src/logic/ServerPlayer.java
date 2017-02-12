package logic;

import java.util.ArrayList;

import javafx.scene.image.Image;
import networkingServer.ServerMsgReceiver;
import physics.Bullet;
import physics.GeneralPlayer;

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
	private GeneralPlayer player;

	/* list of bullets */
	protected ArrayList<Bullet> firedBullets = new ArrayList<Bullet>();

	public ServerPlayer(GeneralPlayer p, int id, ServerMsgReceiver receiver){
		this.player = p;
		this.id = id;
	}

}
