package it.unibs.pajc.pokeproject;
import java.io.*;
import java.util.*;

public class Pokemon implements Serializable{
	private String name;
	private String type;
	private PKMove[] moves = new PKMove[4];
	private HashMap<String, Integer> stats = new HashMap<>();
	
	public Pokemon(String name,  String type) {
		this.name = name;
		this.type = type;
		fillStats(name);
		fillMoves();
	}
	
	public int getID() {
		return stats.get("ID");
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getAttack() {
		return stats.get("Attack");
	}
	
	public int getDefense() {
		return stats.get("Defense");
	}
	
	public int getSpeed() {
		return stats.get("Speed");
	}
	
	public int getLevel() {
		return stats.get("Level");
	}
	
	public int getHP() {
		return stats.get("HP");
	}
	
	/*
	 * TODO LIST
	 * fix percorso file, fix nome file, controlli file
	 * 
	 */
	
	private void fillStats(String name) {
		String filename = name + ".pk";
		File poke = new File(filename);
		if(poke.isFile())
		{
			try (BufferedReader br = new BufferedReader(new FileReader(poke))) {
				String text;
				while((text=br.readLine())!=null) {
					StringTokenizer st = new StringTokenizer(text, ":");
					String key = st.nextToken();
					String value = st.nextToken();
					int intvalue = Integer.parseInt(value);
					stats.put(key, intvalue);
				}	
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}	
	}	
	
	private void fillMoves() {
		moves[0] = new PKMove("Azione", 40);
		moves[1] = new PKMove("Forza", 80);
		moves[2] = new PKMove("Ruggito", 0);
		switch(type) {
		case "Erba":
			moves[3] = new PKMove("Foglielama", 90);
			break;
		case "Fuoco":
			moves[3] = new PKMove("Lanciafiamme", 90);
			break;
		case "Acqua":
			moves[3] = new PKMove("Idropulsar", 60);
			break;
		}
	}
	
	//il damage sono il numero di PS che il server dirà al pkmn di togliersi
	private void getDamage(int damage) {
		stats.put("HP", stats.get("HP") - damage);
	}
	
}
