package gameNetworking;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import networkingServer.ClientTable;
import networkingServer.LobbyTable;
import players.ServerBasicPlayer;

// One per server.
public class UDPServerReceiver extends Thread{

	private boolean debug = false;
	private ClientTable clients;
	private LobbyTable lobby;
	
	// when we get a new connection, add ip to client table 
	public UDPServerReceiver(ClientTable clientTable, LobbyTable lobby) {
		clients = clientTable;
		this.lobby = lobby;
	}
	
	public void run()
	{
		try {
			DatagramSocket serverSocket = new DatagramSocket(9876);
			byte[] receiveData = new byte[2048];
			//byte[] sendData = new byte[2048];
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
			    	  // We assume that all players in a game are connected and their ip addresses inserted.
			    	  
			    	  // Do any extra processing here for in-game commands.
			    	  
			    	  /*
				    	  String capitalizedSentence = sentence.toUpperCase();
				    	  sendData = capitalizedSentence.getBytes();
				    	  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				    	  serverSocket.send(sendPacket);
				    	  //.split(":")[0];
			    	   */
			      }
			}
			serverSocket.close();
		} catch(Exception e)
		{
			//
		}
		
	}

	// Let's broadcast to all members of the same game - given a lobby id.
	public void sendToAll(String toBeSent, int lobbyID) {
		byte[] sendData = new byte[2048];
		
		ServerBasicPlayer[] players = lobby.getLobby(lobbyID).getPlayers();
		for(ServerBasicPlayer player : players)
		{
			int id = player.getID();
			String playerIP = clients.getIP(id);
			// Parse IP to get first part and port number.
			String ipAddr = playerIP.split(":")[0];
			String ipPort = playerIP.split(":")[1];
			
		}
		
	}
	// Let's broadcast to all members of the same game - given ip of member.
	public void sendToAll(String toBeSent, String ip)
	{
		int lobbyID = clients.getPlayer(clients.getID(ip)).getAllocatedLobby();
		sendToAll(toBeSent,lobbyID);
	}

}
