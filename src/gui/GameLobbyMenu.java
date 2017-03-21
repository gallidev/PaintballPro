package gui;

import enums.Menu;
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

/**
 * Class containing the game's lobby menu
 *
 * @author Jack Hughes
 */
public class GameLobbyMenu {

	/**
	 * Method to create the scene for the lobby menu
	 *
	 * @param guiManager GUIManager for the game
	 * @param lobbyData  observable list of players in the lobby
	 * @return scene for the lobby menu
	 */
	public static Scene getScene(GUIManager guiManager, ObservableList<GameLobbyRow> lobbyData) {

		GridPane mainGrid = new GridPane();
		LoadingPane loadingPane = new LoadingPane(mainGrid);

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
		table.setFixedCellSize(30.0);
		table.setPrefHeight(4.0 * table.getFixedCellSize() + 30.0);

		// Setup options area
		Label timeLabel = new Label("Waiting for more players to join...");

		// Lobby update checking
		GameLobbyChecker checker = new GameLobbyChecker(guiManager, timeLabel, loadingPane);
		Thread checkLobby = new Thread(checker);
		checkLobby.start();

		GridPane optionsSection = new GridPane();
		MenuOption[] set = {new MenuOption("Change Team", false, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				guiManager.getClient().getSender().sendMessage("SwitchTeam");
				guiManager.fetchLobbyUpdates();
			}
		}), new MenuOption("Back", false, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				checker.threadRunning = false;
				try {
					checkLobby.join();
				} catch (InterruptedException e) {
					//
				}
				System.out.println(checkLobby.isAlive());
				guiManager.getClient().getSender().sendMessage("Exit:Game");
				guiManager.transitionTo(Menu.MainMenu);
			}
		})};
		GridPane options = MenuOptionSet.optionSetToGridPane(set);

		// Setup options section at bottom of screen
		optionsSection.add(timeLabel, 0, 0);
		optionsSection.add(options, 1, 0);

		// Setup the main grid to be displayed, and add sounds to buttons
		mainGrid.setAlignment(Pos.CENTER);
		mainGrid.setHgap(10);
		mainGrid.setVgap(10);
		mainGrid.setPadding(MenuControls.scaleByResolution(25));
		mainGrid.add(table, 0, 0);
		mainGrid.add(optionsSection, 0, 1);

		return guiManager.createScene(loadingPane);
	}

}