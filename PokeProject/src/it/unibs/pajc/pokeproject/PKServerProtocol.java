package it.unibs.pajc.pokeproject;

import java.io.*;
import java.net.*;
import java.util.*;

/*
 * Il serverprotocol utilizza un hashmap e un arraylist per gestire i 2 client che si
 * connettono. L'hash map contiene come chiave il numero della porta e come valore i socket dei
 * client. 
 * Quando si deve inviare un messaggio da client1 a client2 il server semplicemente legge la porta di client1
 * controlla l'hashmap prende il socket della porta del client2, creando un printwriter, e invia il messaggio
 * a client2.
 * 
 * 
 */
public class PKServerProtocol extends Thread{
	private Socket socketPlayer;
	private ObjectInputStream fromClient;
	private ObjectOutputStream toClient;
	private IdentifiedQueue<PKMessage> inputBuffer;
	private IdentifiedQueue<PKMessage> outputBuffer;
	private int idCounter;
	private static HashMap<Integer, java.net.Socket> clientList = new HashMap<>(); //hashmap contenente i socket dei client
	private static ArrayList<Integer> clientPorts = new ArrayList<>(); //arraylist contenente porte dei client
	private static int idGen=0;

	
	
	public PKServerProtocol(Socket socket) {
		this.socketPlayer = socket;
		this.start();
		idCounter = idGen;
		idGen++;
	}
	
	public void run() {
		PKServerWindow.appendTextToConsole("\nServing client with address "+ socketPlayer.getInetAddress());
		clientList.put(socketPlayer.getPort(), socketPlayer);//aggiunta del client all'hashmap
		try {		
			fromClient = new ObjectInputStream(socketPlayer.getInputStream());
			toClient = new ObjectOutputStream(socketPlayer.getOutputStream());
			//iteratore per scorrere hashmap e aggiungere l'elenco delle porte all'arraylist
			int key=0;
			for(Iterator<Integer> iter = clientList.keySet().iterator(); iter.hasNext(); )
			{
				key = iter.next();		
				PKServerWindow.appendTextToConsole("\n" + clientList.get(key).toString());
			}
			clientPorts.add(key);	
			//stampa arraylist
			for(int j=0;j<clientPorts.size();j++) 
				PKServerWindow.appendTextToConsole("\n" + clientPorts.get(j).toString());		
			while(true) {			
				PKMessage receivedMsg = (PKMessage)fromClient.readObject();
				receivedMsg.setClientID(idCounter);
				inputBuffer.add(receivedMsg);
				PKServerWindow.appendTextToConsole("\nServer received " + receivedMsg.getCommandBody() + " from " + socketPlayer.getInetAddress());
				PKMessage toSendMsg = outputBuffer.poll();
				toClient.writeObject(toSendMsg);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setInputBuffer(IdentifiedQueue<PKMessage> inputBuffer) {
		this.inputBuffer = inputBuffer;
	}

	public void setOutputBuffer(IdentifiedQueue<PKMessage> outputBuffer) {
		this.outputBuffer = outputBuffer;
	}

	public int getIdCounter() {
		return idCounter;
	}
	
	

}
