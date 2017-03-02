import networking.discovery.DiscoveryServerAnnouncer;
import networking.server.Server;

public class GameServer {
	public static void main(String[] args) {
		int portNo = 25566;
		String[] serverArgs = {portNo + "", "127.0.0.1"};
		Thread discovery = new Thread(new DiscoveryServerAnnouncer(portNo));
		discovery.start();
		Server.main(serverArgs);
	}
}
