package gui;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 */
public class GameLobbyRow {
    private SimpleStringProperty redName;
    private SimpleStringProperty blueName;

    public GameLobbyRow(String redName, String blueName) {
        this.redName = new SimpleStringProperty(redName);
        this.blueName = new SimpleStringProperty(blueName);
    }

    public String getRedName() {
        return redName.get();
    }

    public String getBlueName() {
        return blueName.get();
    }
}
