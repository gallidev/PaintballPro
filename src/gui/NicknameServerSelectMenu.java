package gui;

import javax.swing.JOptionPane;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import networkingDiscovery.ClientListener;

/**
 * Created by jack on 12/02/2017.
 */
public class NicknameServerSelectMenu {

    public static Scene getScene(GUIManager m) {
        // Obtain the user's settings
        UserSettings s = m.getUserSettings();

        // Create the main grid (to contain the options grid, and the apply/cancel buttons)
        GridPane mainGrid = new GridPane();
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setHgap(10);
        mainGrid.setVgap(10);
        mainGrid.setPadding(new Insets(25, 25, 25, 25));

        // Create the top grid (grid to contain all possible options)
        GridPane topGrid = new GridPane();
        topGrid.setAlignment(Pos.CENTER);
        topGrid.setHgap(10);
        topGrid.setVgap(10);
        topGrid.setPadding(new Insets(25, 25, 25, 25));

        // Create the username label and text field
        Label usernameLabel = new Label("Username");

        TextField usernameText = new TextField();
        usernameText.setText(s.getUsername());

        Label ipLabel = new Label("Server IP Address");

        TextField ipText = new TextField("127.0.0.1");

        topGrid.add(usernameLabel, 0, 0);
        topGrid.add(usernameText, 1, 0);

        topGrid.add(ipLabel, 0, 1);
        topGrid.add(ipText, 1, 1);

        // Create a array of options for the cancel and apply buttons
        MenuOption[] set = {new MenuOption("Search LAN for server", new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
            	String ipAddr = ClientListener.findServer().split(":")[0];
            	if(ipAddr.compareTo("") != 0)
            		ipText.setText(ipAddr);
            	else 
            		JOptionPane.showMessageDialog(null, "Cannot find any LAN servers running.", "No LAN server.", JOptionPane.ERROR_MESSAGE);
            }
        }),new MenuOption("Connect", new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                // Update the preferences (these will automatically be saved
                // when set is called)
                s.setUsername(usernameText.getText());
                m.notifySettingsObservers();
                // Transition back to the main menu
                m.setIpAddress(ipText.getText());
                m.establishConnection();
                m.transitionTo("Multiplayer", null);
            }
        })};

        // Turn the array into a grid pane
        GridPane buttonGrid = MenuOptionSet.optionSetToGridPane(set);

        // Add the options grid and the button grid to the main grid
        mainGrid.add(topGrid, 0, 0);
        mainGrid.add(buttonGrid, 0, 1);

        // Create a new scene using the main grid
        Scene scene = new Scene(mainGrid, m.width, m.height);
        scene.getStylesheets().add("styles/menu.css");
        return scene;
    }
}
