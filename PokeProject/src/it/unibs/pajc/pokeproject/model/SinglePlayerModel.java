package it.unibs.pajc.pokeproject.model;

import java.util.concurrent.ThreadLocalRandom;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class SinglePlayerModel {

	private ArrayList<PropertyChangeListener> listenerList;
	private Pokemon playerPoke;
	private Pokemon computerPoke;
	private PropertyChangeEvent e;
	
	public SinglePlayerModel() {
		listenerList = new ArrayList<>();
	}
	
	public void setPokemons(Pokemon playerPoke, Pokemon computerPoke) {
		this.playerPoke = playerPoke;
		this.computerPoke = computerPoke;
		
		playerPoke.setBattleHP(playerPoke.getHP());
		computerPoke.setBattleHP(computerPoke.getHP());
		
		e = new PropertyChangeEvent(this, "battle_panel", false, true);
	}
	
	public void doTurnCalculation(int playerMove, int computerMove) {
		int playerDamage = calcDamage(playerPoke, computerPoke, playerMove);
		int computerDamage = calcDamage(computerPoke, playerPoke, computerMove);
		
		int playerRemainingHP = playerPoke.getBattleHP()- computerDamage;
		if(playerRemainingHP<0)
			playerRemainingHP=0;
		playerPoke.setBattleHP(playerRemainingHP);
	
		e = new PropertyChangeEvent(this, "playerHP", -1, playerRemainingHP);
		firePropertyChanged(e);

		int computerRemainingHP = computerPoke.getBattleHP()- playerDamage;
		if(computerRemainingHP<0)
			computerRemainingHP=0;
		computerPoke.setBattleHP(computerRemainingHP);
		
		e = new PropertyChangeEvent(this, "computerHP", -1, computerRemainingHP);
		firePropertyChanged(e);
		
		if(isDead(playerPoke)) 
		{
			e = new PropertyChangeEvent(this, "player_defeat", false, true);
			firePropertyChanged(e);
		}
		else if(isDead(computerPoke))
		{
			e = new PropertyChangeEvent(this, "player_victory", false, true);
			firePropertyChanged(e);
		}
	}
	
	public int calcDamage(Pokemon attacker, Pokemon defender, int moveID) {
		double N = ThreadLocalRandom.current().nextDouble(0.85, 1);
		double stab = (attacker.getType().getTypeName().compareToIgnoreCase(attacker.getMove(moveID).getTypeName())==0) ? 1.5 : 1;
		double damage = ((((2*attacker.getLevel()+10)*attacker.getAttack()*attacker.getMove(moveID).getPwr()) / 
				(250*defender.getDefense()) ) + 2) * stab * attacker.getMove(moveID).getType().getEffectiveness(defender.getType().getTypeName())
				* N;
		return (int)damage;
	}
	
	private int setPriorityBattle(Pokemon p0, Pokemon p1) {
		int speedP0 = p0.getSpeed();
		int speedP1 = p1.getSpeed();
		if(speedP0>=speedP1) {
			return p0.getBattleID();
		}
		else
			return p1.getBattleID();
	}
	
	private boolean isDead(Pokemon poke) {
		return poke.getBattleHP() == 0;
	}
	
	public Pokemon getPlayerPokemon() {
		return playerPoke;
	}
	
	public Pokemon getComputerPokemon() {
		return computerPoke;
	}
	
	public void addPropertyListener(PropertyChangeListener listener) {
		listenerList.add(listener);
	}
	
	public void firePropertyChanged(PropertyChangeEvent e) {
		for(PropertyChangeListener l : listenerList)
			l.propertyChange(e);
	}
}
