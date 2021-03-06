package it.unibs.pajc.pokeproject.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import it.unibs.pajc.pokeproject.util.*;

public class PKClientConnector {
	
	private static final int SERVER_PORT = 50000;
	private String serverIp;
	private Socket socket;
	private ScheduledExecutorService checkMessages;
	private MultiplayerModel model;
	private ObjectInputStream fromServer;
	private ObjectOutputStream toServer;
	private Logger logger;
	
	public PKClientConnector(MultiplayerModel model, Logger logger) {
		this.model = model;
		this.logger = logger;
	}
	
	public void connectToServer(String ip) throws Exception { 
		serverIp = ip;
		socket = new Socket(serverIp, SERVER_PORT);
		toServer = new ObjectOutputStream(socket.getOutputStream());
		fromServer = new ObjectInputStream(socket.getInputStream());
		
		checkMessages = Executors.newSingleThreadScheduledExecutor();
		checkMessages.scheduleAtFixedRate(new Runnable() {
			public void run() {
				try {
					PKMessage msg = (PKMessage)fromServer.readObject();
					model.executeCommand(msg);
				} catch (SocketException e) {
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		}, 0, 1, TimeUnit.SECONDS);
		logger.writeLog(PKClientStrings.CHECK_MESSAGES);
		
		PKMessage testConnection = new PKMessage(Commands.MSG_TEST_CONNECTION);
		sendMessage(testConnection);
	}
		
	public void sendMessage(PKMessage msg){
		try {
			toServer.writeObject(msg);
		} catch (IOException e) {
			logger.writeLog(PKClientStrings.MESSAGE_SENDING_FAILURE + e.toString());
		}
	}
	
	//closing the connection
	public void closeConnection() {
		checkMessages.shutdown();
		try {
			fromServer.close();
			toServer.close();
			socket.close();
			logger.writeLog(PKClientStrings.RESOURCES_CLOSED);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
