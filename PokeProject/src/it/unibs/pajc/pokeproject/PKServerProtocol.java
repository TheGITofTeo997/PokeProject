package it.unibs.pajc.pokeproject;

import java.io.*;
import java.net.*;
import java.util.*;

/*
 * Il serverprotocol utilizza un hashmap e un arraylist per gestire i 2 client che si
 * connettono. L'hash map contiene come chiave il numero della porta e come valore i socket dei
 * client. 
 * Quando si deve inviare un messaggio da client1 a client2 il server prende il messaggio dalla coda di ricezione di client1
 * e lo mette sulla coda di invio per client2. In realtà ora le operazioni di poll e add vengono fatte da 2 thread separati.
 */
public class PKServerProtocol extends Thread{
	private Socket socketPlayer;
	private ObjectInputStream fromClient; // inputStream su cui si ricevono i messaggi
	private ObjectOutputStream toClient; // outputStream su cui si scrivono i messaggi
	private IdentifiedQueue<PKMessage> inputBuffer; // coda in cui vanno messi i messaggi ricevuti
	private IdentifiedQueue<PKMessage> outputBuffer; // coda da cui vanno presi i messaggi da inviare
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
			toClient = new ObjectOutputStream(socketPlayer.getOutputStream());
			fromClient = new ObjectInputStream(socketPlayer.getInputStream());
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
			PKServerSender sender = new PKServerSender(toClient, outputBuffer); // Creazione thread per invio messaggi
			sender.start(); 
			PKServerReceiver receiver = new PKServerReceiver(fromClient, inputBuffer, idGen); // Creazione thread ricezione messaggi
			receiver.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	// Questo metodo serve per settare la coda in cui verranno messi i messaggi ricevuti dal client
	public void setInputBuffer(IdentifiedQueue<PKMessage> inputBuffer) {
		this.inputBuffer = inputBuffer;
	}

	// Questo metodo serve per settare la coda da cui verranno presi i messaggi da inviare al client
	public void setOutputBuffer(IdentifiedQueue<PKMessage> outputBuffer) {
		this.outputBuffer = outputBuffer;
	}

	// Questo metodo server per restituire l'id del protocollo
	public int getIdCounter() {
		return idCounter;
	}
	
	

}
