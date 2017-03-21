package integrationClient;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import enums.TeamEnum;
import helpers.JavaFXTestHelper;
import javafx.application.Platform;
import physics.CollisionsHandler;
import physics.Flag;
import physics.GhostBullet;
import players.EssentialPlayer;
import players.GhostPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;

/**
 * Test class to test if the in-game information received from the server is correct.\
 * Class tested - {@link ClientGameStateReceiver}
 * @author Alexandra Paduraru
 *
 */
public class TestClientGameStateReceiver {

	private ArrayList<EssentialPlayer> players;
	private ClientGameStateReceiver gameStateReceiver;
	private EssentialPlayer p;
	private ClientGameStateReceiver gameStateReceiver2;
	
	@Before
	public void setUp() throws Exception {
		players = new ArrayList<>();

		JavaFXTestHelper.setupApplication();
		Map map = Map.loadRaw("elimination");
		p = new UserPlayer(0, 0, 1, map.getSpawns(), TeamEnum.RED, new CollisionsHandler(map), ImageFactory.getPlayerFlagImage(TeamEnum.RED), enums.GameMode.ELIMINATION, 30);
		players.add(p);
		
		gameStateReceiver = new ClientGameStateReceiver(players);
		
		JavaFXTestHelper.setupApplication();
		Flag flag = new Flag();
		
		gameStateReceiver2 = new ClientGameStateReceiver(players, flag);
		
		p.setNickname("TestPlayer");

	}

	@SuppressWarnings("deprecation")
	@Test
	public void updatePlayerTest() throws InterruptedException {
		gameStateReceiver.updatePlayer(1, 2, 2, 30, true);
		Thread.sleep(1000);
		assertEquals( p.getLayoutX(), 2.0, 0.5);
		assertEquals(p.getLayoutY(), 2.0, 0.5);
		assertTrue(p.isVisible());
	}
	
	@Test
	public void updateBulletsTest() throws InterruptedException {

		gameStateReceiver.updateBullets(1);
		assertTrue(p.hasShot());
		
//		String bullets = "0:2:3:1:4:5";
//		gameStateReceiver.updateBullets(1, bullets.split(":"));
//		Thread.sleep(1000);
//		
//		GhostBullet firedBullet1 = p.getFiredBullets().get(0);
//		assertEquals(firedBullet1.getX(), 2.0, 0.2);
//		assertEquals(firedBullet1.getY(), 3.0, 0.2);
//		assertEquals(firedBullet1.getBulletId(), 0);
//		
//		GhostBullet firedBullet2 = p.getFiredBullets().get(1);
//		assertEquals(firedBullet2.getX(), 4.0, 0.2);
//		assertEquals(firedBullet2.getY(), 5.0, 0.2);
//		assertEquals(firedBullet2.getBulletId(), 1);
//		
//		
//		String newBullets = "0:7:8";
//		gameStateReceiver.updateBullets(1, newBullets.split(":"));
//		System.out.println(p.getFiredBullets());
//
//		GhostBullet firedBullet3 = p.getFiredBullets().get(0);
//		assertEquals(firedBullet3.getX(), 7.0, 0.2);
//		assertEquals(firedBullet3.getY(), 8.0, 0.2);
		
	}
	
//	@Test
//	public void lostFlagTest() {
//
//	}
//	
//	@Test
//	public void respawnFlagTest() {
//	}

}
