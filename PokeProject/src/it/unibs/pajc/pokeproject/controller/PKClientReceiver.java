package it.unibs.pajc.pokeproject.controller;

import java.io.*;

import it.unibs.pajc.pokeproject.util.IdentifiedQueue;
import it.unibs.pajc.pokeproject.util.PKMessage;

public class PKClientReceiver extends Thread {
	private static final String ADDED_MESSAGE_TO_THE_QUEUE = "[i]Received message added to the queue correctly. Proceeding..."; //do we need to be this verbose on client?
	private ObjectInputStream fromServer;
	private IdentifiedQueue<PKMessage> toReceive;
	
	public PKClientReceiver(ObjectInputStream fromServer, IdentifiedQueue<PKMessage> toReceive) {
		this.fromServer = fromServer;
		this.toReceive = toReceive;
	}
	
	
	public void run() {
		try {
			while(true) {
				if(toReceive.isEmpty())
				{
					PKMessage receivedMsg = (PKMessage)fromServer.readObject();
					if(toReceive.add(receivedMsg)) {
						System.out.println(ADDED_MESSAGE_TO_THE_QUEUE + receivedMsg.getCommandBody());					
						}	
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
