package gui;

import javafx.beans.property.SimpleStringProperty;

/**
 * Class containing row data for the lobby table
 */
public class GameLobbyRow {
    private SimpleStringProperty redName;
    private SimpleStringProperty blueName;

    /**
     * Construct a new row
     * @param redName name of red player in row
     * @param blueName name of blue player in row
     */
    public GameLobbyRow(String redName, String blueName) {
        this.redName = new SimpleStringProperty(redName);
        this.blueName = new SimpleStringProperty(blueName);
    }

    /**
     * Get name of red player in row
     * @return name of red player
     */
    public String getRedName() {
        return redName.get();
    }

    /**
     * Get name of blue player in row
     * @return name of blue player
     */
    public String getBlueName() {
        return blueName.get();
    }
}
