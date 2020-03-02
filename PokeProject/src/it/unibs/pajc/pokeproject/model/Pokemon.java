package it.unibs.pajc.pokeproject.model;

import java.io.*;
import java.util.*;
import java.net.URL;

//pokemon
public class Pokemon implements Serializable {
	private static final long serialVersionUID = 9044885598484847739L;
	private static final int NUMBER_OF_MOVES = 4;
	private static final String ATTACK = "Attack";
	private static final String DEFENSE = "Defense";
	private static final String SPEED = "Speed";
	private static final String HP = "HP";
	private static final String LEVEL = "Level";
	private static final String ID = "ID";	
	
	private String name;
	private PKType type;
	private URL frontSprite;
	private URL backSprite;
	private int battleID;
	private int battleHP;
	private PKMove[] moves = new PKMove[NUMBER_OF_MOVES];
	private HashMap<String, Integer> stats = new HashMap<>();
	
	public Pokemon(String name,  PKType type) {
		this.name = name;
		this.type = type;
		frontSprite = Pokemon.class.getResource("/img/"+name+"_F");
		backSprite = Pokemon.class.getResource("/img/"+name+"_B");
	}
	
	public URL getFrontSprite() {
		return frontSprite;
	}
	
	public void setFrontSprite(URL frontSprite) {
		this.frontSprite = frontSprite;
	}
	
	public URL getBackSprite() {
		return backSprite;
	}
	
	public void setBackSprite(URL backSprite) {
		this.backSprite = backSprite;
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
	
	public PKMove[] getMoveSet() {
		return moves;
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
	
	public int getBattleHP() {
		return battleHP;
	}

	public void setBattleHP(int battleHP) {
		if(battleHP<0)
			this.battleHP = 0;
		else
			this.battleHP = battleHP;
	}

	public void setStat(String stat, int value) {
		stats.put(stat, value);
	}
	
	public void setMove(int moveID, PKMove move) {
		moves[moveID] = move;
	}
	
	public boolean hasStab(int moveID) {
		return moves[moveID].getType().equals(type);
	}
}
