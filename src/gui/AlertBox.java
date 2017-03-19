package gui;

import javafx.scene.control.Alert;

/**
 * Class for showing alert/warning boxes
 */
public class AlertBox {

    private String title;
    private String message;
    protected Alert alert;

    /**
     * Construct a new alert box
     * @param title title of alert box
     * @param message message of alert box
     */
    public AlertBox(String title, String message) {
        this.title = title;
        this.message = message;
    }

    /**
     * Show the alert
     */
    public void showAlert() {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Dismiss the alert
     */
    public void dismissAlert() {
        if (alert != null && alert.isShowing())
            alert.close();
    }

}
