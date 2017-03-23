package gui;

import javafx.scene.control.Alert;

/**
 * Class for showing alert/warning boxes
 *
 * @author Jack Hughes
 */
public class AlertBox {

	private String title;
	private String message;
	private Alert alert;

	/**
	 * Construct a new alert box
	 *
	 * @param title   title of alert box
	 * @param message message of alert box
	 */
	public AlertBox(String title, String message) {
		this.title = title;
		this.message = message;
	}

	/**
	 * Show the alert
	 *
	 * @param blocking true if the GUI should wait for the alert to be dismissed
	 */
	public void showAlert(boolean blocking) {
		alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setContentText(message);
		if (blocking)
			alert.showAndWait();
		else
			alert.show();
	}

}
