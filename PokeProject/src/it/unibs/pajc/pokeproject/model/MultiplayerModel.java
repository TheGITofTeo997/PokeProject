package it.unibs.pajc.pokeproject.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import it.unibs.pajc.pokeproject.util.PKClientStrings;
import it.unibs.pajc.pokeproject.util.PKMessage;

public class MultiplayerModel {

	private Pokemon ourPokemon;
	private Pokemon opponentPokemon;
	
	//private String ourMove;
	
	private ArrayList<PropertyChangeListener> listenerList;
	
	public MultiplayerModel() {
		listenerList = new ArrayList<>();
	}
	public void executeCommand(PKMessage msg) {
		PropertyChangeEvent e;
		switch(msg.getCommandBody()) {
		case MSG_TEST_CONNECTION:
			e = new PropertyChangeEvent(this, PKClientStrings.CONNECTION_PROPERTY, false, true);
			firePropertyChanged(e);
			break;
		case MSG_CONNECTION_CLOSED:
			e = new PropertyChangeEvent(this, PKClientStrings.CONNECTION_CLOSED_PROPERTY, false, true);
			firePropertyChanged(e);
			break;
		case MSG_PLAYER_FOUND:
			e = new PropertyChangeEvent(this, PKClientStrings.PLAYER_FOUND_PROPERTY, false, true);
			firePropertyChanged(e);
			break;
		case MSG_OPPONENT_POKEMON:
			int opponentID = msg.getDataToCarry();
			e = new PropertyChangeEvent(this, PKClientStrings.OPPONENT_PROPERTY, -1, opponentID);
			firePropertyChanged(e);
			break;
		case MSG_OPPONENT_MOVE:
			/*
			String opponentMove = opponentPokemon.getMove(msg.getDataToCarry()).getName();
			e = new PropertyChangeEvent(this, "opponent_move", null, opponentMove);
			firePropertyChanged(e);
			*/
			break;
		case MSG_RECEIVED_DAMAGE:
			int receivedDamage = msg.getDataToCarry();
			int ourRemainingHP = ourPokemon.getBattleHP()-receivedDamage;
			if(ourRemainingHP<0)
				ourRemainingHP=0;
			ourPokemon.setBattleHP(ourRemainingHP);

			e = new PropertyChangeEvent(this, PKClientStrings.OUR_HP_PROPERTY, -1, ourRemainingHP);
			firePropertyChanged(e);
			break;
		case MSG_DONE_DAMAGE:
			int doneDamage = msg.getDataToCarry();
			int opponentRemainingHP = opponentPokemon.getBattleHP()-doneDamage;
			if(opponentRemainingHP<0)
				opponentRemainingHP=0;
			opponentPokemon.setBattleHP(opponentRemainingHP);
			
			e = new PropertyChangeEvent(this, PKClientStrings.OPPONENT_HP_PROPERTY, -1, opponentRemainingHP);
			firePropertyChanged(e);
			break;
		case MSG_BATTLE_OVER:
			if(ourPokemon.getBattleHP()==0) {
				e = new PropertyChangeEvent(this, PKClientStrings.OPPONENT_VICTORY_PROPERTY, -1, 0);
				firePropertyChanged(e);
			}
			else if (opponentPokemon.getBattleHP()==0) {
				e = new PropertyChangeEvent(this, PKClientStrings.OUR_VICTORY_PROPERTY, -1, 0);
				firePropertyChanged(e);
			} 
			break;
		case MSG_REMATCH_YES:
			e = new PropertyChangeEvent(this, PKClientStrings.REMATCH_YES_PROPERTY, false, true);
			firePropertyChanged(e);
			break;
		case MSG_REMATCH_NO:
			e = new PropertyChangeEvent(this, PKClientStrings.REMATCH_NO_PROPERTY, false, true);
			firePropertyChanged(e);
		default:
			break;
		}
	}
	
	public void addPropertyListener(PropertyChangeListener listener) {
		listenerList.add(listener);
	}
	
	public void firePropertyChanged(PropertyChangeEvent e) {
		for(PropertyChangeListener l : listenerList)
			l.propertyChange(e);
	}
	
	public Pokemon getOurPokemon() {
		return ourPokemon;
	}
	public void setOurPokemon(Pokemon ourPokemon) {
		this.ourPokemon = ourPokemon;
		ourPokemon.setBattleHP(ourPokemon.getHP());
	}
	
	/*
	public void setOurMove(int moveID) {
		ourMove = ourPokemon.getMove(moveID).getName();
	}
	*/
	
	public Pokemon getOpponentPokemon() {
		return opponentPokemon;
	}
	
	public void setOpponentPokemon(Pokemon opponentPokemon) {
		this.opponentPokemon = opponentPokemon;
		opponentPokemon.setBattleHP(opponentPokemon.getHP());
	}
}
