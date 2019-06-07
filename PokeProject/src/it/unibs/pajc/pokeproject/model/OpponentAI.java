package it.unibs.pajc.pokeproject.model;

import java.util.Random;
import java.util.Set;

public class OpponentAI {

	private PKLoader loader;
	private SinglePlayerModel model;
	private Set idSet;
	
	public OpponentAI(PKLoader loader, SinglePlayerModel model) {
		this.loader = loader;
		this.model = model;
		idSet = loader.getPkDatabase().keySet();
	}
	
	public int chooseRandomPokemon() {
		int selected = 0;
		int size = idSet.size();
		int item = new Random().nextInt(size);
		int i = 0;
		for(Object obj : idSet)
		{
			if (i == item)
				selected = (Integer)obj;
		    i++;
		}
		return selected;
	}
}
