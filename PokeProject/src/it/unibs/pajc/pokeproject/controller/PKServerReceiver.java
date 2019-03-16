package it.unibs.pajc.pokeproject.controller;

import java.io.*;

import it.unibs.pajc.pokeproject.util.IdentifiedQueue;
import it.unibs.pajc.pokeproject.util.PKMessage;
import it.unibs.pajc.pokeproject.view.PKServerWindow;

public class PKServerReceiver extends Thread {
	
	//Local Components
	private static final String MSG_ADDED_CORRECTLY = "\nMessage added to the queue correctly :)";
	private ObjectInputStream fromClient;
	private IdentifiedQueue<PKMessage> toReceive;
	private int clientID;
	
	//View Components
	private PKServerWindow view;
	
	public PKServerReceiver (ObjectInputStream fromClient, IdentifiedQueue<PKMessage> toReceive, int clientID, PKServerWindow view) {
		this.fromClient = fromClient;
		this.toReceive = toReceive;
		this.clientID = clientID;
		this.view = view;
	}
	
	public void run() {
		try {
			while(true) {
				if(toReceive.isEmpty())
				{
					PKMessage receivedMsg = (PKMessage)fromClient.readObject();
					view.appendTextToConsole("\nServer received " + receivedMsg.getCommandBody() + "from " + (1+clientID));
					receivedMsg.setClientID(clientID);
					if(toReceive.add(receivedMsg)) view.appendTextToConsole(MSG_ADDED_CORRECTLY);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}