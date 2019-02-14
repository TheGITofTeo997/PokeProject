package it.unibs.pajc.pokeproject.controller;

import java.io.*;

import it.unibs.pajc.pokeproject.util.IdentifiedQueue;
import it.unibs.pajc.pokeproject.util.PKMessage;

public class PKClientSender extends Thread {

	private ObjectOutputStream toServer;
	private IdentifiedQueue<PKMessage> toSend;
	
	public PKClientSender(ObjectOutputStream toServer, IdentifiedQueue<PKMessage> toSend) {
		this.toServer = toServer;
		this.toSend = toSend;
	}
		
	public void run() {
		try {
			while(true) {
				if(!toSend.isEmpty())
					toServer.writeObject(toSend.poll());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
