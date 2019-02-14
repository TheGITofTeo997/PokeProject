package it.unibs.pajc.pokeproject.model;

import java.io.*;

public class PKMove implements Serializable{
	private static final long serialVersionUID = -503011830200575067L;
	private String name;
	private int pwr;
	private PKType type;
	
	public PKMove(String name, int pwr, PKType type) {
		this.name = name;
		this.pwr = pwr;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPwr() {
		return pwr;
	}

	public void setPwr(int pwr) {
		this.pwr = pwr;
	}
	
	public String getTypeName() {
		return this.type.getTypeName();
	}
	
	public PKType getType() {
		return this.type;
	}
	

}
