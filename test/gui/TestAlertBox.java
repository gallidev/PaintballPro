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

        Platform.runLater(() -> AlertBox.showAlert("Example Alert", "Press Ok"));

        Thread.sleep(100);

    }

}