package rendering;

import enums.MenuEnum;
import gui.GUIManager;
import gui.MenuControls;
import gui.MenuOption;
import gui.MenuOptionSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

class Overlay extends SubScene
{
    private static Pane view = new Pane();
    private int redTeamScore = 0;
    private int blueTeamScore = 0;
    private Label highScore;
    private Label lowScore;
    private Label timeLeft;
    boolean opened = false;

    Overlay(GUIManager m)
    {
        super(view, Renderer.view.getWidth(), 80);
        view.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6);");

        GridPane p = new GridPane();

        p.setAlignment(Pos.CENTER);
        p.setHgap(10);
        p.setVgap(10);
        p.setPadding(new Insets(0, 25, 0, 25));
        p.setPrefWidth(Renderer.view.getWidth());
        p.setPrefHeight(80);

        highScore = new Label();
        highScore.setStyle("-fx-font-size: 32px");
        Label dashScore = new Label("-");
        dashScore.setId("DashScore");
        dashScore.setStyle("-fx-font-size: 32px");
        lowScore = new Label();
        lowScore.setStyle("-fx-font-size: 32px");

        updateScoreDisplay();

        GridPane scores = new GridPane();
        scores.add(highScore, 0, 0);
        scores.add(dashScore, 1, 0);
        scores.add(lowScore, 2, 0);
        scores.setPadding(new Insets(0));
        p.add(MenuControls.centreInPane(scores, new Insets(0)), 0, 0);

        timeLeft = new Label("1:20");
        timeLeft.setId("TimeLeft");
        p.add(MenuControls.centreInPane(timeLeft, new Insets(0)), 0, 1);

        view.getStylesheets().add("styles/menu.css");
        view.getChildren().addAll(p);

    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft.setText(timeLeft);
    }

    public void setRedTeamScore(int score) {
        this.redTeamScore = score;
        updateScoreDisplay();
    }

    public void setBlueTeamScore(int score) {
        this.blueTeamScore = score;
        updateScoreDisplay();
    }

    private void updateScoreDisplay() {
        if (blueTeamScore > redTeamScore) {
            highScore.setText(String.valueOf(blueTeamScore));
            highScore.setId("BlueScore");
            lowScore.setText(String.valueOf(redTeamScore));
            lowScore.setId("RedScore");
        } else {
            highScore.setText(String.valueOf(redTeamScore));
            highScore.setId("RedScore");
            lowScore.setText(String.valueOf(blueTeamScore));
            lowScore.setId("BlueScore");
        }
    }
}
