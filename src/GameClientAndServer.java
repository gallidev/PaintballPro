import networkingServer.Server;

/**
 * Created by jack on 13/02/2017.
 */
public class GameClientAndServer {

    public static void main(String[] args) {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                String[] serverArgs = {"25566"};
                Server.main(serverArgs);
            }
        })).start();
        GameClient.main(args);
    }

}
