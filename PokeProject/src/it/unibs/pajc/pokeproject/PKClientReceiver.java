package it.unibs.pajc.pokeproject;

import java.io.BufferedReader;

public class PKClientReceiver extends Thread {

	private BufferedReader fromServer;
	
	public PKClientReceiver(BufferedReader fromServer) {
		this.fromServer = fromServer;
	}
	
	public void run() {
		try {
			String response;
			while(true) {
				response = fromServer.readLine();
				System.out.println("Received " + response + " from server");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
