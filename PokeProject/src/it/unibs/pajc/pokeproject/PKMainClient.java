package it.unibs.pajc.pokeproject;

import java.io.*;
import java.net.*;

public class PKMainClient {
	
	private static Socket socket;
	private static BufferedReader fromServer;
	private static PrintWriter toServer;
	private static PrintWriter toServerID;
	
	public static void main(String[] args) {
		connectToServer();

	}
		
	public static void connectToServer() {
		try {
			socket = new Socket("25.101.251.30", 50000);
			System.out.println("Successfully connected to server at" + socket.getInetAddress());
			fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			toServer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			PKClientSender sender = new PKClientSender(toServer);
			sender.start();
			PKClientReceiver receiver = new PKClientReceiver(fromServer);
			receiver.start();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}