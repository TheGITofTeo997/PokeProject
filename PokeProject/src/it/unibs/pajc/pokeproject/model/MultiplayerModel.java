package it.unibs.pajc.pokeproject.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import it.unibs.pajc.pokeproject.util.PKClientStrings;
import it.unibs.pajc.pokeproject.util.PKMessage;
import it.unibs.pajc.pokeproject.util.PKTurnMessage;

public class MultiplayerModel {

	private Pokemon ourPokemon;
	private Pokemon opponentPokemon;
	
	private boolean opponentFirst;
	
	private int ourMoveID;
	private int opponentMoveID;
	
	private String ourEffect;
	private String opponentEffect;
	
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
		case MSG_TURN:
			PKTurnMessage realMsg= (PKTurnMessage)msg;
			opponentFirst = realMsg.getOpponentFirst();
			ourPokemon.setBattleHP(realMsg.getPlayerHP());
			opponentPokemon.setBattleHP(realMsg.getOpponentHP());
			ourMoveID = realMsg.getPlayerMoveID();
			opponentMoveID = realMsg.getOpponentMoveID();
			double ourEff = realMsg.getEffectivenessPlayer();
			double opponentEff = realMsg.getEffectivenessOpponent();
			if(ourEff == 2) ourEffect = "E' superefficace!";
			else if(ourEff == 0.5) ourEffect = "Non e' molto efficace...";
			else ourEffect = "";
			
			if(opponentEff == 2) opponentEffect = "E' superefficace!";
			else if(opponentEff == 0.5) opponentEffect = "Non e' molto efficace...";
			else opponentEffect = "";
			
			
			if(!opponentFirst)
			{	
				if(isDead(opponentPokemon))
				{
					e = new PropertyChangeEvent(this, PKClientStrings.HALF_TURN_US_FIRST_PROPERTY, false, true);
					firePropertyChanged(e);
				}
				else 
				{
					e = new PropertyChangeEvent(this, PKClientStrings.COMPLETE_TURN_US_FIRST_PROPERTY, false, true);
					firePropertyChanged(e);
				}
			}
			else
			{	
				if(isDead(ourPokemon)) 
				{
					e = new PropertyChangeEvent(this, PKClientStrings.HALF_TURN_OPP_FIRST_PROPERTY, false, true);
					firePropertyChanged(e);
				}
				else
				{	
					e = new PropertyChangeEvent(this, PKClientStrings.COMPLETE_TURN_OPP_FIRST_PROPERTY, false, true);
					firePropertyChanged(e);
				}
			}
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
	
	private boolean isDead(Pokemon poke) {
		return (poke.getBattleHP() == 0);
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
	
	public Pokemon getOpponentPokemon() {
		return opponentPokemon;
	}
	
	public void setOpponentPokemon(Pokemon opponentPokemon) {
		this.opponentPokemon = opponentPokemon;
		opponentPokemon.setBattleHP(opponentPokemon.getHP());
	}
	
	public boolean getOpponentFirst() {
		return opponentFirst;
	}
	
	public int getOpponetHP() {
		return opponentPokemon.getBattleHP();
	}

	public int getOurHP() {
		return ourPokemon.getBattleHP();
	}

	public String getOpponentMove() {
		if(opponentMoveID == -1) return "";
		return opponentPokemon.getName() + " nemico usa " + opponentPokemon.getMove(opponentMoveID).getName();
	}

	public String getOpponentEffect() {
		return opponentEffect;
	}

	public String getOurMove() {
		if(ourMoveID == -1) return "";
		return ourPokemon.getName() + " usa " + ourPokemon.getMove(ourMoveID).getName();
	}

	public String getOurEffect() {
		return ourEffect;
	}
}
