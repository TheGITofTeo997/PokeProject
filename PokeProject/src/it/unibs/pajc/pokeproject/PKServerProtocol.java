package it.unibs.pajc.pokeproject;

import java.io.*;
import java.net.*;
import java.util.*;

public class PKServerProtocol extends Thread{
	private Socket socketPlayer;
	private BufferedReader fromClient;
	private PrintWriter toClient;
	private HashMap<Integer, java.net.Socket> clientList = new HashMap<>();
	private static int i=0;

	
	public PKServerProtocol(Socket socket) {
		this.socketPlayer = socket;
		this.start();
	}
	
	public void run() {
		System.out.println("Serving client with address "+ socketPlayer.getInetAddress());
		clientList.put(i++, socketPlayer);
		try {		
			fromClient = new BufferedReader(new InputStreamReader(socketPlayer.getInputStream()));
			toClient = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketPlayer.getOutputStream())), true);
			String request;
			while((request = fromClient.readLine()) != null) {
				System.out.println("Server received " + request + " damage from " + socketPlayer.getInetAddress());
				System.out.println("Sending " + request);
				toClient.println(request);
				System.out.println(clientList.get(0));
				System.out.println(clientList.get(1));
				
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
