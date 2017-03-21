package gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * Class to switch between a loading screen and a given main GridPane
 *
 * @author Jack Hughes
 */
public class LoadingPane extends StackPane {

	private GridPane loadingGrid = new GridPane();
	private GridPane mainGrid;

	/**
	 * Create a loading pane
	 *
	 * @param mainGrid main view to display by default
	 */
	public LoadingPane(GridPane mainGrid) {
		this.mainGrid = mainGrid;
		getChildren().addAll(mainGrid);

		loadingGrid.setAlignment(Pos.CENTER);
		loadingGrid.setHgap(10);
		loadingGrid.setVgap(10);
		loadingGrid.setPadding(MenuControls.scaleByResolution(25));
		ProgressIndicator spinner = new ProgressIndicator();
		spinner.setProgress(-1);
		loadingGrid.add(MenuControls.centreInPane(spinner), 0, 0);
	}

	/**
	 * Method to display the loading view
	 */
	public void startLoading() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				getChildren().remove(mainGrid);
				getChildren().add(loadingGrid);
			}
		});
	}

	/**
	 * Method to hide the loading view
	 */
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
