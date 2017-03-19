package gui;

import enums.Menu;
import enums.TeamEnum;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Class containing the End Game Screen
 */
public class EndGameMenu {

    /**
     * Method to get the scene for the end game
     * @param guiManager manager for the GUI
     * @param scores string containing the scores of the game, in the format "red:blue"
     * @param winningTeam the winning team of the game
     * @return scene of the end game menu
     */
    public static Scene getScene(GUIManager guiManager, String scores, TeamEnum winningTeam) {
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
            if ((winningTeam == TeamEnum.RED &&
                    Integer.parseInt(scores.split(",")[1]) > Integer.parseInt(scores.split(",")[0]))
                || (winningTeam == TeamEnum.BLUE &&
                    Integer.parseInt(scores.split(",")[0]) > Integer.parseInt(scores.split(",")[1]))) {
                gameStatus = "You Lost!";
            }
        } catch (Exception e) {
            // Shouldn't happen
        }

        Label gameStatusLabel = new Label(gameStatus);
        gameStatusLabel.setStyle("-fx-font-size: 24px;");
        GridPane gameStatusPane = MenuControls.centreInPane(gameStatusLabel);

        MenuOption[] set = {new MenuOption("Continue", true, event ->
        {
            if (guiManager.getClient() != null && guiManager.getClient().getSender() != null)
                guiManager.getClient().getSender().sendMessage("Exit:Game");
            guiManager.transitionTo(Menu.MainMenu);
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

        return guiManager.createScene(mainGrid);
    }
}
