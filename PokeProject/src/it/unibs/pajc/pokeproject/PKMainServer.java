package it.unibs.pajc.pokeproject;

import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class PKMainServer {
	private static TreeMap<Integer, Pokemon> pkDatabase = new TreeMap<>();
	private static ArrayList<Pokemon> loadedPkmn = new ArrayList<>();
	private static Pokemon trainerPoke0;
	private static Pokemon trainerPoke1;
	private static ArrayList<IdentifiedQueue<PKMessage>> fromQueues = new ArrayList<>();
	private static ArrayList<IdentifiedQueue<PKMessage>> toQueues = new ArrayList<>();
	private static final int QUEUE_LIST_SIZE=2;
	
	
	//Questo era il vecchio main, ora non è più entrypoint poichè viene fatto dalla window
	public static void serverStart() {
		initialize();
		
	}
	
	private static void setupQueues() {
		fromQueues.add(new IdentifiedQueue<>(10));
		fromQueues.add(new IdentifiedQueue<>(10));
		toQueues.add(new IdentifiedQueue<>(10));
		toQueues.add(new IdentifiedQueue<>(10));
	}
	
	public static void executeCommand(PKMessage msg) {
		switch(msg.getCommandBody()) {
		case "msg_selected_pokemon":
			break;
		case "msg_selected_move":
			break;
		case "msg_rematch_yes":
			break;
		case "msg_rematch_no":
			break;	
		}
	}
			
	public static void loadPkmn() {
		Pokemon bulbasaur = new Pokemon("Bulbasaur", "Erba");
		Pokemon charmander = new Pokemon("Charmander", "Fuoco");		
		Pokemon squirtle = new Pokemon ("Squirtle", "Acqua");
		Pokemon chikorita = new Pokemon("Chikorita", "Erba");
		Pokemon cyndaquil = new Pokemon("Cyndaquil", "Fuoco");
		Pokemon totodile = new Pokemon("Totodile", "Acqua");
		loadedPkmn.add(bulbasaur);
		loadedPkmn.add(charmander);
		loadedPkmn.add(squirtle);
		loadedPkmn.add(chikorita);
		loadedPkmn.add(cyndaquil);
		loadedPkmn.add(totodile);
	}
	
	
	public static void initialize() {
		checkForFileExistance();
	    openConnection();

	    
	}
	
	private static void checkForFileExistance() {
		File pkDbase = new File("pkDatabase.dat");
		if(pkDbase.exists()) {
			try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pkDbase))){
				pkDatabase = (TreeMap<Integer, Pokemon>)ois.readObject();
				PKServerWindow.appendTextToConsole("\nLoaded PK treemap...");
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
			loadPkmn();
			for(int i=0; i<loadedPkmn.size(); i++)
				pkDatabase.put(loadedPkmn.get(i).getID(), loadedPkmn.get(i));
			try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pkDbase))){
				oos.writeObject(pkDatabase);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void openConnection() {
		ServerSocket server;
		try {
			int i=0;
			server = new ServerSocket(50000);
			PKServerWindow.appendTextToConsole("\nServer started on port 50000...");
			while(true) {  
		    Socket client = server.accept();
		    PKServerProtocol protocol = new PKServerProtocol(client);
		    	if(fromQueues.get(i).getId()==-1) {
		    		protocol.setInputBuffer(fromQueues.get(i));
		    		fromQueues.get(i).setId(protocol.getIdCounter());
		    	}
		    	else {
		    		protocol.setInputBuffer(fromQueues.get(++i));
		    		fromQueues.get(i).setId(protocol.getIdCounter());
		    	}
		    	i=0;
		    	if(toQueues.get(i).getId()==-1) {
		    		protocol.setOutputBuffer(toQueues.get(i));
		    		toQueues.get(i).setId(protocol.getIdCounter());
		    	}
		    	else {
		    		protocol.setOutputBuffer(toQueues.get(++i));
		    		toQueues.get(i).setId(protocol.getIdCounter());
		    	}   	
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	public int setPriorityBattle(Pokemon p0, Pokemon p1) {
		int speedP0 = p0.getSpeed();
		int speedP1 = p1.getSpeed();
		if(speedP0>=speedP1) {
			return ID0;
		}
		else
			return ID1;
	}*/
	
	public int damageToSend(int pwrAttack) {
		int damageToSend=0;
		return damageToSend;
	}
	
	
	
	//Il server riceverà in ingresso i due pkmn e ad ognuno assegnerà l'ID, in base al quale
	//attaccheranno e verranno identificati all'interno dei metodi.
	
}
