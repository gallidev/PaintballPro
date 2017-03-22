package gui;

import helpers.JavaFXTestHelper;
import javafx.application.Platform;
import org.junit.Test;

/**
 * Test for showing an Alert Box
 *
 * @author Jack Hughes
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
            ab.showAlert(false);
            ab.dismiss();
        });

        JavaFXTestHelper.waitForPlatform();

    }

}