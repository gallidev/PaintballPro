package gui;

import helpers.JavaFXTestHelper;
import javafx.application.Platform;
import org.junit.Test;

/**
 * Test for showing an Alert Box
 */
public class TestAlertBox {

    /**
     * Test for showing an alert
     * @throws Exception error in test case
     */
    @Test
    public void showAlert() throws Exception {
        JavaFXTestHelper.setupApplication();

        Platform.runLater(() -> {
            AlertBox ab = new AlertBox("Example Alert", "Testing - Press OK");
            (new Thread(() -> {
                Platform.runLater(() -> {
                    ab.showAlert(false);
                });
            })).start();
        });

        Thread.sleep(3000);

    }

}