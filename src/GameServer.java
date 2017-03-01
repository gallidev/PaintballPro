import networkingDiscovery.ServerAnnouncer;
import networkingServer.Server;

public class GameServer {
	public static void main(String[] args) {
		int portNo = 25566;
		String[] serverArgs = {portNo + "", "127.0.0.1"};
		Thread discovery = new Thread(new ServerAnnouncer(portNo));
		discovery.start();
		Server.main(serverArgs);
	}
}
