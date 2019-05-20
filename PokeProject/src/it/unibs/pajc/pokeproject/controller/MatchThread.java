package it.unibs.pajc.pokeproject.controller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import it.unibs.pajc.pokeproject.model.PKLoader;
import it.unibs.pajc.pokeproject.model.Pokemon;
import it.unibs.pajc.pokeproject.util.*;

public class MatchThread implements Runnable {
	
	private PKLoader loader;
	
	private Pokemon pokePlayer1;
	private Pokemon pokePlayer2;
	
	private int moveSelectedBy1;
	private int moveSelectedBy2;
	
	private PKServerProtocol player1; 
	private PKServerProtocol player2;
	
	private ArrayBlockingQueue<PKMessage> messagesFrom1;
	private ArrayBlockingQueue<PKMessage> messagesFrom2;
	
	
	public MatchThread(PKServerProtocol player1, PKServerProtocol player2, PKLoader loader) {
		this.player1 = player1;
		this.player2 = player2;
		this.loader = loader;
		
		moveSelectedBy1 = -1;
		moveSelectedBy2 = -1;
		
		messagesFrom1 = new ArrayBlockingQueue<>(5);
		messagesFrom2 = new ArrayBlockingQueue<>(5);
		
		player1.setInputBuffer(messagesFrom1);
		player2.setInputBuffer(messagesFrom2);
	}
	
	public void run() {
		ScheduledExecutorService checkMessages = Executors.newSingleThreadScheduledExecutor();
		PKMessage msg = new PKMessage(Commands.MSG_PLAYER_FOUND);
		player1.sendMessage(msg);
		player2.sendMessage(msg);
		checkMessages.scheduleAtFixedRate(new Runnable() {
			public void run() {
				if(!(messagesFrom1.isEmpty())) {
					executeCommand(messagesFrom1.poll());
				}
				if(!(messagesFrom2.isEmpty())) {
					executeCommand(messagesFrom2.poll());
				}
			}
		}, 0, 1, TimeUnit.SECONDS);
	}
	
