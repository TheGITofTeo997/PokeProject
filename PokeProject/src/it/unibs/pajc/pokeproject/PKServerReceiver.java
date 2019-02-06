package it.unibs.pajc.pokeproject;

import java.io.*;

public class PKServerReceiver extends Thread {
	private static final String MSG_ADDED_CORRECTLY = "\nMessage added to the queue correctly :)";
	private ObjectInputStream fromClient;
	private IdentifiedQueue<PKMessage> toReceive;
	private int clientID;
	
	public PKServerReceiver (ObjectInputStream fromClient, IdentifiedQueue<PKMessage> toReceive, int clientID) {
		this.fromClient = fromClient;
		this.toReceive = toReceive;
		this.clientID = clientID;
	}
	
	public void run() {
		try {
			while(true) {
				if(toReceive.isEmpty())
				{
					PKMessage receivedMsg = (PKMessage)fromClient.readObject();
					PKServerWindow.appendTextToConsole("\nServer received " + receivedMsg.getCommandBody() + "from " + clientID);
					receivedMsg.setClientID(clientID);
					if(toReceive.add(receivedMsg)) PKServerWindow.appendTextToConsole(MSG_ADDED_CORRECTLY);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}