package helpers;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import java.util.concurrent.CountDownLatch;

/**
 * Helper for JavaFX-based tests
 */
public class JavaFXTestHelper {

    /**
     * Helper method to start the JavaFX Application thread
     */
    public static void setupApplication() {
        try {
            Thread t = new Thread(JFXPanel::new);
            t.start();
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("JavaFXTestHelper thread interrupted");
        }
    }

    public static void waitForPlatform() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> { latch.countDown(); });
        latch.await();
    }

}
