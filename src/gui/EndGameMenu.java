package gui;

import enums.Menu;
import enums.TeamEnum;
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
    public static Scene getScene(GUIManager m, String scores, TeamEnum player) {

        // Scores = 1:0 where 1 red : 0 blue

        Label endLabel = new Label("Game Ended");
        endLabel.setStyle("-fx-font-size: 32px;");
        GridPane endPane = MenuControls.centreInPane(endLabel);

        GridPane scorePaneTemp = new GridPane();
        Label redLabel = new Label(scores.split(",")[0]);
        redLabel.setStyle("-fx-text-fill: red; -fx-font-size: 32px;");
        Label dashLabel = new Label(" - ");
        dashLabel.setStyle("-fx-font-size: 24px;");
        Label blueLabel = new Label(scores.split(",")[1]);
        blueLabel.setStyle("-fx-text-fill: blue; -fx-font-size: 32px;");
        scorePaneTemp.add(redLabel, 0, 0);
        scorePaneTemp.add(dashLabel, 1, 0);
        scorePaneTemp.add(blueLabel, 2, 0);
        GridPane scorePane = MenuControls.centreInPane(scorePaneTemp);

        String gameStatus = "You Won!";
        try {
            if ((player == TeamEnum.RED &&
                    Integer.parseInt(scores.split(",")[1]) > Integer.parseInt(scores.split(",")[0]))
                || (player == TeamEnum.BLUE &&
                    Integer.parseInt(scores.split(",")[0]) > Integer.parseInt(scores.split(",")[1]))) {
                gameStatus = "You Lost!";
            }
        } catch (Exception e) {
            // Shouldn't happen
        }
        Label gameStatusLabel = new Label(gameStatus);
        gameStatusLabel.setStyle("-fx-font-size: 24px;");
        GridPane gameStatusPane = MenuControls.centreInPane(gameStatusLabel);


        MenuOption[] set = {new MenuOption("Continue", true, new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                m.getClient().getSender().sendMessage("Exit:Game");
                m.transitionTo(Menu.MultiplayerGameType);
            }
        })};
        GridPane options = MenuOptionSet.optionSetToGridPane(set);

        GridPane mainGrid = new GridPane();
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setHgap(10);
        mainGrid.setVgap(10);
        mainGrid.setPadding(new Insets(25, 25, 25, 25));
        mainGrid.add(endPane, 0, 0);
        mainGrid.add(gameStatusPane, 0, 1);
        mainGrid.add(scorePane, 0, 2);
        mainGrid.add(options, 0, 3);

        m.addButtonHoverSounds(mainGrid);
        Scene s = new Scene(mainGrid, m.width, m.height);
        s.getStylesheets().add("styles/menu.css");
        s.getRoot().setStyle("-fx-background-image: url(styles/background.png); -fx-background-size: cover;");
        return s;
    }
}
