package helpers;

import javafx.embed.swing.JFXPanel;

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

}
