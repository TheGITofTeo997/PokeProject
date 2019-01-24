package it.unibs.pajc.pokeproject;

import java.io.*;

public class PKClientReceiver extends Thread {

	private ObjectInputStream fromServer;
	private IdentifiedQueue<PKMessage> toReceive;
	
	public PKClientReceiver(ObjectInputStream fromServer, IdentifiedQueue<PKMessage> toReceive) {
		this.fromServer = fromServer;
		this.toReceive = toReceive;
	}
	
	public void run() {
		try {
			while(!toReceive.isEmpty()) {
				PKMessage receivedMsg = (PKMessage)fromServer.readObject();
				toReceive.add(receivedMsg);
				System.out.println("Received " + receivedMsg.getCommandBody() + " from server");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
