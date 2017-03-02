package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class PauseOverlayMenu {

    private SubScene getScene(double width, double height) {
        GridPane p = new GridPane();
        SubScene s = new SubScene(p, width, height);

        Label title = new Label("Paused");
        p.add(title, 0, 0);

        p.setAlignment(Pos.CENTER);
        p.setHgap(10);
        p.setVgap(10);
        p.setPadding(new Insets(25, 25, 25, 25));

        p.add(title, 0, 0);
        p.setStyle("-fx-background-color: rgba(255,255,255,0.4); -fx-pref-height: 100px;");

        return s;
    }

}