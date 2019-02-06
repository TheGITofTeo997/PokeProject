package it.unibs.pajc.pokeproject;

import java.io.*;

public class PKClientReceiver extends Thread {
	private static final String ADDED_MESSAGE_TO_THE_QUEUE = "Succesfully added received message to the queue :)";
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
						System.out.println(ADDED_MESSAGE_TO_THE_QUEUE);					
						}	
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
