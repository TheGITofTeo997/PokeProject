package it.unibs.pajc.pokeproject.model;

import java.io.*;
import java.util.*;
import java.net.URL;

public class Pokemon implements Serializable {
	private static final long serialVersionUID = 9044885598484847739L;
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
	private static final String GRASS = "Grass";
	private static final String WATER = "Water";
	private static final String FIRE = "Fire";
	private static final String NORMAL = "Normal";
	private static final String AZIONE = "Azione";
	private static final String RUGGITO = "Ruggito";
	private static final String FORZA = "Forza";
	private static final String FOGLIELAMA = "Foglielama";
	private static final String IDROPULSAR = "Idropulsar";
	private static final String LANCIAFIAMME = "Lanciafiamme";
	private static final int AZIONE_PWR = 40;
	private static final int FORZA_PWR = 80;
	private static final int RUGGITO_PWR = 0;
	private static final int FOGLIELAMA_PWR = 90;
	private static final int LANCIAFIAMME_PWR = 90;
	private static final int IDROPULSAR_PWR = 60;
	
	
	private String name;
	private PKType type;
	private URL frontSprite;
	private URL backSprite;
	private int battleID;
	private PKMove[] moves = new PKMove[NUMBER_OF_MOVES];
	private HashMap<String, Integer> stats = new HashMap<>();
	
	public Pokemon(String name,  PKType type) {
		this.name = name;
		this.type = type;
		fillStats(name);
		fillMoves();
		frontSprite = Pokemon.class.getResource("/img/"+name+"_F");
		backSprite = Pokemon.class.getResource("/img/"+name+"_B");
	}
	
	public URL getFrontSprite() {
		return frontSprite;
	}
	
	public URL getBackSprite() {
		return backSprite;
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
	
	public PKMove getMove(int moveID) {
		return moves[moveID];
	}
	
	public PKType getType() {
		return this.type;
	}
	
	public int getBattleID() {
		return battleID;
	}

	public void setBattleID(int battleID) {
		this.battleID = battleID;
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
		moves[MOVE_1] = new PKMove(AZIONE, AZIONE_PWR, new PKType(NORMAL));
		moves[MOVE_2] = new PKMove(FORZA, FORZA_PWR, new PKType(NORMAL));
		moves[MOVE_3] = new PKMove(RUGGITO, RUGGITO_PWR, new PKType(NORMAL));
		switch(type.getTypeName()) {
		case GRASS:
			moves[MOVE_4] = new PKMove(FOGLIELAMA, FOGLIELAMA_PWR, new PKType(GRASS));
			break;
		case FIRE:
			moves[MOVE_4] = new PKMove(LANCIAFIAMME, LANCIAFIAMME_PWR, new PKType(FIRE));
			break;
		case WATER:
			moves[MOVE_4] = new PKMove(IDROPULSAR, IDROPULSAR_PWR, new PKType(WATER));
			break;
		}
	}
	
	//il damage sono il numero di PS che il server dirà al pkmn di togliersi
	private void getDamage(int damage) {
		stats.put(HP, stats.get(HP) - damage);
	}
	
	
}
