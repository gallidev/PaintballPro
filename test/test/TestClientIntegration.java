package test;

import enums.GameMode;
import enums.TeamEnum;
import gui.GUIManager;
import helpers.JavaFXTestHelper;
import integration.client.ClientGameStateReceiver;
import logic.server.Team;
import networking.client.TeamTable;
import networking.game.UDPClient;
import networking.game.UDPServer;
import networking.server.ClientTable;
import networking.server.LobbyTable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import physics.CollisionsHandler;
import physics.Flag;
import physics.Powerup;
import physics.PowerupType;
import players.ClientPlayer;
import players.EssentialPlayer;
import players.GhostPlayer;
import rendering.ImageFactory;
import rendering.Map;
import rendering.Renderer;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestClientIntegration {

	private UDPClient client;
	private UDPServer server;
	private ClientGameStateReceiver gameStateReceiver;
	private EssentialPlayer player;
	private ClientPlayer cPlayer;
	private Map map;
	private CollisionsHandler ch;


	@Before
	public void setUp() throws Exception {
		ClientTable clientTable = new ClientTable();
		TeamTable teamTable = new TeamTable();
		GUIManager gui = new GUIManager();
		LobbyTable lobby = new LobbyTable();

		server = new UDPServer(clientTable, lobby, 19877);
		server.start();

		client = new UDPClient(1, "127.0.0.1", 19877,null, null, 25567, "test");
		client.start();

		JavaFXTestHelper.setupApplication();
		map = Map.loadRaw("elimination");
		ch = new CollisionsHandler(map);
		player = new GhostPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, ch, GameMode.ELIMINATION, Renderer.TARGET_FPS);

		cPlayer = new ClientPlayer(0, 0, 2, map.getSpawns(), TeamEnum.BLUE, gui, ch, null, ImageFactory.getPlayerImage(TeamEnum.BLUE), null, Renderer.TARGET_FPS);

		ArrayList<EssentialPlayer> players = new ArrayList<>();
		players.add(player);
		players.add(cPlayer);

		player.setMyTeam(new Team(TeamEnum.RED));
		player.setMyTeam(new Team(TeamEnum.BLUE));

		Powerup[] powerups = new Powerup[2];
		powerups[0] = new Powerup(PowerupType.SHIELD, map.getPowerupLocations());
		powerups[1] = new Powerup(PowerupType.SPEED, map.getPowerupLocations());


		ClientGameStateReceiver gameStateReceiver2 = new ClientGameStateReceiver(players, powerups);
		gameStateReceiver = new ClientGameStateReceiver(players, new Flag(), powerups);

		client.setGameStateReceiver(gameStateReceiver);
		Thread.sleep(1000);
	}

	@After
	public void tearDown() throws Exception {
		client.stopThread();
		server.m_running = false;
		server.interrupt();
	}

	@Test
	public void testAll() throws InterruptedException{
		updatePlayerTest();
		updateScoreTest();
		capturedFlagTest();
		bulletTest();
		baseFlagTest();
		lostFlagTest();
		powerUpTest();
		shieldRemovedTest();
	}

	public void updatePlayerTest() throws InterruptedException {
		String input = "1:1:2:3:30:true";
		client.updatePlayerAction(input);

		assertNotNull(gameStateReceiver);
		Thread.sleep(1000);
		assertEquals(player.getLayoutX(), 2.0, 0.5);
		assertEquals(player.getLayoutY(), 3.0, 0.5);
		assertTrue(player.isVisible());

		input = "1:1:2:3:30:false";
		client.updatePlayerAction(input);
		Thread.sleep(1000);
		assertFalse(player.isVisible());

		input = "1:2:220:300:30:false";
		client.updatePlayerAction(input);
		Thread.sleep(1000);
		assertFalse(cPlayer.isVisible());
	}

	public void updateScoreTest() {
		String input = "3:5:10";

		client.updateScoreAction(input);

		assertTrue(client.testIntegration);
		input = "3:18:29";

		client.updateScoreAction(input);

		assertFalse(client.testIntegration);
	}

	public void bulletTest() throws InterruptedException {

		String input = "4:1:1:" + map.getSpawns()[1].x*64 + ":" + map.getSpawns()[1].y*64 +":1.2";

		client.generateBullet(input);
		Thread.sleep(100);
		assertFalse(player.getBullets().isEmpty());

		input = "5:1:1";

		client.destroyBullet(input);

		Thread.sleep(100);

		assertFalse(player.getBullets().get(0).isVisible());

		//test with invalid ids of bullets and players

		input = "5:20:1";

		client.destroyBullet(input);

		input = "5:1:44";

		client.destroyBullet(input);

	}

	public void capturedFlagTest() throws InterruptedException {
		String input = "8:1";

		client.capturedFlagAction(input);
		Thread.sleep(100);

		assertTrue(player.hasFlag());
		assertFalse(gameStateReceiver.getFlag().isVisible());
	}

	public void lostFlagTest() throws InterruptedException {
		String input = "9:1";

		client.lostFlagAction(input);
		Thread.sleep(100);

		assertFalse(player.hasFlag());
		assertTrue(gameStateReceiver.getFlag().isVisible());
		assertTrue(gameStateReceiver.getFlag().getLayoutX() == player.getLayoutX());
		assertTrue(gameStateReceiver.getFlag().getLayoutY() == player.getLayoutY());
	}



	public void baseFlagTest() throws InterruptedException {
		String input = "!:2:30:45";

		client.baseFlagAction(input);

		assertFalse(cPlayer.hasFlag());
	}

	public void powerUpTest() {
		String input = "$:0:1";
		client.powerUpAction(input);

		assertFalse(gameStateReceiver.getPowerups()[0].isVisible());
		assertTrue(player.getShieldActive());

		input = "$:1:1";
		client.powerUpAction(input);

		assertFalse(gameStateReceiver.getPowerups()[1].isVisible());
		assertTrue(player.isSpeedActive());

		input = "P:0:1";
		client.powerUpRespawn(input);

		assertTrue(ch.getShieldPowerup().isVisible());

		input = "P:1:0";
		client.powerUpRespawn(input);

		assertTrue(ch.getSpeedPowerup().isVisible());


	}

	public void shieldRemovedTest() {
		String input = "%:1";
		client.shieldRemovedAction(input);
		assertTrue(player.getShieldPopped());
	}

}