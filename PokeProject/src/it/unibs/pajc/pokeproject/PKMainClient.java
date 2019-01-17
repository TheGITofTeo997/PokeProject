package it.unibs.pajc.pokeproject;

import java.io.*;
import java.net.*;

public class PKMainClient {
	
	private static Socket socket;
	private static Pokemon chosenPokemon;
	private static BufferedReader fromServer;
	private static PrintWriter toServer;
	
	public static void main(String[] args) {
		connectToServer();

	}
	
	public static void sendChosenPokemon(Pokemon chosen) {
		// implementazione socket 
	}
	
	public static void connectToServer() {
		try {
			socket = new Socket("25.101.251.30", 50000);
			System.out.println("Successfully connected to server at" + socket.getInetAddress());
			fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			toServer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
			String request, response;
			while((request = stdin.readLine()) != null) {
				toServer.println(request);
				response = fromServer.readLine();
				System.out.println("Received " + response + " from server");
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
