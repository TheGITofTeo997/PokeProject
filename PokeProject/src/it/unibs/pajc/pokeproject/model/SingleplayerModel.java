package it.unibs.pajc.pokeproject.model;

import java.util.concurrent.ThreadLocalRandom;

import it.unibs.pajc.pokeproject.util.PKClientStrings;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class SingleplayerModel {

	private ArrayList<PropertyChangeListener> listenerList;
	private Pokemon playerPoke;
	private Pokemon computerPoke;
	private PropertyChangeEvent e;
	
	public SingleplayerModel() {
		listenerList = new ArrayList<>();
	}
	
	public void setPokemons(Pokemon playerPoke, Pokemon computerPoke) {
		this.playerPoke = playerPoke;
		this.computerPoke = computerPoke;
		
		playerPoke.setBattleID(0);
		computerPoke.setBattleID(1);
		
		playerPoke.setBattleHP(playerPoke.getHP());
		computerPoke.setBattleHP(computerPoke.getHP());
		
		e = new PropertyChangeEvent(this, PKClientStrings.START_BATTLE_PROPERTY , false, true);
		firePropertyChanged(e);
	}
	
	public void doTurnCalculation(int playerMove, int computerMove) {
		int playerDamage = calcDamage(playerPoke, computerPoke, playerMove);
		int computerDamage = calcDamage(computerPoke, playerPoke, computerMove);
		int firstAttackerID = setPriorityBattle(playerPoke, computerPoke);
		
		int computerRemainingHP = computerPoke.getBattleHP()- playerDamage;
		if(computerRemainingHP<0)
			computerRemainingHP=0;
		computerPoke.setBattleHP(computerRemainingHP);
		
		int playerRemainingHP = playerPoke.getBattleHP()- computerDamage;
		if(playerRemainingHP<0)
			playerRemainingHP=0;
		playerPoke.setBattleHP(playerRemainingHP);
		
		if(firstAttackerID == 0)
		{
			
			e = new PropertyChangeEvent(this, PKClientStrings.COMPUTER_HP_PROPERTY, -1, computerRemainingHP);
			firePropertyChanged(e);
			
			if(isDead(computerPoke))
			{
				e = new PropertyChangeEvent(this, PKClientStrings.PLAYER_VICTORY_PROPERTY, false, true);
				firePropertyChanged(e);
			}
			else
			{
				e = new PropertyChangeEvent(this, PKClientStrings.PLAYER_HP_PROPERTY, -1, playerRemainingHP);
				firePropertyChanged(e);
				
				if(isDead(playerPoke)) 
				{
					e = new PropertyChangeEvent(this, PKClientStrings.PLAYER_DEFEAT_PROPERTY, false, true);
					firePropertyChanged(e);
				}
			}
		}
		else
		{
			e = new PropertyChangeEvent(this, PKClientStrings.PLAYER_HP_PROPERTY, -1, playerRemainingHP);
			firePropertyChanged(e);
			
			if(isDead(playerPoke)) 
			{
				e = new PropertyChangeEvent(this, PKClientStrings.PLAYER_DEFEAT_PROPERTY, false, true);
				firePropertyChanged(e);
			}
			else
			{	
				e = new PropertyChangeEvent(this, PKClientStrings.COMPUTER_HP_PROPERTY, -1, computerRemainingHP);
				firePropertyChanged(e);
				
				if(isDead(computerPoke))
				{
					e = new PropertyChangeEvent(this, PKClientStrings.PLAYER_VICTORY_PROPERTY, false, true);
					firePropertyChanged(e);
				}
			}
		}
	}
	
	public int calcDamage(Pokemon attacker, Pokemon defender, int moveID) {
		PKMove move = attacker.getMove(moveID);
		double N = ThreadLocalRandom.current().nextDouble(0.85, 1);
		double stab = (attacker.hasStab(moveID)) ? 1.5 : 1;
		double damage = ((((2*attacker.getLevel()+10)*attacker.getAttack()*move.getPwr()) / 
				(250*defender.getDefense()) ) + 2) * stab *move.getType().getEffectiveness(defender.getType())
				* N;
		if(damage < 1) damage = 0;
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
