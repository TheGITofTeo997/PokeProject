package it.unibs.pajc.pokeproject.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

import it.unibs.pajc.pokeproject.controller.PKClientReceiver;
import it.unibs.pajc.pokeproject.util.IdentifiedQueue;
import it.unibs.pajc.pokeproject.util.PKMessage;

public class PKClientConnector {
	
	private static final int SERVER_PORT = 50000;
	private String serverIp;
	private Socket socket;
	private PKBattleEnvironment env;
	private ObjectInputStream fromServer;
	private ObjectOutputStream toServer;
	private IdentifiedQueue<PKMessage> toReceive = new IdentifiedQueue<>(5);
	
	public PKClientConnector(PKBattleEnvironment env) {
		this.env = env;
	}
	
	public boolean connectToServer(String ip) {
		try {
			serverIp = ip;
			socket = new Socket(serverIp, SERVER_PORT);
			socket.setKeepAlive(true); // Potrebbe non servire
			System.out.println("Successfully connected to server at" + socket.getInetAddress());
			toServer = new ObjectOutputStream(socket.getOutputStream());
			fromServer = new ObjectInputStream(socket.getInputStream());
			PKClientReceiver receiver = new PKClientReceiver(fromServer, toReceive);
			receiver.start();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		Timer t = new Timer();
		t.schedule(
				new TimerTask() {
					public void run() {
						if(!toReceive.isEmpty()) {
							readMessage();
						}
					}
				}, 0, 1000);
		return socket.isConnected();
	}
	
	// need a better method to verify if the client is connected
	
	public void sendMessage(PKMessage msg) throws IOException {
		toServer.writeObject(msg);
	}
	
	public void readMessage() {
		env.executeCommand(toReceive.poll());
	}
}
