package gui;

import enums.Menu;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;

/**
 * Class to create a scene for the help screen
 */
public class HelpMenu {

    /**
     * Create and return a help menu scene for a given GUI manager
     * @param m GUI manager to use
     * @return scene for the help menu
     */
    public static Scene getScene(GUIManager m) {

        // Create the main grid (to contain the options grid, and the apply/cancel buttons)
        GridPane mainGrid = new GridPane();
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setHgap(10);
        mainGrid.setVgap(10);
        mainGrid.setPadding(new Insets(25, 25, 25, 25));

        TextArea textArea = new TextArea();
        textArea.editableProperty().setValue(false);
        textArea.setText("Welcome to Painball Pro");

        // Create a array of options for the cancel and apply buttons
        MenuOption[] set = {new MenuOption("Back", true, new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                // Transition back to the main menu
                m.transitionTo(Menu.MainMenu);
            }
        })};
        // Turn the array into a grid pane
        GridPane buttonGrid = MenuOptionSet.optionSetToGridPane(set);

        // Add the options grid and the button grid to the main grid
        mainGrid.add(textArea, 0, 0);
        mainGrid.add(buttonGrid, 0, 1);

        // Create a new scene using the main grid
        m.addButtonHoverSounds(mainGrid);
        Scene scene = new Scene(mainGrid, m.width, m.height);
        scene.getStylesheets().add("styles/menu.css");
        scene.getRoot().setStyle("-fx-background-image: url(styles/background.png); -fx-background-size: cover;");
        return scene;
    }
}
