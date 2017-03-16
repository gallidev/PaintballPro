package gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

/**
 * Created by jack on 16/03/2017.
 */
public class LoadingPane extends StackPane {

    GridPane loadingGrid = new GridPane();
    GridPane mainGrid;

    public LoadingPane(GridPane mainGrid) {
        this.mainGrid = mainGrid;
        getChildren().addAll(mainGrid);
        createLoadingGrid();
    }

    private void createLoadingGrid() {
        loadingGrid.setAlignment(Pos.CENTER);
        loadingGrid.setHgap(10);
        loadingGrid.setVgap(10);
        loadingGrid.setPadding(new Insets(25, 25, 25, 25));
        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setProgress(-1);
        loadingGrid.add(MenuControls.centreInPane(spinner), 0, 0);
    }

    public void startLoading() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getChildren().remove(mainGrid);
                getChildren().add(loadingGrid);
            }
        });
    }

    public void stopLoading() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getChildren().add(mainGrid);
                getChildren().remove(loadingGrid);
            }
        });
    }
}
