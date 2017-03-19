package gui;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import static java.lang.Thread.sleep;

/**
 * Class containing a thread to get updates for the lobby
 */
public class GameLobbyChecker implements Runnable {

    public boolean threadRunning = true;
    private GUIManager guiManager;
    private Label timeLabel;
    private LoadingPane loadingPane;

    /**
     * Constructor for the lobby checker
     * @param guiManager GUIManager to use to fetch lobby updates
     * @param timeLabel label containing the time left in the game
     * @param loadingPane loading pane containing the main view and loading spinner view
     */
    public GameLobbyChecker(GUIManager guiManager, Label timeLabel, LoadingPane loadingPane) {
        this.guiManager = guiManager;
        this.timeLabel = timeLabel;
    }

    /**
     * Method to run the lobby checking
     */
    @Override
    public void run() {
        while (threadRunning) {
            try {
                if (guiManager.isTimerStarted()) {
                    Platform.runLater(() -> {
                        timeLabel.setText("Game starting in " + guiManager.getTimeLeft() + " second(s)...");
                    });
                    if (guiManager.getTimeLeft() <= 1) {
                        // Game starting soon, so show the loading view
                        Platform.runLater(() -> {
                            loadingPane.startLoading();
                        });
                        threadRunning = false;
                    } else {
                        guiManager.fetchLobbyUpdates();
                    }
                    sleep(100);
                } else {
                    guiManager.fetchLobbyUpdates();
                    sleep(1000);
                }
            } catch (InterruptedException e) {
                // Should never happen
                System.err.println("Could not sleep!");
            }
        }
    }
}
