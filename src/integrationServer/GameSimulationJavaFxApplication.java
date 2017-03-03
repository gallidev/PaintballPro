package integrationServer;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import enums.TeamEnum;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import networking.server.ServerReceiver;
import physics.CollisionsHandler;
import serverLogic.Team;
import players.ServerMinimumPlayer;
import players.UserPlayer;
import rendering.ImageFactory;
import rendering.Map;


public class GameSimulationJavaFxApplication extends Application {
	static Pane view = new Pane();

   @Override
    public void start(Stage stage) {
        Scene scene = new Scene(view,0, 0);
        stage.setScene(scene);
        stage.show();
    }
}
