package gui;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import static java.lang.Thread.sleep;

/**
 * Created by jack on 16/03/2017.
 */
public class GameLobbyChecker implements Runnable {
    public boolean threadRunning = true;
    private GUIManager m;
    private Label timeLabel;
    private StackPane sp;
    private GridPane mainGrid;
    private GridPane loadingGrid;

    public GameLobbyChecker(GUIManager m, Label timeLabel, StackPane sp, GridPane mainGrid, GridPane loadingGrid) {
        this.m = m;
        this.timeLabel = timeLabel;
        this.sp = sp;
        this.mainGrid = mainGrid;
        this.loadingGrid = loadingGrid;
    }

    @Override
    public void run() {
        while (threadRunning) {
            try {
                if (m.isTimerStarted()) {
                    Platform.runLater(() -> {
                        timeLabel.setText("Game starting in " + m.getTimeLeft() + " second(s)...");
                    });
                    if (m.getTimeLeft() <= 1) {
                        Platform.runLater(() -> {
                            sp.getChildren().remove(mainGrid);
                            sp.getChildren().addAll(loadingGrid);
                        });
                        threadRunning = false;
                    } else {
                        m.fetchLobbyUpdates();
                    }
                    sleep(100);
                } else {
                    m.fetchLobbyUpdates();
                    sleep(1000);
                }
            } catch (InterruptedException e) {
                // Should never happen
                System.err.println("Could not sleep!");
            }
        }
    }
}
