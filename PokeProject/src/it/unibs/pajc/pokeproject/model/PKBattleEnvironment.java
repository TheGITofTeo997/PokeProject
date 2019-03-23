package it.unibs.pajc.pokeproject.model;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import it.unibs.pajc.pokeproject.util.PKMessage;

public class PKBattleEnvironment {

	private Pokemon ourPokemon;
	private Pokemon enemyPokemon;
	private boolean wait;
	private ArrayList<PropertyChangeListener> listenerList;
	
	public PKBattleEnvironment() {
		wait = false;
		listenerList = new ArrayList<>();
	}
	public void executeCommand(PKMessage msg) {
		switch(msg.getCommandBody()) {
		case MSG_WAITING:
			wait = true;
			break;
		case MSG_WAKEUP:
			wait = false;
			PropertyChangeEvent e = new PropertyChangeEvent(this, "wait", true, false);
			firePropertyChanged(e);
			break;
		case MSG_START_BATTLE:
			//startBattle();
			break;
		case MSG_OPPONENT_POKEMON:
			break;
		case MSG_OPPONENT_MOVE:
			break;
		case MSG_RECEIVED_DAMAGE:
			break;
		case MSG_DONE_DAMAGE:
			break;
		case MSG_BATTLE_OVER:
			break;
		case MSG_REMATCH:
			break;
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
}
