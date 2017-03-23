package integrationServer;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import enums.TeamEnum;
import helpers.JavaFXTestHelper;
import logic.server.Team;
import networking.client.ClientReceiver;
import networking.game.UDPClient;
import networking.game.UDPServer;
import networking.server.ClientTable;
import networking.server.Lobby;
import networking.server.LobbyTable;
import networking.server.ServerReceiver;
import players.AIPlayer;
import players.EssentialPlayer;

public class TestLobbyIntegration {
	
	private Team red;
	private Team blue;
	private Lobby lobby;
	private UDPServer server;
	private UDPClient client;
	private ClientReceiver clientReceiver;
	private ServerReceiver serverReceiver;

	@Before
	public void setUp() throws Exception {
		JavaFXTestHelper.setupApplication();
		server = new UDPServer(new ClientTable(), new LobbyTable(), 19877);
		server.start();
		client =  new UDPClient(1, "127.0.0.1", 19877, null, null, 25567, "test");
		
		red = new Team(TeamEnum.RED);
		blue = new Team(TeamEnum.BLUE);
		lobby = new Lobby(1, 1, false);
		lobby.setRedTeam(red);
		lobby.setBlueTeam(blue);
	}

	@After
	public void tearDown() throws Exception {
		lobby = null;
	}

	@Test
	public void lobbyTest() {
		lobby.playGame(serverReceiver, server, 1);
		lobby.setPlayerNames();
		
		File names = new File("res/names.txt");
		Scanner readNames;
		try {
			readNames = new Scanner(names);
			for (EssentialPlayer p : red.getMembers()) {
				if (p instanceof AIPlayer)
					assertTrue(p.getNickname().equals(readNames.nextLine()));
			}

			for (EssentialPlayer p : blue.getMembers()) {
				if (p instanceof AIPlayer)
					assertTrue(p.getNickname().equals(readNames.nextLine()));
			}

			readNames.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void getRedTeamTest() {
		lobby.setRedTeam(red);
		assertTrue(lobby.getRedTeam() == red);
	}
	
	@Test
	public void getBlueTeamTest() {
		lobby.setRedTeam(blue);
		assertTrue(lobby.getBlueTeam() == blue);
	}
	
	@Test
	public void getMaxIdTest() {
		lobby.playGame(serverReceiver, server, 1);
		assertTrue(lobby.getMaxPlayers() == 8);
	}
	
	
	
	

}
