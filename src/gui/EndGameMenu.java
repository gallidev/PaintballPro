package gui;

import enums.Menu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class EndGameMenu {
	// TODO: implement the menu to be displayed at the end of each game

    public static Scene getScene(GUIManager m) {
        GridPane optionsSection = new GridPane();

        Label endLabel = new Label("Game Ended");

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
        mainGrid.add(options, 0, 1);

        m.addButtonHoverSounds(mainGrid);
        Scene s = new Scene(mainGrid, m.width, m.height);
        s.getStylesheets().add("styles/menu.css");
        return s;
    }
}
