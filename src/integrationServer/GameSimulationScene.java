package integrationServer;

import java.util.ArrayList;

import enums.TeamEnum;
import javafx.animation.AnimationTimer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import networking.server.ServerReceiver;
import physics.CollisionsHandler;
import players.ServerMinimumPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;
import serverLogic.Team;

public class GameSimulationScene extends Scene{

	private Team redTeam;
	private Team blueTeam;
	static Pane view = new Pane();

	private ArrayList<ServerMinimumPlayer> players;

	public GameSimulationScene(ServerReceiver receiver, Team redTeam, Team blueTeam) {
		super(view);
		Map map = Map.load("res/maps/" + "elimination" + ".json");

		double imageWidth = ImageFactory.getPlayerImage(TeamEnum.RED).getWidth();
		double imageHeight = ImageFactory.getPlayerImage(TeamEnum.RED).getHeight();
		CollisionsHandler collisionsHandler = new CollisionsHandler(map);

		UserPlayer redPlayer = new UserPlayer(map.getSpawns()[0].x * 64, map.getSpawns()[0].y * 64, 0, imageWidth, imageHeight, map.getSpawns(), TeamEnum.RED, collisionsHandler);
		UserPlayer bluePlayer = new UserPlayer(map.getSpawns()[4].x * 64, map.getSpawns()[4].y * 64, 4, imageWidth, imageHeight, map.getSpawns(), TeamEnum.BLUE, collisionsHandler);

		//add players to the teams
		redTeam.addMember(redPlayer);
		blueTeam.addMember(bluePlayer);

		collisionsHandler.setRedTeam(redTeam.getMembers());
		collisionsHandler.setBlueTeam(blueTeam.getMembers());

		players = new ArrayList<>();
		players.addAll(redTeam.getMembers());
		players.addAll(blueTeam.getMembers());
		receiver.setInputReceiver(players);
		this.redTeam = redTeam;
		this.blueTeam = blueTeam;

		new AnimationTimer()
		{
			@Override
			public void handle(long now)
			{
				for(ServerMinimumPlayer player : players)
				{
					player.tick();
				}
			}
		}.start();

		ServerGameStateSender stateSender = new ServerGameStateSender(receiver, players);
		stateSender.startSending();
	}

}
