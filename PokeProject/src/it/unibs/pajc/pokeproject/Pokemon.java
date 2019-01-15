package it.unibs.pajc.pokeproject;
import java.util.*;

public class Pokemon {
	private String name;
	private String type;
	private PKMove[] moves = new PKMove[4];
	private HashMap<String, Integer> stats;
	
	
	
	public Pokemon(String name,  String type, HashMap<String, Integer> stats) {
		this.name = name;
		this.type = type;
		this.stats = stats;
	}
	
	private HashMap fillStats(HashMap<String, Integer> stats) {
		
	}
	
	
}
