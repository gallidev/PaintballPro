import networkingDiscovery.ServerAnnouncer;
import networkingServer.Server;

/**
 * Created by jack on 13/02/2017.
 */
public class GameClientAndServer {

    public static void main(String[] args) {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                int portNo = 25566;
                String[] serverArgs = {portNo + ""};
//                Thread discovery = new Thread(new ServerAnnouncer(portNo));
//                discovery.start();
                Server.main(serverArgs);
            }
        })).start();
        GameClient.main(args);
    }

}
