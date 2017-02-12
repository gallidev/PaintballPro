package gui;

import enums.GameLocation;
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
		TableColumn redColumn = new TableColumn("Red");
		redColumn.setCellValueFactory(new PropertyValueFactory<GameLobbyRow, String>("redName"));
		TableColumn blueColumn = new TableColumn("Blue");
		blueColumn.setCellValueFactory(new PropertyValueFactory<GameLobbyRow, String>("blueName"));

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

		
		GridPane optionsSection = new GridPane();
		MenuOption[] set = {new MenuOption("Change Team", new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		        System.out.println("ActionEvent: " + event);
		    }     
		}), new MenuOption("Ready", new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		        System.out.println("ActionEvent: " + event);
		        optionsSection.getChildren().get(0).setVisible(false);
		        m.transitionTo("Elimination", null);
		    }     
		})};
		GridPane options = MenuOptionSet.optionSetToGridPane(set);
		optionsSection.add(options, 1, 0);
		
		Label timeLabel = new Label("Time remaining: 2:00");
		optionsSection.add(timeLabel, 0, 0);
		
		GridPane mainGrid = new GridPane();
		mainGrid.setAlignment(Pos.CENTER);
		mainGrid.setHgap(10);
		mainGrid.setVgap(10);
		mainGrid.setPadding(new Insets(25, 25, 25, 25));
		mainGrid.add(table, 0, 0);
		mainGrid.add(optionsSection, 0, 1);
		
		return new Scene(mainGrid, m.width, m.height);
	}
	
}
