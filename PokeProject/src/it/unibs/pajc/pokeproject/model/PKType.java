package it.unibs.pajc.pokeproject.model;

import java.io.*;
import java.util.HashMap;

public class PKType implements Serializable{
	private static final long serialVersionUID = -1261663080265777425L;
	// hashmap contenente le associazioni tra il tipo considerato e l'efficacia sugli altri tipi
	private HashMap<String, Double> effectivenessTable = new HashMap<>();
	private String type;
	
	public PKType(String type) {
		this.type = type;
	}
	
	public double getEffectiveness(PKType type) {
		return effectivenessTable.get(type.getTypeName());
	}
	
	public String getTypeName() {
		return this.type;
	}
	
	public void setEffectivenessEntry(String type, double eff) {
		effectivenessTable.put(type, eff);
	}
	
	public boolean equals(PKType type) {
		return type.getTypeName().equals(this.type);
	}
}
