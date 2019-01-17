package it.unibs.pajc.pokeproject;

import java.util.*;
import java.io.IOException;
import java.net.*;

public class PKMainServer {
	private static final int ID0 = 0;
	private static final int ID1 = 1;
	private static Pokemon trainerPoke0;
	private static Pokemon trainerPoke1;
	
	
	

	public static void main(String[] args) {
		initialize();
		
	}
			
	public static void loadPkmn() {
		Pokemon bulbasaur = new Pokemon("Bulbasaur", "Erba");
		Pokemon charmander = new Pokemon("Charmander", "Fuoco");
		Pokemon squirtle = new Pokemon ("Squirtle", "Acqua");
	}
	
	public static void initialize() {
		loadPkmn();
	    openConnection();
	    trainerPoke0.setID(ID0);
	    trainerPoke1.setID(ID1);

	    
	}
	
	public static void openConnection() {
		ServerSocket server;
		try {
			server = new ServerSocket(50000);
			System.out.println("Server started on port 50000...");
			while(true) {  
		    Socket client = server.accept();
		    PKServerProtocol protocol = new PKServerProtocol(client);
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

	    
	}
	
	
	public int setPriorityBattle(Pokemon p0, Pokemon p1) {
		int speedP0 = p0.getSpeed();
		int speedP1 = p1.getSpeed();
		if(speedP0>=speedP1) {
			return ID0;
		}
		else
			return ID1;
	}
	
	public int damageToSend(int pwrAttack) {
		int damageToSend=0;
		return damageToSend;
	}
	
	
	
	//Il server riceverà in ingresso i due pkmn e ad ognuno assegnerà l'ID, in base al quale
	//attaccheranno e verranno identificati all'interno dei metodi.
	
}
