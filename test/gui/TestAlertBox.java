package gui;

import com.sun.javafx.stage.StageHelper;
import helpers.JavaFXTestHelper;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
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
            AlertBox ab = new AlertBox("Example Alert", "Testing - This will automatically hide");
            ab.showAlert(false);
        });

        JavaFXTestHelper.waitForPlatform();

    }

}