package oldCode.logic;

import java.util.ArrayList;

import enums.TeamEnum;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import logic.server.Team;
import networking.server.ServerReceiver;
import physics.CollisionsHandler;
import players.EssentialPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;

/**
 * Class that creates the scene for a new game.
 * @author Alexandra Paduraru
 * @author Fillipo Galli
 */
public class GameSimulationScene extends Scene{

	private Team redTeam;
	private Team blueTeam;
	static Pane view = new Pane();

	private ArrayList<EssentialPlayer> players;

	public GameSimulationScene(ServerReceiver receiver, Team redTeam, Team blueTeam) {
		super(view);
		Map map = Map.loadRaw("res/maps/" + "elimination" + ".json");

		CollisionsHandler collisionsHandler = new CollisionsHandler(map);

		UserPlayer redPlayer = new UserPlayer(map.getSpawns()[0].x * 64, map.getSpawns()[0].y * 64, 0, map.getSpawns(), TeamEnum.RED, collisionsHandler, ImageFactory.getPlayerImage(TeamEnum.RED), map.getGameMode(), 60);
		UserPlayer bluePlayer = new UserPlayer(map.getSpawns()[4].x * 64, map.getSpawns()[4].y * 64, 4, map.getSpawns(), TeamEnum.BLUE, collisionsHandler, ImageFactory.getPlayerImage(TeamEnum.RED), map.getGameMode(), 60);

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
				for(EssentialPlayer player : players)
				{
					player.tick();
				}
			}
		}.start();

		//ServerGameStateSender stateSender = new ServerGameStateSender(receiver, players);
		//stateSender.startSending();
	}

}
