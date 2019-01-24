package it.unibs.pajc.pokeproject;

import java.io.*;
import java.net.*;
import java.util.*;

public class PKMainClient{
	
	private static Socket socket;
	private static BufferedReader fromServer;
	private static PrintWriter toServer;
	private static ArrayList<Pokemon> loadedPkmn = new ArrayList<>();
	private static TreeMap<Integer, Pokemon> pkDatabase = new TreeMap<>();
	
	
	
	public static void main(String[] args) {
		checkForFileExistance();
		connectToServer();

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
				System.out.println("treemap letta xD");
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
			fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			toServer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			PKClientSender sender = new PKClientSender(toServer);
			sender.start();
			sender.sendData(001);
			PKClientReceiver receiver = new PKClientReceiver(fromServer);
			receiver.start();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}