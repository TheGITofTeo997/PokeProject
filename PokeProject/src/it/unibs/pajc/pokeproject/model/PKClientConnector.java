package it.unibs.pajc.pokeproject.model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.unibs.pajc.pokeproject.controller.PKClientReceiver;
import it.unibs.pajc.pokeproject.controller.PKClientSender;
import it.unibs.pajc.pokeproject.util.IdentifiedQueue;
import it.unibs.pajc.pokeproject.util.PKMessage;

public class PKClientConnector {
	
	private static final int SERVER_PORT = 50000;
	private String serverIp;
	private Socket socket;
	private ObjectInputStream fromServer;
	private ObjectOutputStream toServer;
	private IdentifiedQueue<PKMessage> toSend = new IdentifiedQueue<>(5); //this dimension is still arbitrary btw
	private IdentifiedQueue<PKMessage> toReceive = new IdentifiedQueue<>(5);
	
	public boolean connectToServer(String ip) {
		try {
			serverIp = ip;
			socket = new Socket(serverIp, SERVER_PORT);
			socket.setKeepAlive(true); // Potrebbe non servire
			System.out.println("Successfully connected to server at" + socket.getInetAddress());
			toServer = new ObjectOutputStream(socket.getOutputStream());
			PKClientSender sender = new PKClientSender(toServer, toSend);
			sender.start();
			fromServer = new ObjectInputStream(socket.getInputStream());
			PKClientReceiver receiver = new PKClientReceiver(fromServer, toReceive);
			receiver.start();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return socket.isConnected();
	}
	
	// need a better method to verify if the client is connected
}
