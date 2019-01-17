package it.unibs.pajc.pokeproject;

import java.io.*;
import java.net.*;

public class PKServerProtocol extends Thread{
	private Socket socket;
	private BufferedReader fromClient;
	private PrintWriter toClient;
	
	public PKServerProtocol(Socket socket) {
		this.socket = socket;
		this.start();
	}
	
	public void run() {
		System.out.println("Serving client with address "+ socket.getInetAddress());
		try {		
			fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			toClient = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			String request;
			while((request = fromClient.readLine()) != null) {
				System.out.println("Server received " + request + " damage from " + socket.getInetAddress());
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
