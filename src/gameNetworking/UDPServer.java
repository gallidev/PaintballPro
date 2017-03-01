package gameNetworking;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import networkingServer.ClientTable;
import networkingServer.LobbyTable;
import players.ServerBasicPlayer;

// One per server.
public class UDPServer extends Thread{

	private boolean debug = false;
	private ClientTable clients;
	private LobbyTable lobby;
	private DatagramSocket serverSocket;
	
	// when we get a new connection, add ip to client table 
	public UDPServer(ClientTable clientTable, LobbyTable lobby) {
		clients = clientTable;
		this.lobby = lobby;
	}
	
	public void run()
	{
		try {
			serverSocket = new DatagramSocket(9876);
			byte[] receiveData = new byte[1024];
			while(true)
			{
			      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			      
			      serverSocket.receive(receivePacket);
			      String sentence = new String( receivePacket.getData());
			      
			      if(debug)System.out.println("RECEIVED: " + sentence);
			      
			      InetAddress IPAddress;
			      int port;
			     
			      if(sentence.contains("Exit"))
			    	  break;
			      else if(sentence.contains("Connect:"))
			      {
			    	  IPAddress = receivePacket.getAddress();
			    	  port = receivePacket.getPort();
			    	  int clientID = Integer.parseInt(sentence.substring(8));
			    	  clients.addNewIP(IPAddress.toString(), clientID);
			    	  clients.addUDPQueue(IPAddress.toString());
			      }
			      else
			      {
			    	  // We assume that all players in a game are now connected and their ip addresses inserted into clientTable.
			    	  // if they are not, we will not be able to send messaged to them using sendToAll.
			    	  
			    	  // Do any extra processing here for in-game commands.
			    	  
			      }
			}
		} catch(Exception e)
		{
			if(debug) System.err.println(e.getStackTrace());
		}
		// TODO - Let's do some closing stuff here.
		serverSocket.close();
	}

	// Let's broadcast to all members of the same game - given a lobby id.
	public void sendToAll(String toBeSent, int lobbyID) {
		byte[] sendData = new byte[1024];
		sendData = toBeSent.getBytes();
		
		// We get all players in the same game as the transmitting player.
		ServerBasicPlayer[] players = lobby.getLobby(lobbyID).getPlayers();
		// Let's send a message to them all.
		for(ServerBasicPlayer player : players)
		{
			int id = player.getID();
			String playerIP = clients.getIP(id);
			// Parse IP to get first part and port number.
			String ipAddr = playerIP.split(":")[0];
			String ipPort = playerIP.split(":")[1];
			try{
				// Let's send the message.
				InetAddress sendAddress = InetAddress.getByName(ipAddr);
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, sendAddress, Integer.parseInt(ipPort));
				serverSocket.send(sendPacket);
			}
			catch(Exception e)
			{
				if(debug) System.out.println("Cannot send message:"+toBeSent+", to:" +ipAddr);
			}
		}
		
	}
	// Let's broadcast to all members of the same game - given ip of member.
	public void sendToAll(String toBeSent, String ip)
	{
		// we get the lobby id.
		int lobbyID = clients.getPlayer(clients.getID(ip)).getAllocatedLobby();
		// we can now send to all clients in the same lobby as the origin client.
		sendToAll(toBeSent,lobbyID);
	}
}