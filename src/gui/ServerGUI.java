package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import networking.server.Server;

/**
 * Server GUI scene class
 */
public class ServerGUI extends Scene implements ServerView {

    static GridPane view = new GridPane();

    private String messages = "";
    private TextArea textArea;
    private Server server;
    private Thread discovery;

    public ServerGUI() {
        super(view, new GUIManager().width, new GUIManager().height);

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

        textArea = new TextArea();
        textArea.setPrefWidth(400);
        textArea.setPrefHeight(300);
        view.add(textArea, 0, 1);

        Button exitButton = new Button("Exit");
        exitButton.setOnAction((event) -> {
            if (server != null) {
                server.getExitListener().stopServer();
            }
            if (discovery != null) {
                discovery.interrupt();
            }
        });
        view.add(exitButton, 0, 2);


        getStylesheets().add("styles/menu.css");
        getRoot().setStyle("-fx-background-image: url(styles/background.png); -fx-background-size: cover;");
    }

    public void addMessage(String message) {
        System.out.println("Test: " + message);
        messages += message + "\n";
        textArea.setText(messages);
    }

    public void setServer(Server server, Thread discovery) {
        this.server = server;
        this.discovery = discovery;
    }

}
