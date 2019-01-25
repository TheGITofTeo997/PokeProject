package it.unibs.pajc.pokeproject;
import java.io.*;
import java.util.*;

public class Pokemon implements Serializable {
	private static final int NUMBER_OF_MOVES = 4;
	private static final int MOVE_1 = 0;
	private static final int MOVE_2 = 1;
	private static final int MOVE_3 = 2;
	private static final int MOVE_4 = 3;
	
	private static final String ATTACK = "Attack";
	private static final String DEFENSE = "Defense";
	private static final String SPEED = "Speed";
	private static final String HP = "HP";
	private static final String LEVEL = "Level";
	private static final String ID = "ID";
	
	private static final String GRASS = "Erba";
	private static final String WATER = "Acqua";
	private static final String FIRE = "Fuoco";
	
	
	
	private String name;
	private String type;
	private PKMove[] moves = new PKMove[NUMBER_OF_MOVES];
	private HashMap<String, Integer> stats = new HashMap<>();
	
	public Pokemon(String name,  String type) {
		this.name = name;
		this.type = type;
		fillStats(name);
		fillMoves();
	}
	
	public int getID() {
		return stats.get(ID);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getAttack() {
		return stats.get(ATTACK);
	}
	
	public int getDefense() {
		return stats.get(DEFENSE);
	}
	
	public int getSpeed() {
		return stats.get(SPEED);
	}
	
	public int getLevel() {
		return stats.get(LEVEL);
	}
	
	public int getHP() {
		return stats.get(HP);
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
		moves[MOVE_1] = new PKMove("Azione", 40);
		moves[MOVE_2] = new PKMove("Forza", 80);
		moves[MOVE_3] = new PKMove("Ruggito", 0);
		switch(type) {
		case GRASS:
			moves[MOVE_4] = new PKMove("Foglielama", 90);
			break;
		case FIRE:
			moves[MOVE_4] = new PKMove("Lanciafiamme", 90);
			break;
		case WATER:
			moves[MOVE_4] = new PKMove("Idropulsar", 60);
			break;
		}
	}
	
	//il damage sono il numero di PS che il server dirà al pkmn di togliersi
	private void getDamage(int damage) {
		stats.put(HP, stats.get(HP) - damage);
	}
	
}
