package gui;

import helpers.JavaFXTestHelper;
import javafx.application.Platform;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jack on 02/03/2017.
 */
public class TestAlertBox {
    @Test
    public void showAlert() throws Exception {
        JavaFXTestHelper.setupApplication();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AlertBox.showAlert("Example Alert", "Press Ok");
            }
        });

        Thread.sleep(100);

    }

}