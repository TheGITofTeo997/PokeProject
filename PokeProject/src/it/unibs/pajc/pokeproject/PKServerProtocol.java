package it.unibs.pajc.pokeproject;

import java.io.*;
import java.net.*;

public class PKServerProtocol extends Thread{
	private Socket socket;
	
	public PKServerProtocol(Socket socket) {
		this.socket = socket;
		this.start();
	}
	
	public void run() {
		System.out.println("Serving client with address "+ socket.getInetAddress());
		try {		
			ObjectInputStream inFromClient = new ObjectInputStream(socket.getInputStream());		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
