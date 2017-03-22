package integrationClient;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
import physics.CollisionsHandler;
import physics.Flag;
import physics.Powerup;
import physics.PowerupType;
import players.EssentialPlayer;
import players.GhostPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;

public class TestClientIntegration {

	private UDPClient client;
	private UDPServer server;
	private ClientGameStateReceiver gameStateReceiver;
	private EssentialPlayer player;

	
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
		Map map = Map.loadRaw("elimination");
		CollisionsHandler ch = new CollisionsHandler(map);
		player = new GhostPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, ch, GameMode.ELIMINATION, 30);
		
		ArrayList<EssentialPlayer> players = new ArrayList<>();
		players.add(player);
		
		player.setMyTeam(new Team(TeamEnum.RED));
		player.setMyTeam(new Team(TeamEnum.BLUE));
		
		Powerup[] powerups = new Powerup[2];
		powerups[0] = new Powerup(PowerupType.SHIELD, map.getPowerupLocations());
		powerups[1] = new Powerup(PowerupType.SPEED, map.getPowerupLocations());

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
		generateBulletTest();
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
	}
	
	public void updateScoreTest() {
		String input = "3:5:10";
		
		client.updateScoreAction(input);
		
		assertTrue(client.testIntegration);
		input = "3:18:29";
		
		client.updateScoreAction(input);
		
		assertFalse(client.testIntegration);
	}
	
	public void generateBulletTest() throws InterruptedException {
		String input = "4:1";
		
		client.generateBullet(input);
		Thread.sleep(100);
		assertTrue(player.hasShot());
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
		String input = "!:1:5:7";
		
		client.baseFlagAction(input);
		Thread.sleep(100);
		
		assertFalse(player.hasFlag());
		assertTrue(gameStateReceiver.getFlag().isVisible());
		assertTrue(gameStateReceiver.getFlag().getLayoutX() == 5.0);
		assertTrue(gameStateReceiver.getFlag().getLayoutY() == 7.0);
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

	}
	
	public void shieldRemovedTest() {
		String input = "%:1";
		client.shieldRemovedAction(input);
		assertTrue(player.getShieldRemoved());
	}
	
}
