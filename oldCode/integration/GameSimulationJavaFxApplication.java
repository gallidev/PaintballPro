package oldCode.integration;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Class that creates and sets the scene for a new game.
 *
 */
public class GameSimulationJavaFxApplication extends Application {
	static Pane view = new Pane();

   @Override
    public void start(Stage stage) {
        Scene scene = new Scene(view,0, 0);
        stage.setScene(scene);
        stage.show();
    }
}
