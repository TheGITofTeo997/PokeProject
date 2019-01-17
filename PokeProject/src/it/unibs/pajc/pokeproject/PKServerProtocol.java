package it.unibs.pajc.pokeproject;

import java.io.*;
import java.net.*;

public class PKServerProtocol extends Thread{
	private Socket socketPlayer1;
	private Socket socketPlayer2;
	private BufferedReader fromClient1;
	private PrintWriter toClient1;
	private BufferedReader fromClient2;
	private PrintWriter toClient2;
	
	public PKServerProtocol(Socket socket) {
		this.socketPlayer1 = socket;
		this.start();
	}
	
	public void run() {
		System.out.println("Serving client with address "+ socketPlayer1.getInetAddress());
		try {		
			fromClient1 = new BufferedReader(new InputStreamReader(socketPlayer1.getInputStream()));
			toClient1 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketPlayer1.getOutputStream())), true);
			fromClient2 = new BufferedReader(new InputStreamReader(socketPlayer2.getInputStream()));
			toClient2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketPlayer2.getOutputStream())), true);
			String request;
			while((request = fromClient1.readLine()) != null) {
				System.out.println("Server received " + request + " damage from " + socketPlayer1.getInetAddress());
				System.out.println("Sending " + request);
				toClient2.println(request);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
