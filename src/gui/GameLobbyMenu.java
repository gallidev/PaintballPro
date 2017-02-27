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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import static java.lang.Thread.sleep;

public class GameLobbyMenu {
	// TODO: implement the lobby menu GUI
	
	public static Scene getScene(GUIManager m, ObservableList<GameLobbyRow> lobbyData) {
//		GridPane table = new GridPane();
//		Label teamRed = new Label("Red");
//		Label teamBlue = new Label("Blue");
//		Label teamR1 = new Label();
//		Label teamR2 = new Label();
//		Label teamR3 = new Label();
//		Label teamR4 = new Label();
//		Label teamB1 = new Label();
//		Label teamB2 = new Label();
//		Label teamB3 = new Label();
//		Label teamB4 = new Label();
//
//
//
//		Label[] tableLabels = {teamRed, teamBlue, teamR1, teamR2, teamR3, teamR4, teamB1, teamB2, teamB3, teamB4};
//
//		for (Label label: tableLabels) {
//			label.setStyle("-fx-min-width: 100px; -fx-min-height: 50px; -fx-background-color: green; -fx-border-width: 1px; -fx-border-color: black;");
//		}

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


//		table.add(teamRed, 0, 0);
//		table.add(teamR1, 0, 1);
//		table.add(teamR2, 0, 2);
//		table.add(teamR3, 0, 3);
//		table.add(teamR4, 0, 4);
//
//		table.add(teamBlue, 1, 0);
//		table.add(teamB1, 1, 1);
//		table.add(teamB2, 1, 2);
//		table.add(teamB3, 1, 3);
//		table.add(teamB4, 1, 4);

		Label timeLabel = new Label("Waiting for more players to join...");

		GridPane optionsSection = new GridPane();
		MenuOption[] set = {new MenuOption("Change Team", false, new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				m.getClient().getSender().sendMessage("SwitchTeam");
				m.fetchLobbyUpdates();
				System.out.println("ActionEvent: " + event);
			}
		})};
		GridPane options = MenuOptionSet.optionSetToGridPane(set);

		Thread checkLobby = new Thread(new Runnable() {
			@Override
			public void run() {
				boolean threadRunning = true;
				while (threadRunning) {
					try {
						if (m.isTimerStarted()) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									options.setVisible(false);
									timeLabel.setText("Game starting in " + m.getTimeLeft() + " second(s)...");
								}
							});
							if (m.getTimeLeft() <= 1) {
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
		});

		checkLobby.start();

		

		optionsSection.add(options, 1, 0);
		

		optionsSection.add(timeLabel, 0, 0);
		
		GridPane mainGrid = new GridPane();
		mainGrid.setAlignment(Pos.CENTER);
		mainGrid.setHgap(10);
		mainGrid.setVgap(10);
		mainGrid.setPadding(new Insets(25, 25, 25, 25));
		mainGrid.add(table, 0, 0);
		mainGrid.add(optionsSection, 0, 1);

		m.addButtonHoverSounds(mainGrid);
		Scene s = new Scene(mainGrid, m.width, m.height);
		s.getStylesheets().add("styles/menu.css");
		return s;
	}
	
}
