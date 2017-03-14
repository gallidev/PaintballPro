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
 * Created by jack on 07/03/2017.
 */
public class TestGameLobbyMenu {

    @Before
    public void setUp() throws Exception {
        JavaFXTestHelper.setupApplication();
    }

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
    }

}