	private void executeCommand(PKMessage msg) {
		switch(msg.getCommandBody()) {
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
	
	private void selectedPokemon(PKMessage msg) {
		if(msg.getClientID() == player1.getClientID()) {
			/*
			 * The various MatchThreads use the same resources. So they need to be synchronized on that resource
			 * which is the loader. This is purely hypothetic and may be changed.
			 */
			synchronized(loader) { 
				pokePlayer1 = loader.getPokemonFromDB(msg.getDataToCarry());
			}
			pokePlayer1.setBattleID(msg.getClientID());
		}
		else {
			synchronized(loader) {
				pokePlayer2 = loader.getPokemonFromDB(msg.getDataToCarry());
			}
			pokePlayer2.setBattleID(msg.getClientID());
		}
		
		if(!(pokePlayer1 == null) && !(pokePlayer2 == null)) {
			PKMessage opponentFor1 = new PKMessage(Commands.MSG_OPPONENT_POKEMON, pokePlayer2.getID());
			PKMessage opponentFor2 = new PKMessage(Commands.MSG_OPPONENT_POKEMON, pokePlayer1.getID());
			player1.sendMessage(opponentFor1);
			player2.sendMessage(opponentFor2);
		}
		else {
			PKMessage wait = new PKMessage(Commands.MSG_WAITING);
			if(msg.getClientID() == player1.getClientID()) {
				player1.sendMessage(wait);
			}
			else {
				player2.sendMessage(wait);
			}
		}
	}
	
	private void selectedMove(PKMessage msg) {
		int selected = msg.getDataToCarry();
		//first, we understand which client send the move
		if(msg.getClientID() == pokePlayer1.getBattleID()) {
			moveSelectedBy1 = selected;
		}
		else {
			moveSelectedBy2 = selected;
		}	
		//then we send the wait, if needed
		if(moveSelectedBy2 == -1) { //if player1 sends the move first
			PKMessage wait = new PKMessage(Commands.MSG_WAITING);
			player1.sendMessage(wait);
		}
		else if(moveSelectedBy1 == -1){ //if player2 sends the move first
			PKMessage wait = new PKMessage(Commands.MSG_WAITING);
			player2.sendMessage(wait);
		}
		else { // everything is ready
			int firstAttackerID = setPriorityBattle(pokePlayer1, pokePlayer2);
			
			if(firstAttackerID == pokePlayer1.getBattleID() && firstAttackerID == msg.getClientID()) {
				sendSelectedMoveMessage(pokePlayer1, pokePlayer2, firstAttackerID, moveSelectedBy1, moveSelectedBy2);
			}
			else if(firstAttackerID == pokePlayer2.getBattleID() && firstAttackerID == msg.getClientID()) {
				sendSelectedMoveMessage(pokePlayer2, pokePlayer1, firstAttackerID, moveSelectedBy2, moveSelectedBy1);
			}
			else if(firstAttackerID == pokePlayer1.getBattleID() && firstAttackerID != msg.getClientID()) {
				sendSelectedMoveMessage(pokePlayer1, pokePlayer2, firstAttackerID, moveSelectedBy1, moveSelectedBy2);
			}
			else {
				sendSelectedMoveMessage(pokePlayer2, pokePlayer1, firstAttackerID, moveSelectedBy2, moveSelectedBy1);
			}
		}
	}
	
	//need rework
	private void sendSelectedMoveMessage(Pokemon firstAttacker, Pokemon secondAttacker, int firstAttackerID, int firstMove, int secondMove) {
		
		//damage calculation
		int damageFirstAttacker = calcDamage(firstAttacker, secondAttacker, firstMove);
		int damageSecondAttacker = calcDamage(secondAttacker, firstAttacker, secondMove);
		
		//message creation
		PKMessage damageDoneByFirst = new PKMessage(Commands.MSG_DONE_DAMAGE, damageFirstAttacker);
		PKMessage opponentMove = new PKMessage(Commands.MSG_OPPONENT_MOVE, secondMove);
		PKMessage damageDoneBySecond = new PKMessage(Commands.MSG_RECEIVED_DAMAGE, damageSecondAttacker);
		
		PKMessage trainerMove = new PKMessage(Commands.MSG_OPPONENT_MOVE, firstMove);
		PKMessage damageFirst = new PKMessage(Commands.MSG_RECEIVED_DAMAGE, damageFirstAttacker);
		PKMessage damageSecond = new PKMessage(Commands.MSG_DONE_DAMAGE, damageSecondAttacker);
		
		//message sending
		if(firstAttackerID == player1.getClientID()) {
			//messages for first to attack
			player1.sendMessage(damageDoneByFirst);
			player1.sendMessage(opponentMove);
			player1.sendMessage(damageDoneBySecond);
			
			//messages for second to attack
			player2.sendMessage(trainerMove);
			player2.sendMessage(damageFirst);
			player2.sendMessage(damageSecond);
		}
		else {
			//messages for first to attack
			player2.sendMessage(damageDoneByFirst);
			player2.sendMessage(opponentMove);
			player2.sendMessage(damageDoneBySecond);
			
			//messages for second to attack
			player1.sendMessage(trainerMove);
			player1.sendMessage(damageFirst);
			player1.sendMessage(damageSecond);
		}
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
	
	private int calcDamage(Pokemon attacker, Pokemon defender, int moveID) {
		double N = ThreadLocalRandom.current().nextDouble(0.85, 1);
		double stab = (attacker.getType().getTypeName().compareToIgnoreCase(attacker.getMove(moveID).getTypeName())==0) ? 1.5 : 1;
		double damage = ((((2*attacker.getLevel()+10)*attacker.getAttack()*attacker.getMove(moveID).getPwr()) / 
				(250*defender.getDefense()) ) + 2) * stab * attacker.getMove(moveID).getType().getEffectiveness(defender.getType().getTypeName())
				* N;
		return (int)damage;
	}
}
