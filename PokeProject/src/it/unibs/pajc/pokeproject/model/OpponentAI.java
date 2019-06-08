package it.unibs.pajc.pokeproject.model;

import java.util.Random;
import java.util.Set;

public class OpponentAI {

	private SingleplayerModel model;
	private Set<Integer> idSet;
	private int[] movePoints;
	
	public OpponentAI(PKLoader loader, SingleplayerModel model) {
		this.model = model;
		idSet = loader.getPkDatabase().keySet();
		movePoints = new int[4];
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
	
	public int chooseMove() {
		int selected = 0;
		
		for(int i = 0; i < movePoints.length; i++)
			movePoints[i] = model.calcDamage(model.getComputerPokemon(), model.getPlayerPokemon(), i);
		
		for (int i = 0; i < movePoints.length; i++) 
		    selected = movePoints[i] > movePoints[selected] ? i : selected;
		return selected;
	}
}
