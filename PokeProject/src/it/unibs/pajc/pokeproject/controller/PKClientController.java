package it.unibs.pajc.pokeproject.controller;

import java.io.*;
import java.net.*;
import java.util.*;

import it.unibs.pajc.pokeproject.model.*;
import it.unibs.pajc.pokeproject.util.*;
import it.unibs.pajc.pokeproject.view.*;

public class PKClientController{
	
	private static final int SERVER_PORT = 50000;
	private String SERVER_IP;
	private Socket socket;
	private ObjectInputStream fromServer;
	private ObjectOutputStream toServer;
	private ArrayList<PKType> typeDatabase;
	private TreeMap<Integer, Pokemon> pkDatabase;
	private IdentifiedQueue<PKMessage> toSend = new IdentifiedQueue<>(5); //this dimension is still arbitrary btw
	private IdentifiedQueue<PKMessage> toReceive = new IdentifiedQueue<>(5);
	//private int selectedID;
	//private static WaitingFrame wf = new WaitingFrame();
	//private static BattleFrame bf = new BattleFrame();
	
	private PKLoader loader;
	private PKClientWindow view;
	
	public PKClientController() {
		loader = new PKLoader();
	}
	
	public void setIP(String IP) {
		this.SERVER_IP = IP;
	}
		
	public void setupClientUtils() {
		typeDatabase = loader.loadTypes();
		pkDatabase = loader.loadPokemon();
	}
	
	public void drawGUI() {
		view = new PKClientWindow();
		view.setController(this);
		view.drawMainPanel();
	}
	
	public boolean connectToServer() {
		try {
			socket = new Socket(SERVER_IP, SERVER_PORT);
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
	
	public void executeCommand(PKMessage msg) {
		switch(msg.getCommandBody()) {
		case MSG_WAITING:
			//waiting();
			break;
		case MSG_WAKEUP:
			//wakeup();
			break;
		case MSG_START_BATTLE:
			//startBattle();
			break;
		case MSG_OPPONENT_POKEMON:
			break;
		case MSG_OPPONENT_MOVE:
			break;
		case MSG_RECEIVED_DAMAGE:
			break;
		case MSG_DONE_DAMAGE:
			break;
		case MSG_BATTLE_OVER:
			break;
		case MSG_REMATCH:
			break;
		default:
			break;
		}
	}
	
	public TreeMap<Integer, Pokemon> getPkDatabase() {
		return pkDatabase;
	}
	
	/*
	private static void waiting() {
		wf.setVisible(true);
	}
	
	private static void wakeup() {
		wf.setVisible(false);
	}
	
	private static void startBattle() {
		
		bf.setVisible(true);
		
	}

	
	public void actionPerformed(ActionEvent e) {
		//start battle
		for(Map.Entry<Integer, Pokemon> entry : pkDatabase.entrySet()) {
			if(e.getActionCommand().equals(entry.getValue().getName())) {
				selectedID = entry.getKey();
			}
		}
		PKMessage msgChosenID = new PKMessage(Commands.MSG_SELECTED_POKEMON, selectedID);
		toSend.add(msgChosenID);
	}
	*/

}