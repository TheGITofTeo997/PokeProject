package it.unibs.pajc.pokeproject.controller;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import it.unibs.pajc.pokeproject.model.*;
import it.unibs.pajc.pokeproject.util.*;
import it.unibs.pajc.pokeproject.view.*;

public class PKServerController extends Thread implements ActionListener {
	
	//Local Components
	private static final int FIRST_QUEUE = 0;
	private static final int SECOND_QUEUE = 1;
	private static final int DEFALT_QUEUE_ID = -1;
	private static final int SERVER_PORT = 50000;
	private static final int QUEUE_SIZE = 5;
	
	//Controller Components
	private PKLoader loader;
	private Pokemon trainerPoke0;
	private Pokemon trainerPoke1;
	private ArrayList<IdentifiedQueue<PKMessage>> fromQueues = new ArrayList<>(); 
	// array di code da cui il server prenderà i messaggi che i client hanno mandato
	private ArrayList<IdentifiedQueue<PKMessage>> toQueues = new ArrayList<>(); 
	// array di code in cui il server metterà i messaggi da inviare ai client
	private int firstMoveSelectedID = -1;
	
	//View Components
	private PKServerWindow view;

	public PKServerController() {
		loader = new PKLoader();
	}
	
	public void run(){
		setupServerUtils();
		/*
		 * @author Patrick
		 * I will replace this piece of code with a ScheduledThreadPoolExecutor
		 * just to be fancier
		 */
		Timer timer = new Timer(); 
		timer.schedule(
			new TimerTask() {
				public void run() {
					if(!(fromQueues.get(FIRST_QUEUE).isEmpty())) {
						executeCommand(fromQueues.get(FIRST_QUEUE).poll());
					}
					if(!(fromQueues.get(SECOND_QUEUE).isEmpty())) {
						executeCommand(fromQueues.get(SECOND_QUEUE).poll());
					}
				}
			}, 0, 1000);
		openConnection();
	}
	
	public void setupServerUtils() {
		loader.loadTypes();
		if(loader.typeDatabaseExist()) 
			view.appendTextToConsole(PKServerStrings.LOADED_TYPE_ARRAYLIST_SUCCESFULLY);
		else
			view.appendTextToConsole(PKServerStrings.TYPE_ARRAYLIST_LOADING_FAILURE);
		loader.loadPokemon();
		if(loader.pkDatabaseExist())
			view.appendTextToConsole(PKServerStrings.LOADED_PK_TREEMAP_SUCCESFULLY);
		else
			view.appendTextToConsole(PKServerStrings.PK_TREEMAP_LOADING_FAILURE);
		setupQueues();
	}
	
	private void setupQueues() { // le code vengono create qui e vengono messe negli arraylist
		fromQueues.add(new IdentifiedQueue<>(QUEUE_SIZE));
		fromQueues.add(new IdentifiedQueue<>(QUEUE_SIZE));
		toQueues.add(new IdentifiedQueue<>(QUEUE_SIZE));
		toQueues.add(new IdentifiedQueue<>(QUEUE_SIZE));
	}
	
