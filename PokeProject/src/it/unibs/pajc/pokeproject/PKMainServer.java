package it.unibs.pajc.pokeproject;

import java.util.HashMap;

public class PKMainServer {

	public static void main(String[] args) {
		loadPkmn();

	}
	
	
	public static void loadPkmn() {
		Pokemon bulbasaur = new Pokemon("Bulbasaur", "Erba");
		System.out.println(bulbasaur.stats.get("Attack"));
	}
	
}
