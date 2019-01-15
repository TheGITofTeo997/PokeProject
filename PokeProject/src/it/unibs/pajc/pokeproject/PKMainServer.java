package it.unibs.pajc.pokeproject;

import java.util.HashMap;

public class PKMainServer {

	public static void main(String[] args) {
		loadPkmn();

	}
	
	
	public static void loadPkmn() {
		Pokemon bulbasaur = new Pokemon("Bulbasaur", "Erba");
		System.out.println(bulbasaur.stats.get("Attack"));
		System.out.println(bulbasaur.stats.get("Defense"));
		System.out.println(bulbasaur.stats.get("HP"));
		Pokemon charmander = new Pokemon("Charmander", "Fuoco");
		System.out.println(charmander.stats.get("HP"));
		System.out.println(charmander.moves[0].getName());
		System.out.println(charmander.moves[3].getName());
	}
	
}
