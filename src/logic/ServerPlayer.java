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
public class ServerPlayer  extends GeneralPlayer{

	private int id;
	/* The location */
	private double x;
	private double y;

	/* list of bullets */
	protected ArrayList<Bullet> firedBullets = new ArrayList<Bullet>();

	public ServerPlayer(int id, ServerMsgReceiver receiver, int x, int y){
		super(x, y, id);
		this.id = id;
	}

	@Override
	protected void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateAngle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

}
