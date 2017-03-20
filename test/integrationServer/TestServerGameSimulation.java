package integrationServer;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import enums.TeamEnum;
import helpers.JavaFXTestHelper;
import logic.GameMode;
import physics.CollisionsHandler;
import players.EssentialPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;
import serverLogic.Team;
import serverLogic.TeamMatchMode;

public class TestServerGameSimulation {
	
	private Team red;
	private Team blue;
	private GameMode game;
	private ServerGameSimulation gameSimulation;

	@Before
	public void setUp() throws Exception {
		red = new Team(TeamEnum.RED);
		blue = new Team(TeamEnum.BLUE);
		
		JavaFXTestHelper.setupApplication();
		Map map = Map.loadRaw("elimination");
		EssentialPlayer p = new UserPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, new CollisionsHandler(map), ImageFactory.getPlayerFlagImage(TeamEnum.RED), enums.GameMode.ELIMINATION);		
		
		red.addMember(p);
		
		game = new TeamMatchMode(red, blue);
		gameSimulation = new ServerGameSimulation(game);
	}

	@Test
	public void startExecutionTestest() throws InterruptedException{
		gameSimulation.startExecution();
		Thread.sleep(1000);
		assertTrue(game.getRemainingTime() <= 17900);
		
		ArrayList<EssentialPlayer> players = new ArrayList<>();
		players.addAll(red.getMembers());
		players.addAll(blue.getMembers());
		
		for (EssentialPlayer p : players)
			assertTrue(((UserPlayer) p).isTicked);
		
	}
	
	@Test
	public void getGameTestest(){
		assertTrue(gameSimulation.getGame() instanceof TeamMatchMode);
	}
	
	

}