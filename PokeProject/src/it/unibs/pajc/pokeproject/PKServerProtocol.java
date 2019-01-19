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
 * DA RISOLVERE --> Il client non si aggiorna, dunque il messaggio ricevuto si legge solo dopo l'invio
 * 
 * 
 */
public class PKServerProtocol extends Thread{
	private Socket socketPlayer;
	private BufferedReader fromClient;
	private PrintWriter toClient;
	private static HashMap<Integer, java.net.Socket> clientList = new HashMap<>(); //hashmap contenente i socket dei client
	private static ArrayList<Integer> clientPorts = new ArrayList<>(); //arraylist contenente porte dei client

	
	public PKServerProtocol(Socket socket) {
		this.socketPlayer = socket;
		this.start();
	}
	
	public void run() {
		PKServerWindow.appendTextToConsole("\nServing client with address "+ socketPlayer.getInetAddress());
		clientList.put(socketPlayer.getPort(), socketPlayer);//aggiunta del client all'hashmap
		try {		
			fromClient = new BufferedReader(new InputStreamReader(socketPlayer.getInputStream()));
			String request;	
			//iteratore per scorrere hashmap e aggiungere l'elenco delle porte all'arraylist
			int key=0;
			for(Iterator<Integer> iter = clientList.keySet().iterator(); iter.hasNext(); )
			{
				key = iter.next();		
				PKServerWindow.appendTextToConsole("\n" + clientList.get(key).toString());
			}
			clientPorts.add(key);	
			//stampa arraylist
			for(int j=0;j<clientPorts.size();j++) PKServerWindow.appendTextToConsole("\n" + clientPorts.get(j).toString());
			
			while((request = fromClient.readLine()) != null) {
			//quando riceve un messaggio da un client il server invia il messaggio all'altro client
			//semplicemente cambiando socket
				int port_receiver;
				if(socketPlayer.getPort() == clientPorts.get(0)) {
					port_receiver = clientPorts.get(1);
					toClient = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientList.get(port_receiver).getOutputStream())),true);
				}else if(socketPlayer.getPort() == clientPorts.get(1)){
					port_receiver = clientPorts.get(0);
					toClient = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientList.get(port_receiver).getOutputStream())),true);
				}
				PKServerWindow.appendTextToConsole("\nServer received " + request + " damage from " + socketPlayer.getInetAddress());
				PKServerWindow.appendTextToConsole("\nSending " + request);
				toClient.println(request);				
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
