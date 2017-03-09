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

		CollisionsHandler collisionsHandler = new CollisionsHandler(map);

		UserPlayer redPlayer = new UserPlayer(map.getSpawns()[0].x * 64, map.getSpawns()[0].y * 64, 0, map.getSpawns(), TeamEnum.RED, collisionsHandler, ImageFactory.getPlayerImage(TeamEnum.RED));
		UserPlayer bluePlayer = new UserPlayer(map.getSpawns()[4].x * 64, map.getSpawns()[4].y * 64, 4, map.getSpawns(), TeamEnum.BLUE, collisionsHandler, ImageFactory.getPlayerImage(TeamEnum.RED));

		//add players to the teams
		redTeam.addMember(redPlayer);
		blueTeam.addMember(bluePlayer);

		collisionsHandler.setRedTeam(redTeam);
		collisionsHandler.setBlueTeam(blueTeam);

		players = new ArrayList<>();
		players.addAll(redTeam.getMembers());
		players.addAll(blueTeam.getMembers());
		//.setInputReceiver(players);
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

		//ServerGameStateSender stateSender = new ServerGameStateSender(receiver, players);
		//stateSender.startSending();
	}

}
