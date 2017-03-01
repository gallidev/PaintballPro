package gameNetworking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// One per client.
public class UDPClientReceiver extends Thread {

	private boolean debug = false;
	private int clientID;
	
	DatagramSocket clientSocket;
	InetAddress IPAddress;
	
	// We establish a connection with the UDP server... we tell it we are connecting for the first time so that
	// it stores our information server-side.
	public UDPClientReceiver(int clientID, String udpServIP)
	{
		this.clientID = clientID;
		// Let's establish a connection to the running UDP server and send our client id.
		try{
			clientSocket = new DatagramSocket();
			IPAddress = InetAddress.getByName(udpServIP);
			String sentence = "Connect:"+clientID;
			sendMessage(sentence);
			
		}
		catch (Exception e)
		{
			if(debug) System.err.println(e.getStackTrace());
		}
	}
	
	// We loop, reading messages from the server.
	public void run()
	{
		try{
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			String sendSentence = new String(receivePacket.getData());
			
			// Do any extra processing here for in-game commands.
			
		}
		catch (Exception e)
		{
			if(debug) System.err.println(e.getStackTrace());
		}
		clientSocket.close();
	}
	
	// We send messages to the server.
	public void sendMessage(String msg) 
	{	
		try{
			byte[] sendData = new byte[1024];
			sendData = msg.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
			clientSocket.send(sendPacket);
		}
		catch(Exception e)
		{
			if(debug) System.err.println(e.getStackTrace());
		}
	}
	
	// -------------------------------------
	// -----------Game Methods--------------
	// -------------------------------------
	
}