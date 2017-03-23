package rendering;

import gui.GUIManager;
import helpers.JavaFXTestHelper;
import networking.client.ClientReceiver;
import networking.client.TeamTable;
import networking.game.UDPClient;
import networking.game.UDPServer;
import networking.server.ClientTable;
import networking.server.LobbyTable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static gui.GUIManager.renderer;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestRendererMultiplayer
{
	private GUIManager guiManager;

	@Before
	public void setUp() throws Exception
	{
		JavaFXTestHelper.setupApplication();
		guiManager = new GUIManager();

		ClientTable table = new ClientTable();
		LobbyTable lobby = new LobbyTable();
		lobby.testEnv = true;
		int id = table.add("test");
		lobby.addPlayerToLobby(table.getPlayer(id), 1, null, null);

		UDPServer server = new UDPServer(table, lobby, 19887);
		server.start();
		UDPClient client = new UDPClient(1, "127.0.0.1", 19887,null, null, 25568, "test");

		ClientReceiver receiver = new ClientReceiver(0, null, null, guiManager, client, new TeamTable());
		receiver.startGameAction("2:0:1:Red:Artur:2:Blue:Beth:");
		renderer = new Renderer("ctf", receiver, guiManager);
	}

	@After
	public void tearDown()
	{
		renderer.destroy();
		renderer = null;
	}

	@Test
	public void testRenderer()
	{
		assertNotNull(renderer);
		assertNotNull(renderer.getMap());

		assertNotNull(renderer.getHud());
		assertTrue(Renderer.view.getChildren().contains(renderer.getHud()));

		assertNotNull(renderer.cPlayer);
		assertTrue(Renderer.view.getChildren().contains(renderer.cPlayer));
	}

}
