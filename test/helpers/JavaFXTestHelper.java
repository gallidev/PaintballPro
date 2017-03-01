package helpers;

import javafx.embed.swing.JFXPanel;

/**
 * Created by jack on 01/03/2017.
 */
public class JavaFXTestHelper {
    public static void setupApplication() {
        try {
            Thread t = new Thread(() -> new JFXPanel());
            t.start();
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("JavaFXTestHelper thread interrupted");
        }
    }
}
