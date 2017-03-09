package gui;

import enums.Menu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * End Game Screen
 */
public class EndGameMenu {

    /**
     * Get the scene for the end game
     * @param m manager for the GUI
     * @return scene of the end game menu
     */
    public static Scene getScene(GUIManager m, String scores) {

        // Scores = 1:0 where 1 red : 0 blue

        Label endLabel = new Label("Game Ended");

        Label redLabel = new Label(scores.split(",")[0]);
        Label blueLabel = new Label(scores.split(",")[1]);

        MenuOption[] set = {new MenuOption("Main Menu", true, new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                m.transitionTo(Menu.MainMenu, null);
            }
        })};
        GridPane options = MenuOptionSet.optionSetToGridPane(set);

        GridPane mainGrid = new GridPane();
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setHgap(10);
        mainGrid.setVgap(10);
        mainGrid.setPadding(new Insets(25, 25, 25, 25));
        mainGrid.add(endLabel, 0, 0);
        mainGrid.add(redLabel, 0, 2);
        mainGrid.add(blueLabel, 0, 3);
        mainGrid.add(options, 0, 4);

        m.addButtonHoverSounds(mainGrid);
        Scene s = new Scene(mainGrid, m.width, m.height);
        s.getStylesheets().add("styles/menu.css");
        return s;
    }
}
