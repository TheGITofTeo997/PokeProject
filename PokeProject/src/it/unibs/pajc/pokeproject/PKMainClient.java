package it.unibs.pajc.pokeproject;

import java.io.*;
import java.net.*;
import java.util.*;

public class PKMainClient{
	
	private static Socket socket;
	private static ObjectInputStream fromServer;
	private static ObjectOutputStream toServer;
	private static ArrayList<Pokemon> loadedPkmn = new ArrayList<>();
	private static TreeMap<Integer, Pokemon> pkDatabase = new TreeMap<>();
	private static IdentifiedQueue<PKMessage> toSend = new IdentifiedQueue<>(10);
	private static IdentifiedQueue<PKMessage> toReceive = new IdentifiedQueue<>(10);
	private static PKMessage test = new PKMessage("msg_test" , 0);
	
	
	public static void main(String[] args) {
		checkForFileExistance();
		connectToServer();

	}
	
	public static void executeCommand(PKMessage msg) {
		switch(msg.getCommandBody()) {
		case "msg_waiting":
			break;
		case "msg_start_battle":
			break;
		case "msg_opponent_pokemon":
			break;
		case "msg_opponent_move":
			break;
		case "msg_received_damage":
			break;
		case "msg_done_damage":
			break;
		case "msg_battle_over":
			break;
		case "msg_rematch":
			break;
		}
	}
		
	private static void loadPkmn() {
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
	
	private static void checkForFileExistance() {
		File pkDbase = new File("pkDatabase.dat");
		if(pkDbase.exists()) {
			try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pkDbase))){
				pkDatabase = (TreeMap<Integer, Pokemon>)ois.readObject();
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
		
	public static void connectToServer() {
		try {
			socket = new Socket("25.101.251.30", 50000);
			System.out.println("Successfully connected to server at" + socket.getInetAddress());
			fromServer = new ObjectInputStream(socket.getInputStream());
			toServer = new ObjectOutputStream(socket.getOutputStream());
			PKClientSender sender = new PKClientSender(toServer, toSend);
			sender.start();
			PKClientReceiver receiver = new PKClientReceiver(fromServer, toReceive);
			receiver.start();
			toSend.add(test);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}