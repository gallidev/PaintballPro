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
            AlertBox ab = new AlertBox("Example Alert", "Testing");
            (new Thread(() -> {
                Platform.runLater(() -> {
                    ab.showAlert();
                });
            })).start();

            try {
                Thread.sleep(100);
            } catch (Exception e) {
                throw new RuntimeException("Thread could not sleep");
            }

            ab.dismissAlert();
        });

        Thread.sleep(1000);

    }

}