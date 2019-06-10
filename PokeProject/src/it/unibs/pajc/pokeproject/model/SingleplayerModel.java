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
	private int playerMoveID;
	private int computerMoveID;
	private double effectiveness;
	private String playerEffect;
	private String computerEffect;
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
	
	public void doTurnCalculation(int playerMoveID, int computerMoveID) {
		this.playerMoveID = playerMoveID;
		this.computerMoveID = computerMoveID;
		int playerDamage = calcDamage(playerPoke, computerPoke, playerMoveID);
		if(effectiveness == 2) playerEffect = "E' superefficace!";
		else if(effectiveness == 1) playerEffect = "";
		else playerEffect = "Non e' molto efficace";
		int computerDamage = calcDamage(computerPoke, playerPoke, computerMoveID);
		if(effectiveness == 2) computerEffect = "E' superefficace!";
		else if(effectiveness == 1) computerEffect = "";
		else computerEffect = "Non e' molto efficace";
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
			if(isDead(computerPoke))
			{
				e = new PropertyChangeEvent(this, PKClientStrings.HALF_TURN_PLAYER_FIRST_PROPERTY, false, true);
				firePropertyChanged(e);
				
				e = new PropertyChangeEvent(this, PKClientStrings.PLAYER_VICTORY_PROPERTY, false, true);
				firePropertyChanged(e);
			}
			else 
			{
				e = new PropertyChangeEvent(this, PKClientStrings.COMPLETE_TURN_PLAYER_FIRST_PROPERTY, false, true);
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
			if(isDead(playerPoke)) 
			{
				e = new PropertyChangeEvent(this, PKClientStrings.HALF_TURN_COMPUTER_FIRST_PROPERTY, false, true);
				firePropertyChanged(e);
				
				e = new PropertyChangeEvent(this, PKClientStrings.PLAYER_DEFEAT_PROPERTY, false, true);
				firePropertyChanged(e);
			}
			else
			{	
				e = new PropertyChangeEvent(this, PKClientStrings.COMPLETE_TURN_COMPUTER_FIRST_PROPERTY, false, true);
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
		effectiveness = move.getType().getEffectiveness(defender.getType());
		double N = ThreadLocalRandom.current().nextDouble(0.85, 1);
		double stab = (attacker.hasStab(moveID)) ? 1.5 : 1;
		double damage = ((((2*attacker.getLevel()+10)*attacker.getAttack()*move.getPwr()) / 
				(250*defender.getDefense()) ) + 2) * stab *effectiveness
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

	public int getComputerHP() {
		return computerPoke.getBattleHP();
	}

	public int getPlayerHP() {
		return playerPoke.getBattleHP();
	}

	public String getComputerMove() {
		return computerPoke.getName() + " nemico usa " + computerPoke.getMove(computerMoveID).getName();
	}

	public String getComputerEffect() {
		return computerEffect;
	}

	public String getPlayerMove() {
		return playerPoke.getName() + " usa " + playerPoke.getMove(playerMoveID).getName();
	}

	public String getPlayerEffect() {
		return playerEffect;
	}
}
