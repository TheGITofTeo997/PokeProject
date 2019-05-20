package it.unibs.pajc.pokeproject.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import it.unibs.pajc.pokeproject.util.Commands;
import it.unibs.pajc.pokeproject.util.PKMessage;
import it.unibs.pajc.pokeproject.util.PKServerStrings;

public class PKClientConnector {
	
	private static final int SERVER_PORT = 50000;
	private String serverIp;
	private Socket socket;
	private PKBattleEnvironment env;
	private ObjectInputStream fromServer;
	private ObjectOutputStream toServer;
	private ArrayBlockingQueue<PKMessage> toReceive = new ArrayBlockingQueue<>(5);
	
	public PKClientConnector(PKBattleEnvironment env) {
		this.env = env;
	}
	
	public void connectToServer(String ip) throws Exception { // we probably need to be more specific
		serverIp = ip;
		socket = new Socket(serverIp, SERVER_PORT);
		socket.setKeepAlive(true); // Potrebbe non servire
		System.out.println("Successfully connected to server at" + socket.getInetAddress()); //do we need to be this verbose on the client?
		toServer = new ObjectOutputStream(socket.getOutputStream());
		fromServer = new ObjectInputStream(socket.getInputStream());
		
		ScheduledExecutorService checkMessages = Executors.newSingleThreadScheduledExecutor();
		checkMessages.scheduleAtFixedRate(new Runnable() {
			public void run() {
				try {
					PKMessage msg = (PKMessage)fromServer.readObject();
					env.executeCommand(msg);
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		}, 0, 1, TimeUnit.SECONDS);
		
		PKMessage testConnection = new PKMessage(Commands.MSG_TEST_CONNECTION);
		sendMessage(testConnection);
	}
	
	// need a better method to verify if the client is connected
	
	public void sendMessage(PKMessage msg){
		try {
			toServer.writeObject(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
