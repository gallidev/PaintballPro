import gui.ServerGUI;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import networking.discoveryNew.DiscoveryServerAnnouncer;
import networking.discoveryNew.IPAddress;
import networking.server.Server;

/**
 * Class for running a game server
 */
public class GameServer extends Application {

	private ServerGUI gui = new ServerGUI();
	private Server server;
	private Thread discovery;

	/**
	 * Start the application
	 * @param stage the primary stage to use for the GUI
	 * @throws Exception exception when creating the application
	 */
	@Override
	public void start(Stage stage) throws Exception {
		Font.loadFont(getClass().getResourceAsStream("styles/fonts/roboto-slab/RobotoSlab-Regular.ttf"), 16);
		stage.getIcons().addAll(new Image("assets/icon_dock.png"), new Image("assets/icon_32.png"), new Image("assets/icon_16.png"));
		stage.setScene(gui);
		stage.setTitle("Paintball Pro Server");
		stage.setOnCloseRequest((event) -> {
			if (server != null)
				try {
					server.serverSocket.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			if (discovery != null)
				discovery.interrupt();
			System.exit(0);
		});
		stage.show();
		(new Thread(() -> {
			int portNo = 25566;
			DiscoveryServerAnnouncer discovery = new DiscoveryServerAnnouncer();
			discovery.start();
			server = new Server(portNo, IPAddress.getLAN(), gui, 0);
			server.start();
			gui.setServer(server, discovery);
		})).start();

	}

	/**
	 * Main method for starting the server
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}