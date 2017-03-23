package gui;

import helpers.GUIManagerTestHelper;
import helpers.JavaFXTestHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the game lobby menu
 *
 * @author Jack Hughes
 */
public class TestGameLobbyMenu {

    /**
     * Setup the JavaFX thread
     * @throws Exception test failed
     */
    @Before
    public void setUp() throws Exception {
        JavaFXTestHelper.setupApplication();
        JavaFXTestHelper.waitForPlatform();
    }

    /**
     * Test that the scene is created correctly
     * @throws Exception test failed
     */
    @Test
    public void getScene() throws Exception {
        GUIManager g = new GUIManager();
        ObservableList<GameLobbyRow> list = FXCollections.observableArrayList();
        list.add(new GameLobbyRow("redPlayer", "bluePlayer"));
        Scene s = GameLobbyMenu.getScene(g, list);
        TableView table = GUIManagerTestHelper.findTableViewInParent(s.getRoot()).get(0);

        GameLobbyRow firstRow = (GameLobbyRow)table.getItems().get(0);
        assertTrue(firstRow.getRedName().equals("redPlayer"));
        assertTrue(firstRow.getBlueName().equals("bluePlayer"));

        assertTrue(table.getItems().size() == 1);

        g.setTimerStarted();
        g.setTimeLeft(5);

        Thread.sleep(1000);

        g.setTimeLeft(4);

        Thread.sleep(1000);

        g.setTimeLeft(1);

        Thread.sleep(1000);
    }

    /**
     * Test that the GUIManager can handle lobby updates
     * @throws Exception test failed
     */
    @Test
    public void testLobbyUpdates() throws Exception {
        GUIManager g = new GUIManager();
        String[] newPlayers = {"Player 1", "Player 2"};
        g.updateBlueLobby(newPlayers);
        g.updateRedLobby(newPlayers);
    }
}