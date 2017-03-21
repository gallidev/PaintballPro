package gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import networking.discoveryNew.IPAddress;
import networking.server.Server;

import java.util.ArrayList;

/**
 * Server GUI scene class
 *
 * @author Jack Hughes
 */
public class ServerGUI extends Scene {

	static GridPane view = new GridPane();
	static LoadingPane loadingPane = new LoadingPane(ServerGUI.view);

	private ArrayList<String> messages = new ArrayList<>();
	private TextArea textArea;
	private Server server;
	private Thread discovery;

	/**
	 * Create the server GUI scene
	 */
	public ServerGUI() {
		super(loadingPane, 1024, 576);

		loadingPane.startLoading();

		view.setAlignment(Pos.CENTER);
		view.setHgap(10);
		view.setVgap(10);
		view.setPadding(new Insets(25, 25, 25, 25));

		Image i = new Image("assets/paintballlogo.png");
		ImageView iv = new ImageView(i);
		iv.setId("logo");
		iv.setPreserveRatio(true);
		iv.setFitWidth(200);
		view.add(iv, 0, 0);

		view.add(new Label("Running on " + IPAddress.getLAN()), 0, 1);

		textArea = new TextArea();
		textArea.setPrefWidth(400);
		textArea.setPrefHeight(300);
		view.add(textArea, 0, 2);

		Button exitButton = new Button("Exit");
		exitButton.setOnAction((event) -> {
			if (server != null) {
				server.getExitListener().stopServer();
			}
			if (discovery != null) {
				discovery.interrupt();
			}
		});
		view.add(exitButton, 0, 3);


		getStylesheets().add("styles/menu.css");
		getRoot().setStyle("-fx-background-image: url(styles/background.png); -fx-background-size: cover;");
	}

	public void addMessage(String message) {
		messages.add(message);
		textArea.setText(String.join("\n", messages));
	}

	public void setServer(Server server, Thread discovery) {
		this.server = server;
		this.discovery = discovery;
		Platform.runLater(() -> {
			loadingPane.stopLoading();
		});
	}

}
