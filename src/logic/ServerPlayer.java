package logic;

import java.util.ArrayList;

import audio.AudioManager;
import enums.TeamEnum;
import javafx.scene.image.Image;
import networkingServer.ServerMsgReceiver;
import physics.Bullet;
import physics.GeneralPlayer;
import rendering.Map;

/**
 * Class to represent the server version of a player currently in a game. Stores
 * only a selected amount of data, which is strictly necessary to the server.
 *
 * @author Alexandra Paduraru
 */
public class ServerPlayer{

	private ServerMsgReceiver receiver;
	
	private int id, x, y;
	private double angle;
	private TeamEnum team;
	private Map map;

	public ServerPlayer(int id, ServerMsgReceiver receiver, int x, int y, TeamEnum color){
		this.id = id;
		this.receiver = receiver;
		this.team = color;
		this.x = x;
		this.y = y;
		angle = 0.0;
	}

	public void setTeam (TeamEnum team){
		this.team = team;
	}

	//@Override
	protected void updatePosition() {
		// TODO Auto-generated method stub

	}

	//@Override
	protected void updateAngle() {
		// TODO Auto-generated method stub

	}

	//@Override
	public void tick() {
		// TODO Auto-generated method stub

	}

	public ServerMsgReceiver getServerReceiver(){
		return receiver;
	}

}
