package gui;


import enums.Menu;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import networking.discovery.DiscoveryClientListener;
import networking.discovery.IPAddress;

/**
 * Created by jack on 12/02/2017.
 */
public class NicknameServerSelectMenu {
    public static Scene getScene(GUIManager m) {
        UserSettings s = GUIManager.getUserSettings();

        GridPane mainGrid = new GridPane();
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setHgap(10);
        mainGrid.setVgap(10);
        mainGrid.setPadding(new Insets(25, 25, 25, 25));
        LoadingPane sp = new LoadingPane(mainGrid);

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
                sp.startLoading();
                s.setUsername(usernameText.getText());
                m.notifySettingsObservers();

                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (automatic.isSelected()) {
                            DiscoveryClientListener client = new DiscoveryClientListener();
                            String ipPort = client.findServer().split(":")[0];
                            System.out.println("IP connecting to:"+ipPort);
                            if (ipPort.equals("")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertBox.showAlert("No LAN server", "Cannot find any LAN servers running. Please try again or enter a server IP manually.");
                                        sp.stopLoading();
                                    }
                                });

                            } else {

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        m.setIpAddress(ipPort);
                                        // Transition back to the main menu
                                        if (m.establishConnection())
                                            m.transitionTo(Menu.MultiplayerGameType);
                                        else {
                                            AlertBox.showAlert("No LAN server", "Cannot find any LAN servers running. Please try again or enter a server IP manually.");
                                            sp.stopLoading();
                                        }
                                    }
                                });
                            }
                        } else {
                            (new Thread(() -> {
                                final String ipAddr = ipText.getText();
                                final String lanIP = IPAddress.getLAN();

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (ipText.getText().equals("127.0.0.1") || ipText.getText().equals("localhost")) {
                                            m.setIpAddress(lanIP);
                                        } else {
                                            m.setIpAddress(ipAddr);
                                        }

                                        // Transition back to the main menu
                                        if (m.establishConnection())
                                            m.transitionTo(Menu.MultiplayerGameType);
                                        else {
                                            AlertBox.showAlert("No LAN server", "Cannot find any LAN servers running. Please try again or enter a server IP manually.");
                                            sp.stopLoading();
                                        }
                                    }
                                });
                            })).start();


                        }
                    }
                })).start();
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
        m.addButtonHoverSounds(mainGrid);

        // Create a new scene using the main grid
        Scene scene = new Scene(sp, m.width, m.height);
        scene.getStylesheets().add("styles/menu.css");
        scene.getRoot().setStyle("-fx-background-image: url(styles/background.png); -fx-background-size: cover;");
        return scene;
    }
}
