package it.unibs.pajc.pokeproject;

import java.io.*;
import java.net.*;

public class PKMainClient {
	
	private static Socket socket;
	private Pokemon chosenPokemon;
	
	public static void main(String[] args) {
		connectToServer();

	}
	
	public void sendChosenPokemon(Pokemon chosen) {
		// implementazione socket 
	}
	
	public static void connectToServer() {
		try {
			socket = new Socket("10.0.83.63", 50000);
			System.out.println("Successfully connected to server at" + socket.getInetAddress());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
