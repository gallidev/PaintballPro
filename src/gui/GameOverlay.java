package gui;

import javafx.beans.NamedArg;
import javafx.scene.Parent;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Created by jack on 24/02/2017.
 */
public class GameOverlay extends SubScene implements GameObserver {

    private Label l;
    private GridPane p;

    public GameOverlay(Parent root, double width, double height) {
        super(root, width, height);
        init();
    }

    public GameOverlay(Parent root, double width, double height, boolean depthBuffer, SceneAntialiasing antiAliasing) {
        super(root, width, height, depthBuffer, antiAliasing);
        this.l = l;
        init();
    }

    private void init() {
        this.p = new GridPane();
        this.l = new Label("Testing");
        this.setRoot(p);
        p.add(l, 0, 0);
        p.setStyle("-fx-background-color: rgba(255,255,255,0.4); -fx-pref-height: 100px;");
    }

    @Override
    public void gameUpdated() {
        l.setText("Updated");
    }
}
