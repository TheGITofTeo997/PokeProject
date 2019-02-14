package it.unibs.pajc.pokeproject.controller;

import java.io.*;

import it.unibs.pajc.pokeproject.util.IdentifiedQueue;
import it.unibs.pajc.pokeproject.util.PKMessage;

public class PKServerSender extends Thread{
	
	private ObjectOutputStream toClient;
	private IdentifiedQueue<PKMessage> toSend;
	
	public PKServerSender (ObjectOutputStream toClient, IdentifiedQueue<PKMessage> toSend) {
		this.toClient = toClient;
		this.toSend = toSend;
	}
		
	public void run() {
		try {
			while(true) {
				if(!toSend.isEmpty())
					toClient.writeObject(toSend.poll());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}

