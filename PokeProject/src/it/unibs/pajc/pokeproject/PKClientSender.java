package it.unibs.pajc.pokeproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class PKClientSender extends Thread {

	private PrintWriter toServer;
	
	public PKClientSender(PrintWriter toServer) {
		this.toServer = toServer;
	}
	
	public void run() {
		try (BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))){
			String request;
			while((request = stdin.readLine()) != null) {
				toServer.println(request);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
