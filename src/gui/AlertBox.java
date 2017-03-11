package gui;

import javafx.scene.control.Alert;

/**
 * Helper class for alert/warning boxes
 */
public class AlertBox {

    /**
     * Show an alert
     * @param title title of alert
     * @param message message of alert
     */
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
