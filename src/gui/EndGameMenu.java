package gui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import static java.lang.Thread.sleep;

public class EndGameMenu {
	// TODO: implement the menu to be displayed at the end of each game

    public static Scene getScene(GUIManager m) {
        GridPane optionsSection = new GridPane();

        Label endLabel = new Label("Game Ended");

        MenuOption[] set = {new MenuOption("Main Menu", new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                m.transitionTo("Main", null);
                System.out.println("ActionEvent: " + event);
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

        return new Scene(mainGrid, m.width, m.height);
    }
}
