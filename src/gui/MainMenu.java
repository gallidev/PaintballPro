package gui;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainMenu extends Application {
	// TODO: implement main menu scene
	
	public static void main(String[] args) {
		// TODO: Remove this method
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Paintball Pro - Main Menu");
		
		Button singleButton = new Button();
		singleButton.setText("Single player");
		Button multiButton = new Button();
		multiButton.setText("Multiplayer");
		Button settingsButton = new Button();
		settingsButton.setText("Settings");
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		grid.add(singleButton, 0, 1);
		grid.add(multiButton, 0, 2);
		grid.add(settingsButton, 0, 3);
		primaryStage.setScene(new Scene(grid, 300, 250));
		primaryStage.show();
	}
}
