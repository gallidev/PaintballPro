import networkingDiscovery.DiscoveryServerAnnouncer;
import networkingServer.Server;

public class GameServer {
	public static void main(String[] args) {
		int portNo = 25566;
		String[] serverArgs = {portNo + ""};
		Thread discovery = new Thread(new DiscoveryServerAnnouncer(portNo));
		discovery.start();
		Server.main(serverArgs);
	}
}
