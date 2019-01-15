package it.unibs.pajc.pokeproject;

public class PKMove {
	private String name;
	private int pwr;
	
	public PKMove(String name, int pwr) {
		this.name = name;
		this.pwr = pwr;
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
	
	

}
