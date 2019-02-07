package it.unibs.pajc.pokeproject;

import java.util.concurrent.*;

public class IdentifiedQueue<E> extends ArrayBlockingQueue<E>{
	private static final long serialVersionUID = 4616453441761136390L;
	private int id;
	
	
	public IdentifiedQueue(int capacity) {
		super(capacity);
		id = -1; // inizializzazione di default dell'id
	}


	public int getId() {
		return id;
	}

	// questo metodo sarà invocato nel MainServer per settare l'id della coda usando quello del ServerProtocol
	public void setId(int id) {
		this.id = id;
	}
}
