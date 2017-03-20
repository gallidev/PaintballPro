package gui;

import enums.Menu;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import networking.discoveryNew.DiscoveryClientListener;
import networking.discoveryNew.IPAddress;

/**
 * Class containing the nickname and server selection menu
 */
public class NicknameServerSelectMenu {

    /**
     * Get the scene for the nickname and server selection menu
     * @param guiManager for the scene
     * @return scene for the menu
     */
    public static Scene getScene(GUIManager guiManager) {
        UserSettings userSettings = GUIManager.getUserSettings();

        GridPane mainGrid = new GridPane();
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setHgap(10);
        mainGrid.setVgap(10);
        mainGrid.setPadding(new Insets(25, 25, 25, 25));
        LoadingPane loadingPane = new LoadingPane(mainGrid);

        GridPane topGrid = new GridPane();
        topGrid.setAlignment(Pos.CENTER);
        topGrid.setHgap(10);
        topGrid.setVgap(10);
        topGrid.setPadding(new Insets(25, 25, 25, 25));

        // Create the username label and text field
        Label usernameLabel = new Label("Username");
        TextField usernameText = new TextField();
        usernameText.setText(userSettings.getUsername());

        // Create the toggle group
        final ToggleGroup group = new ToggleGroup();
        topGrid.add(usernameLabel, 0, 0);
        topGrid.add(usernameText, 1, 0);

        // Create the automatic radio button
        RadioButton automatic = new RadioButton();
        automatic.setToggleGroup(group);
        automatic.setSelected(true);

        // Create the search label
        Label automaticLabel = new Label("Search LAN for a Server");
        automaticLabel.setOnMouseClicked((event) -> automatic.fire());
        topGrid.add(automatic, 0, 1);
        topGrid.add(automaticLabel, 1, 1);

        // Create the manual radio button, label and text field
        RadioButton manual = new RadioButton();
        manual.setToggleGroup(group);
        Label ipLabel = new Label("Manually Enter IP Address");
        ipLabel.setOnMouseClicked((event) -> manual.fire());
        TextField ipText = new TextField("127.0.0.1");
        GridPane manualField = new GridPane();
        manualField.add(ipLabel, 0, 0);
        manualField.add(ipText, 0, 1);
        topGrid.add(manual, 0, 2);
        topGrid.add(manualField, 1, 2);

        // Add opacity styling to the form
        automaticLabel.setStyle("-fx-opacity: 1.0;");
        ipLabel.setStyle("-fx-opacity: 0.5;");
        ipText.setStyle("-fx-opacity: 0.5;");

        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (automatic.isSelected()) {
                automaticLabel.setStyle("-fx-opacity: 1.0;");
                ipLabel.setStyle("-fx-opacity: 0.5;");
                ipText.setStyle("-fx-opacity: 0.5;");
            } else {
                automaticLabel.setStyle("-fx-opacity: 0.5;");
                ipLabel.setStyle("-fx-opacity: 1.0;");
                ipText.setStyle("-fx-opacity: 1.0;");
            }
        });

        // Add listeners for focusing and editing on the text field, changing the selection to manual
        ipText.focusedProperty().addListener((observable, oldValue, newValue) -> manual.setSelected(true));
        ipText.editableProperty().addListener((observable, oldValue, newValue) -> manual.setSelected(true));

        // Create the set of buttons for connecting and going back
        MenuOption[] connect = {new MenuOption("Connect", true, (event) -> {
            loadingPane.startLoading();
            userSettings.setUsername(usernameText.getText());
            guiManager.notifySettingsObservers();

            (new Thread(() -> {
                if (automatic.isSelected()) {
                    // Search the local network for servers
                    DiscoveryClientListener client = new DiscoveryClientListener();
                    String ipPort = client.findServer().split(":")[0];

                    if (ipPort.equals("")) {
                        // Could not find a LAN server
                        Platform.runLater(() -> {
                            (new AlertBox("No LAN server", "Cannot find any LAN servers running. Please try again or enter a server IP manually.")).showAlert();
                            loadingPane.stopLoading();
                        });
                    } else {
                        // Found a LAN server, so try to connect to it
                        Platform.runLater(() -> {
                            guiManager.setIpAddress(ipPort);
                            if (guiManager.establishConnection())
                                guiManager.transitionTo(Menu.MultiplayerGameType);
                            else {
                                (new AlertBox("No LAN server", "Cannot find any LAN servers running. Please try again or enter a server IP manually.")).showAlert();
                                loadingPane.stopLoading();
                            }
                        });
                    }
                } else {
                    // Manual is selected, so try to connect to the server
                    (new Thread(() -> {
                        Platform.runLater(() -> {
                            // If localhost is selected, use the LAN IP instead
                            if (ipText.getText().equals("127.0.0.1") || ipText.getText().equals("localhost"))
                                guiManager.setIpAddress(IPAddress.getLAN());
                            else
                                guiManager.setIpAddress(ipText.getText());

                            // Try to establish a connection
                            if (guiManager.establishConnection())
                                guiManager.transitionTo(Menu.MultiplayerGameType);
                            else
                                loadingPane.stopLoading();
                        });
                    })).start();
                }
            })).start();
        }), new MenuOption("Back", false, (event) -> guiManager.transitionTo(Menu.MainMenu))};

        // Turn the array into a grid pane
        GridPane connectGrid = MenuOptionSet.optionSetToGridPane(connect);
        // Add the options grid and the button grid to the main grid
        mainGrid.add(topGrid, 0, 0);
        mainGrid.add(connectGrid, 0, 1);

        ipText.setOnKeyPressed((event) -> {
            if (event.getCode().equals(KeyCode.ENTER))
                for (Node n: connectGrid.getChildren())
                    if (n instanceof Button && ((Button) n).getText().equals("Connect"))
                        ((Button) n).fire();
        });

        // Create a new scene using the main grid
        return guiManager.createScene(loadingPane);
    }
}