	public void openConnection() {
		ServerSocket server;
		try {
			int i=0;
			int connectedClients=0;
			server = new ServerSocket(SERVER_PORT);
			view.appendTextToConsole(PKServerStrings.SERVER_STARTED_SUCCESFULLY);
			while(connectedClients<2) {  
				Socket client = server.accept();
				connectedClients++;
				PKServerProtocol protocol = new PKServerProtocol(client, view);
		    
				//assegnamento code ai serverprotocol con assegnamento id
				if(fromQueues.get(i).getId() == DEFALT_QUEUE_ID) { //cerco la coda con id di default
					protocol.setInputBuffer(fromQueues.get(i)); //la assegno al ServerProtocol
					fromQueues.get(i).setId(protocol.getIdCounter()); //e poi le assegno l'id del ServerProtocol
				}
				else { // i client che si connettono sono 2, quindi se una è già assegnata l'altra sarà libera
					protocol.setInputBuffer(fromQueues.get(++i));
					fromQueues.get(i).setId(protocol.getIdCounter());
				}
				i=0;
				if(toQueues.get(i).getId() == DEFALT_QUEUE_ID) { //stessa cosa di prima
					protocol.setOutputBuffer(toQueues.get(i));
					toQueues.get(i).setId(protocol.getIdCounter());
				}
				else {
					protocol.setOutputBuffer(toQueues.get(++i));		
					toQueues.get(i).setId(protocol.getIdCounter());
				}   	
				
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void executeCommand(PKMessage msg) {
		switch(msg.getCommandBody()) {
		case MSG_TEST_CONNECTION:
			returnTestConnection(msg);
			break;
		case MSG_SELECTED_POKEMON:
			selectedPokemon(msg);
			break;
		case MSG_SELECTED_MOVE:
			selectedMove(msg);
			break;
		case MSG_REMATCH_YES:
			break;
		case MSG_REMATCH_NO:
			break;	
		default:
			break;
		}
	}
	
	public int setPriorityBattle(Pokemon p0, Pokemon p1) {
		int speedP0 = p0.getSpeed();
		int speedP1 = p1.getSpeed();
		if(speedP0>=speedP1) {
			return p0.getBattleID();
		}
		else
			return p1.getBattleID();
	}
		
	private void selectedPokemon(PKMessage msg) {
		if(trainerPoke0 == null) {
			trainerPoke0 = loader.getPokemonFromDB(msg.getDataToCarry());
			trainerPoke0.setBattleID(msg.getClientID());
		}
		else {
			trainerPoke1 = loader.getPokemonFromDB(msg.getDataToCarry());
			trainerPoke1.setBattleID(msg.getClientID());
		}
		if(!(trainerPoke0 == null) && !(trainerPoke1 == null)) {
			PKMessage wakeup = new PKMessage(Commands.MSG_WAKEUP);
			PKMessage opponentFor0 = new PKMessage(Commands.MSG_OPPONENT_POKEMON, trainerPoke1.getID());
			PKMessage opponentFor1 = new PKMessage(Commands.MSG_OPPONENT_POKEMON, trainerPoke0.getID());
			toQueues.get(FIRST_QUEUE).add(wakeup);
			toQueues.get(SECOND_QUEUE).add(wakeup);		
			toQueues.get(FIRST_QUEUE).add(opponentFor0);
			toQueues.get(SECOND_QUEUE).add(opponentFor1);
		}
		else {
			PKMessage wait = new PKMessage(Commands.MSG_WAITING);
			toQueues.get(msg.getClientID()).add(wait);
		}
	}
	
	private int calcDamage(Pokemon attacker, Pokemon defender, int moveID) {
		double N = ThreadLocalRandom.current().nextDouble(0.85, 1);
		double stab = (attacker.getType().getTypeName().compareToIgnoreCase(attacker.getMove(moveID).getTypeName())==0) ? 1.5 : 1;
		double damage = ((((2*attacker.getLevel()+10)*attacker.getAttack()*attacker.getMove(moveID).getPwr()) / 
				(250*defender.getDefense()) ) + 2) * stab * attacker.getMove(moveID).getType().getEffectiveness(defender.getType().getTypeName())
				* N;
		return (int)damage;
	}
	
	private void selectedMove(PKMessage msg) {
		int selected = msg.getDataToCarry();
		if(firstMoveSelectedID == -1) {
			firstMoveSelectedID = selected;
			PKMessage wait = new PKMessage(Commands.MSG_WAITING);
			for(int i=0; i<toQueues.size(); i++) {
				if (toQueues.get(i).getId() == msg.getClientID())
					toQueues.get(i).add(wait);
			}
		}
		else {
			PKMessage wakeup = new PKMessage(Commands.MSG_WAKEUP);
			for(int i=0; i<toQueues.size(); i++) {
				if (toQueues.get(i).getId() != msg.getClientID())
					toQueues.get(i).add(wakeup);
			}
			
			int firstAttackerID = setPriorityBattle(trainerPoke0, trainerPoke1);
			
			if(firstAttackerID == trainerPoke0.getBattleID() && firstAttackerID == msg.getClientID()) {
				sendSelectedMoveMessage(trainerPoke0, trainerPoke1, firstAttackerID, selected, firstMoveSelectedID);
			}
			else if(firstAttackerID == trainerPoke1.getBattleID() && firstAttackerID == msg.getClientID()) {
				sendSelectedMoveMessage(trainerPoke1, trainerPoke0, firstAttackerID, selected, firstMoveSelectedID);
			}
			else if(firstAttackerID == trainerPoke0.getBattleID() && firstAttackerID != msg.getClientID()) {
				sendSelectedMoveMessage(trainerPoke0, trainerPoke1, firstAttackerID, firstMoveSelectedID, selected);
			}
			else {
				sendSelectedMoveMessage(trainerPoke1, trainerPoke0, firstAttackerID, firstMoveSelectedID, selected);
			}
		}
	}
	
	//this method doesn't look actually very well
	private void sendSelectedMoveMessage(Pokemon firstAttacker, Pokemon secondAttacker, int firstAttackerID, int firstMove, int secondMove) {
		int damageFirstAttacker = calcDamage(firstAttacker, secondAttacker, firstMove);
		int damageSecondAttacker = calcDamage(secondAttacker, firstAttacker, secondMove);
		PKMessage damageDoneByFirst = new PKMessage(Commands.MSG_DONE_DAMAGE, damageFirstAttacker);
		PKMessage opponentMove = new PKMessage(Commands.MSG_OPPONENT_MOVE, secondMove);
		PKMessage damageDoneBySecond = new PKMessage(Commands.MSG_RECEIVED_DAMAGE, damageSecondAttacker);
		for(int i=0; i<toQueues.size(); i++) {
			if (toQueues.get(i).getId() == firstAttackerID) {
				toQueues.get(i).add(damageDoneByFirst);
				toQueues.get(i).add(opponentMove);
				toQueues.get(i).add(damageDoneBySecond);
			}
		}
		PKMessage trainerMove = new PKMessage(Commands.MSG_OPPONENT_MOVE, firstMove);
		PKMessage damageFirst = new PKMessage(Commands.MSG_RECEIVED_DAMAGE, damageFirstAttacker);
		PKMessage damageSecond = new PKMessage(Commands.MSG_DONE_DAMAGE, damageSecondAttacker);
		for(int i=0; i<toQueues.size(); i++) {
			if (toQueues.get(i).getId() != firstAttackerID) {
				toQueues.get(i).add(trainerMove);
				toQueues.get(i).add(damageFirst);
				toQueues.get(i).add(damageSecond);
			}
		}
	}
	
	private void returnTestConnection(PKMessage msg) {
		for(int i=0; i<toQueues.size(); i++) {
			if (toQueues.get(i).getId() == msg.getClientID())
				toQueues.get(i).add(msg);
		}
	}
	
	public void drawGUI() {
		view = new PKServerWindow(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		view.disableServerButton();
		this.start();
	}
}