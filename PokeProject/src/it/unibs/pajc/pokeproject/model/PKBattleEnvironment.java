package it.unibs.pajc.pokeproject.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import it.unibs.pajc.pokeproject.util.PKMessage;

public class PKBattleEnvironment {

	private Pokemon ourPokemon;
	private Pokemon opponentPokemon;
	
	private String ourMove;
	
	private ArrayList<PropertyChangeListener> listenerList;
	
	public PKBattleEnvironment() {
		listenerList = new ArrayList<>();
	}
	public void executeCommand(PKMessage msg) {
		PropertyChangeEvent e;
		switch(msg.getCommandBody()) {
		case MSG_TEST_CONNECTION:
			e = new PropertyChangeEvent(this, "connection", false, true);
			firePropertyChanged(e);
			break;
		case MSG_CONNECTION_CLOSED:
			e = new PropertyChangeEvent(this, "connection_closed", false, true);
			firePropertyChanged(e);
		case MSG_WAKEUP:
			// this may not be needed, further analysis requested
			e = new PropertyChangeEvent(this, "wait", true, false);
			firePropertyChanged(e);
			break;
		case MSG_PLAYER_FOUND:
			e = new PropertyChangeEvent(this, "player_found", false, true);
			firePropertyChanged(e);
			break;
		case MSG_OPPONENT_POKEMON:
			int opponentID = msg.getDataToCarry();
			e = new PropertyChangeEvent(this, "opponent", -1, opponentID);
			firePropertyChanged(e);
			break;
		case MSG_OPPONENT_MOVE:
			/*
			String opponentMove = opponentPokemon.getMove(msg.getDataToCarry()).getName();
			e = new PropertyChangeEvent(this, "opponent_move", null, opponentMove);
			firePropertyChanged(e);
			break;
			*/
		case MSG_RECEIVED_DAMAGE:
			int receivedDamage = msg.getDataToCarry();
			int ourRemainingHP = ourPokemon.getBattleHP()-receivedDamage;
			System.out.println("Nostri HP iniziali turno: "+ ourPokemon.getBattleHP());
			if(ourRemainingHP<0)
				ourRemainingHP=0;
			ourPokemon.setBattleHP(ourRemainingHP);
			
			System.out.println("Danno ricevuto da noi: " + receivedDamage + "\nHP rimasti a noi: " + ourRemainingHP);
			System.out.println("****************************");
			
			e = new PropertyChangeEvent(this, "ourHP", -1, ourRemainingHP);
			firePropertyChanged(e);
			break;
		case MSG_DONE_DAMAGE:
			/*
			e = new PropertyChangeEvent(this, "our_move", null, ourMove);
			firePropertyChanged(e);
			*/
			int doneDamage = msg.getDataToCarry();
			int opponentRemainingHP = opponentPokemon.getBattleHP()-doneDamage;
			System.out.println("Loro HP iniziali turno: "+ opponentPokemon.getBattleHP());
			if(opponentRemainingHP<0)
				opponentRemainingHP=0;
			opponentPokemon.setBattleHP(opponentRemainingHP);
			
			System.out.println("Danno ricevuto dal nemico: " + doneDamage + "\nHP rimasti al nemico: " + opponentRemainingHP);
			System.out.println("****************************");
			
			e = new PropertyChangeEvent(this, "opponentHP", -1, opponentRemainingHP);
			firePropertyChanged(e);
			break;
		case MSG_BATTLE_OVER:
			if(ourPokemon.getBattleHP()==0) {
				e = new PropertyChangeEvent(this, "opponentVictory", -1, 0);
				firePropertyChanged(e);
			}
			else if (opponentPokemon.getBattleHP()==0) {
				e = new PropertyChangeEvent(this, "ourVictory", -1, 0);
				firePropertyChanged(e);
			} 
			break;
		case MSG_REMATCH_YES:
			e = new PropertyChangeEvent(this, "rematch_yes", false, true);
			firePropertyChanged(e);
			break;
		case MSG_REMATCH_NO:
			e = new PropertyChangeEvent(this, "rematch_no", false, true);
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
	
	public void setOurMove(int moveID) {
		ourMove = ourPokemon.getMove(moveID).getName();
	}
	
	public Pokemon getOpponentPokemon() {
		return opponentPokemon;
	}
	
	public void setOpponentPokemon(Pokemon opponentPokemon) {
		this.opponentPokemon = opponentPokemon;
		opponentPokemon.setBattleHP(opponentPokemon.getHP());
	}
}
