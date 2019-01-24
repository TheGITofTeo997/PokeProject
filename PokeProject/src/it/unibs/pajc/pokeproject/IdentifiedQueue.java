package it.unibs.pajc.pokeproject;

import java.util.concurrent.*;

public class IdentifiedQueue<E> extends ArrayBlockingQueue<E>{
	private int id=-1;
	
	
	public IdentifiedQueue(int capacity) {
		super(capacity);
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
}
