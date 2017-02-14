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
	private double x;
	private double y;
	private double angle;
	private int id;
	private TeamEnum team;

	public ServerPlayer(int id, ServerMsgReceiver receiver, double x, double y, TeamEnum color){
		this.id = id;
		this.receiver = receiver;
		this.team = color;
		this.x = x;
		this.y = y;
		angle = 0;
	}

	public void setTeam (TeamEnum team){
		this.team = team;
	}
	
	public ServerMsgReceiver getServerReceiver(){
		return receiver;
	}

}
