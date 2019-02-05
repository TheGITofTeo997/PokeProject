package it.unibs.pajc.pokeproject;

import java.io.*;

public class PKMove implements Serializable{
	private String name;
	private int pwr;
	private String type;
	
	public PKMove(String name, int pwr, String type) {
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
	
	public String getType() {
		return this.type;
	}
	
	

}
