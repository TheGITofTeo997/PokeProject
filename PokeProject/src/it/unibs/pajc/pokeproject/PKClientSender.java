package it.unibs.pajc.pokeproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class PKClientSender extends Thread {

	private PrintWriter toServer;
	private int dataToSend = -1;
	
	public PKClientSender(PrintWriter toServer) {
		this.toServer = toServer;
	}
	
	public void sendData(int data) {
		dataToSend = data;
	}
	
	public void run() {
		try {
			while(dataToSend>-1) {
				toServer.println(dataToSend);
				dataToSend = -1;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
