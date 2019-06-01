package it.unibs.pajc.pokeproject.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import it.unibs.pajc.pokeproject.util.*;

public class PKClientConnector {
	
	private static final int SERVER_PORT = 50000;
	private String serverIp;
	private Socket socket;
	private ScheduledExecutorService checkMessages;
	private PKBattleEnvironment env;
	private ObjectInputStream fromServer;
	private ObjectOutputStream toServer;
	
	public PKClientConnector(PKBattleEnvironment env) {
		this.env = env;
	}
	
	public void connectToServer(String ip) throws Exception { // we probably need to be more specific
		serverIp = ip;
		socket = new Socket(serverIp, SERVER_PORT);
		System.out.println("Successfully connected to server at" + socket.getInetAddress()); //do we need to be this verbose on the client?
		toServer = new ObjectOutputStream(socket.getOutputStream());
		fromServer = new ObjectInputStream(socket.getInputStream());
		
		checkMessages = Executors.newSingleThreadScheduledExecutor();
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
		
	public void sendMessage(PKMessage msg){
		try {
			toServer.writeObject(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//closing the connection
	public void closeConnection() {
		checkMessages.shutdown();
		try {
			socket.close();
			toServer.close();
			fromServer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
