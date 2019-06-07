package it.unibs.pajc.pokeproject.controller;

import java.io.*;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import it.unibs.pajc.pokeproject.util.*;
import it.unibs.pajc.pokeproject.view.PKServerWindow;

public class PKServerProtocol extends Thread {
	
	//Local Components
	
	private Socket socketPlayer;
	private ObjectInputStream fromClient; //inputStream for received messages
	private ObjectOutputStream toClient; //outputStream to write messages
	private ArrayBlockingQueue<PKMessage> toProcess; //queue where to put messages
	private ScheduledExecutorService checkMessages;
	private int clientID;
	private static int idGen=0;
	private boolean connection; //boolean for connection check

	//View Components
	private PKServerWindow view;
	
	public PKServerProtocol(Socket socket, PKServerWindow view) {
		this.socketPlayer = socket;
		this.view = view;
		clientID = idGen;
		idGen++;
		connection = false;
	}
	
	public void run() {
		view.appendTextToConsole(PKServerStrings.SERVING_CLIENT_STRING + socketPlayer.getInetAddress());
		try {		
			toClient = new ObjectOutputStream(socketPlayer.getOutputStream());
			fromClient = new ObjectInputStream(socketPlayer.getInputStream());
			checkMessages = Executors.newSingleThreadScheduledExecutor();
			checkMessages.scheduleAtFixedRate(new Runnable() {
				public void run() {
					try {
						PKMessage msg = (PKMessage)fromClient.readObject();
						view.appendTextToConsole("\nServer received " + msg.getCommandBody() + "from: " + (1+clientID));
						//this if-else is 'used' one time, need to think about it
						if(!connection && msg.getCommandBody() == Commands.MSG_TEST_CONNECTION) {
							connection = true;
							toClient.writeObject(msg);
						}
						else if(msg.getCommandBody() == Commands.MSG_CONNECTION_CLOSED) {
							connection = false;
						}
						else {
							msg.setClientID(clientID);
							if(toProcess.add(msg)) 
								view.appendTextToConsole(PKServerStrings.MSG_ADDED_CORRECTLY);
						}
					} catch (ClassNotFoundException | IOException e) {
					}
				}
			}, 0, 1, TimeUnit.SECONDS);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	// Questo metodo serve per settare la coda in cui verranno messi i messaggi ricevuti dal client
	public void setInputBuffer(ArrayBlockingQueue<PKMessage> inputBuffer) {
		this.toProcess = inputBuffer;
	}

	// Questo metodo server per restituire l'id del protocollo
	public int getClientID() {
		return clientID;
	}
	
	public void sendMessage(PKMessage msg) {
		try {
			toClient.writeObject(msg);
		} catch (IOException e) {
			view.appendTextToConsole(PKServerStrings.MESSAGE_SEND_FAIL + socketPlayer.getInetAddress());
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return connection;
	}
	
	public void setConnectionStatus(boolean connection) {
		this.connection = connection;
	}
	
	public void closeConnection() {
		checkMessages.shutdown();
		try {
			socketPlayer.close();
			fromClient.close();
			toClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}