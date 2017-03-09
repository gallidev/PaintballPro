package gui;


import enums.Menu;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import networking.discovery.DiscoveryClientListener;
/**
 * Created by jack on 12/02/2017.
 */
public class NicknameServerSelectMenu {
    public static Scene getScene(GUIManager m) {
        // Obtain the user's settings
        UserSettings s = GUIManager.getUserSettings();
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
        final ToggleGroup group = new ToggleGroup();
        topGrid.add(usernameLabel, 0, 0);
        topGrid.add(usernameText, 1, 0);
        RadioButton automatic = new RadioButton();
        automatic.setToggleGroup(group);
        automatic.setSelected(true);
        Label automaticLabel = new Label("Search LAN for a Server");
        topGrid.add(automatic, 0, 1);
        topGrid.add(automaticLabel, 1, 1);
        RadioButton manual = new RadioButton();
        manual.setToggleGroup(group);
        Label ipLabel = new Label("Manually Enter IP Address");
        TextField ipText = new TextField("127.0.0.1");
        GridPane manualField = new GridPane();
        manualField.add(ipLabel, 0, 0);
        manualField.add(ipText, 0, 1);
        topGrid.add(manual, 0, 2);
        topGrid.add(manualField, 1, 2);
        automaticLabel.setStyle("-fx-opacity: 1.0;");
        ipLabel.setStyle("-fx-opacity: 0.5;");
        ipText.setStyle("-fx-opacity: 0.5;");
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (automatic.isSelected()) {
                    automaticLabel.setStyle("-fx-opacity: 1.0;");
                    ipLabel.setStyle("-fx-opacity: 0.5;");
                    ipText.setStyle("-fx-opacity: 0.5;");
                } else {
                    automaticLabel.setStyle("-fx-opacity: 0.5;");
                    ipLabel.setStyle("-fx-opacity: 1.0;");
                    ipText.setStyle("-fx-opacity: 1.0;");
                }
            }
        });
        ipText.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                manual.setSelected(true);
            }
        });
        ipText.editableProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                manual.setSelected(true);
            }
        });
        MenuOption[] connect = {new MenuOption("Connect", true, new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                // Update the preferences (these will automatically be saved
                // when set is called)
                s.setUsername(usernameText.getText());
                m.notifySettingsObservers();
                if (automatic.isSelected()) {
                    String ipPort = DiscoveryClientListener.findServer().split(":")[0];
                    if (ipPort.equals("")) {
                        AlertBox.showAlert("No LAN server", "Cannot find any LAN servers running. Please try again or enter a server IP manually.");
                    } else {
                        m.setIpAddress(ipPort);
                        // Transition back to the main menu
                        if (m.establishConnection())
                            m.transitionTo(Menu.MultiplayerGameType);
                    }
                } else {
                    m.setIpAddress(ipText.getText());
                    // Transition back to the main menu
                    if (m.establishConnection())
                        m.transitionTo(Menu.MultiplayerGameType);
                }
            }
        }), new MenuOption("Back", false, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                m.transitionTo(Menu.MainMenu);
            }
        })};
        // Turn the array into a grid pane
        GridPane connectGrid = MenuOptionSet.optionSetToGridPane(connect);
        // Add the options grid and the button grid to the main grid
        mainGrid.add(topGrid, 0, 0);
        mainGrid.add(connectGrid, 0, 1);
        // Create a new scene using the main grid
        Scene scene = new Scene(mainGrid, m.width, m.height);
        scene.getStylesheets().add("styles/menu.css");
        return scene;
    }
}
