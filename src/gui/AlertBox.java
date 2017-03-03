package gui;

import javafx.scene.control.Alert;

/**
 * Created by jack on 02/03/2017.
 */
public class AlertBox {
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
