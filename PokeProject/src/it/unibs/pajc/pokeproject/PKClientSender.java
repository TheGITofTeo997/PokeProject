package it.unibs.pajc.pokeproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class PKClientSender extends Thread {

	private PrintWriter toServer;
	
	public PKClientSender(PrintWriter toServer) {
		this.toServer = toServer;
	}
	
	public void sendData(int data) {
		toServer.println(data);
	}
	
	public void run() {
		try {
			String request = "TEST";
			while(true) {
				toServer.println(request);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
