package gui;

import enums.GameLocation;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import static java.lang.Thread.sleep;

/**
 *
 */
public class GameLobbyMenu {

	public static Scene getScene(GUIManager m, ObservableList<GameLobbyRow> lobbyData) {

		StackPane sp = new StackPane();
		GridPane mainGrid = new GridPane();
		GridPane loadingGrid = new GridPane();
		loadingGrid.setAlignment(Pos.CENTER);
		loadingGrid.setHgap(10);
		loadingGrid.setVgap(10);
		loadingGrid.setPadding(new Insets(25, 25, 25, 25));
		ProgressIndicator spinner = new ProgressIndicator();
		spinner.setProgress(-1);
		loadingGrid.add(MenuControls.centreInPane(spinner), 0, 0);

		// Setup table
		TableView table = new TableView();
		table.setPrefWidth(200.0);
		table.setPlaceholder(new Label("No Players in Lobby"));
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setSelectionModel(null);
		TableColumn redColumn = new TableColumn("Red");
		redColumn.setCellValueFactory(new PropertyValueFactory<GameLobbyRow, String>("redName"));
		redColumn.prefWidthProperty().bind(table.widthProperty().divide(2));
		TableColumn blueColumn = new TableColumn("Blue");
		blueColumn.setCellValueFactory(new PropertyValueFactory<GameLobbyRow, String>("blueName"));
		blueColumn.prefWidthProperty().bind(table.widthProperty().divide(2));
		table.getColumns().addAll(redColumn, blueColumn);
		table.setItems(lobbyData);

		// Setup options area
		Label timeLabel = new Label("Waiting for more players to join...");
		GridPane optionsSection = new GridPane();
		MenuOption[] set = {new MenuOption("Change Team", false, new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				m.getClient().getSender().sendMessage("SwitchTeam");
				m.fetchLobbyUpdates();
			}
		})};
		GridPane options = MenuOptionSet.optionSetToGridPane(set);

		// Lobby update checking
		Thread checkLobby = new Thread(() -> {
			boolean threadRunning = true;
			while (threadRunning) {
				try {
					if (m.isTimerStarted()) {
						Platform.runLater(() -> {
							options.setVisible(false);
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
		});
		checkLobby.start();

		// Setup options section at bottom of screen
		optionsSection.add(timeLabel, 0, 0);
		optionsSection.add(options, 1, 0);

		// Setup the main grid to be displayed, and add sounds to buttons

		mainGrid.setAlignment(Pos.CENTER);
		mainGrid.setHgap(10);
		mainGrid.setVgap(10);
		mainGrid.setPadding(new Insets(25, 25, 25, 25));
		mainGrid.add(table, 0, 0);
		mainGrid.add(optionsSection, 0, 1);
		m.addButtonHoverSounds(mainGrid);
		sp.getChildren().addAll(mainGrid);
		Scene s = new Scene(sp, m.width, m.height);
		s.getStylesheets().add("styles/menu.css");
		s.getRoot().setStyle("-fx-background-image: url(styles/background.png); -fx-background-size: cover;");
		return s;
	}
	
}