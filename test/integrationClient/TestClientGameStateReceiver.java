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
import players.EssentialPlayer;
import players.GhostPlayer;
import players.GhostPlayer;
import rendering.ImageFactory;
import rendering.Renderer;

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
		p = new GhostPlayer(0, 0, 1, null, TeamEnum.RED, null, null, Renderer.TARGET_FPS);

		players.add(p);

		gameStateReceiver = new ClientGameStateReceiver(players, null);

		JavaFXTestHelper.setupApplication();
		Flag flag = new Flag();

		gameStateReceiver2 = new ClientGameStateReceiver(players, flag, null);

		p.setNickname("TestPlayer");

	}

	@SuppressWarnings("deprecation")
	@Test
	public void updatePlayerTest() throws InterruptedException {
		gameStateReceiver.updatePlayer(1, 2, 2, 30, true, false);
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

	@Test
	public void lostFlagTest() {
//		GhostPlayer player = getPlayerWithId(id);
//		player.setFlagStatus(true);
//		flag.setVisible(false);
//
//		System.out.println("Player " + id + " captured the flag");
//
//		gameStateReceiver.lostFlag(1);
//		assertTrue(flag.);
	}

	@Test
	public void respawnFlagTest() {
		//fail("Not yet implemented");
//		flag.setVisible(true);
//		flag.relocate(x, y);
//
//		GhostPlayer player = getPlayerWithId(id);
//		player.setFlagStatus(false);
//		System.out.println("Flag has been respawned");

		gameStateReceiver2.respawnFlag(1, 2, 3);
//		assertEquals(p.getX(), 2.0, 0.2);
//		assertEquals(p.getY(), 3.0, 0.2);
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
