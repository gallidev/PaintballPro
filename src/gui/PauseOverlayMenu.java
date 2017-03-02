package gui;

import enums.MenuEnum;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class PauseOverlayMenu {

    private SubScene getScene(double width, double height) {
        GridPane p = new GridPane();
        SubScene s = new SubScene(p, width, height);

        Label title = new Label("Paused");
        p.add(title, 0, 0);

        MenuOption[] set = {new MenuOption("Resume", true, new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                System.out.println("ActionEvent: " + event);
            }
        }), new MenuOption("Settings", true, new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                System.out.println("ActionEvent: " + event);
            }
        }), new MenuOption("Back", false, new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                System.out.println("ActionEvent: " + event);
            }
        })};

        GridPane buttonSet = MenuOptionSet.optionSetToGridPane(set);
        p.add(buttonSet, 0, 1);

        p.setAlignment(Pos.CENTER);
        p.setHgap(10);
        p.setVgap(10);
        p.setPadding(new Insets(25, 25, 25, 25));

        p.add(title, 0, 0);
        p.setStyle("-fx-background-color: rgba(255,255,255,0.4); -fx-pref-height: 100px;");

        return s;
    }

}