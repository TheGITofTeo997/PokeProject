package it.unibs.pajc.pokeproject;

import java.io.*;
import java.net.*;
import java.util.*;

public class PKServerProtocol extends Thread{
	private Socket socketPlayer;
	private BufferedReader fromClient;
	private PrintWriter toClient;
	private static HashMap<Integer, java.net.Socket> clientList = new HashMap<>();
	private static ArrayList<Integer> clientPorts = new ArrayList<>();

	
	public PKServerProtocol(Socket socket) {
		this.socketPlayer = socket;
		this.start();
	}
	
	public void run() {
		System.out.println("Serving client with address "+ socketPlayer.getInetAddress());
		clientList.put(socketPlayer.getPort(), socketPlayer);
		try {		
			fromClient = new BufferedReader(new InputStreamReader(socketPlayer.getInputStream()));
			String request;		
			int key=0;
			for(Iterator<Integer> iter = clientList.keySet().iterator(); iter.hasNext(); )
			{
				key = iter.next();		
				System.out.println(clientList.get(key));
			}
			clientPorts.add(key);	
			for(int j=0;j<clientPorts.size();j++) System.out.println(clientPorts.get(j));
			while((request = fromClient.readLine()) != null) {
			if(socketPlayer.getPort() == clientPorts.get(0)) {
				int port_receiver = clientPorts.get(1);
				toClient = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientList.get(port_receiver).getOutputStream())),true);
				toClient.println(request);
			}else if(socketPlayer.getPort() == clientPorts.get(1)){
				int port_receiver = clientPorts.get(0);
				toClient = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientList.get(port_receiver).getOutputStream())),true);
				toClient.println(request);
			}
			System.out.println("Server received " + request + " damage from " + socketPlayer.getInetAddress());
			System.out.println("Sending " + request);
			toClient.println(request);				
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
