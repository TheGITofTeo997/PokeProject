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
	
	private Pokemon pokePlayerOne;
	private Pokemon pokePlayerTwo;
	
	private int moveSelectedByOne;
	private int moveSelectedByTwo;
	
	private PKServerProtocol playerOne; 
	private PKServerProtocol playerTwo;
	
	private ArrayBlockingQueue<PKMessage> messagesFromOne;
	private ArrayBlockingQueue<PKMessage> messagesFromTwo;
	
	
	public MatchThread(PKServerProtocol playerOne, PKServerProtocol playerTwo, PKLoader loader) {
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
		this.loader = loader;
		
		moveSelectedByOne = -1;
		moveSelectedByTwo = -1;
		
		messagesFromOne = new ArrayBlockingQueue<>(5);
		messagesFromTwo = new ArrayBlockingQueue<>(5);
		
		playerOne.setInputBuffer(messagesFromOne);
		playerTwo.setInputBuffer(messagesFromTwo);
	}
	
	public void run() {
		ScheduledExecutorService checkMessages = Executors.newSingleThreadScheduledExecutor();
		PKMessage msg = new PKMessage(Commands.MSG_PLAYER_FOUND);
		playerOne.sendMessage(msg);
		playerTwo.sendMessage(msg);
		checkMessages.scheduleAtFixedRate(new Runnable() {
			public void run() {
				if(!(messagesFromOne.isEmpty())) {
					executeCommand(messagesFromOne.poll());
				}
				if(!(messagesFromTwo.isEmpty())) {
					executeCommand(messagesFromTwo.poll());
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
		if(msg.getClientID() == playerOne.getClientID()) {
			/*
			 * The various MatchThreads use the same resources. So they need to be synchronized on that resource
			 * which is the loader. This is purely hypothetic and may be changed.
			 */
			synchronized(loader) { 
				pokePlayerOne = loader.getPokemonFromDB(msg.getDataToCarry());
			}
			pokePlayerOne.setBattleID(msg.getClientID());
		}
		else {
			synchronized(loader) {
				pokePlayerTwo = loader.getPokemonFromDB(msg.getDataToCarry());
			}
			pokePlayerTwo.setBattleID(msg.getClientID());
		}
		
		if(!(pokePlayerOne == null) && !(pokePlayerTwo == null)) {
			PKMessage opponentFor1 = new PKMessage(Commands.MSG_OPPONENT_POKEMON, pokePlayerTwo.getID());
			PKMessage opponentFor2 = new PKMessage(Commands.MSG_OPPONENT_POKEMON, pokePlayerOne.getID());
			playerOne.sendMessage(opponentFor1);
			playerTwo.sendMessage(opponentFor2);
		}
		else {
			PKMessage wait = new PKMessage(Commands.MSG_WAITING);
			if(msg.getClientID() == playerOne.getClientID()) {
				playerOne.sendMessage(wait);
			}
			else {
				playerTwo.sendMessage(wait);
			}
		}
	}
	
	private void selectedMove(PKMessage msg) {
		int selected = msg.getDataToCarry();
		//first, we understand which client sent the move
		if(msg.getClientID() == pokePlayerOne.getBattleID()) {
			moveSelectedByOne = selected;
			System.out.println("mossa selezionata da 1");
		}
		else {
			moveSelectedByTwo = selected;
			System.out.println("mossa selezionata da 2");
		}	
		//then we send the wait, if needed
		if(moveSelectedByTwo == -1) { //if player1 sends the move first
			PKMessage wait = new PKMessage(Commands.MSG_WAITING);
			playerOne.sendMessage(wait);
			System.out.println("wait 1");
		}
		else if(moveSelectedByOne == -1){ //if player2 sends the move first
			PKMessage wait = new PKMessage(Commands.MSG_WAITING);
			playerTwo.sendMessage(wait);
			System.out.println("wait 2");
		}
		else { // everything is ready
			System.out.println("mosse inviate da tutti");
			int firstAttackerID = setPriorityBattle(pokePlayerOne, pokePlayerTwo);
			
			if(firstAttackerID == pokePlayerOne.getBattleID()) {
				sendSelectedMoveMessage(pokePlayerOne, pokePlayerTwo, firstAttackerID, moveSelectedByOne, moveSelectedByTwo);
				System.out.println("Il giocatore che si è connesso per primo attacca per primo");
			}
			else {
				sendSelectedMoveMessage(pokePlayerTwo, pokePlayerOne, firstAttackerID, moveSelectedByTwo, moveSelectedByOne);
				System.out.println("Il giocatore che si è connesso per secondo attacca per primo");
			}
			//Refreshing moves
			moveSelectedByOne = -1;
			moveSelectedByTwo = -1;
		}
	}
	
	//need rework
	private void sendSelectedMoveMessage(Pokemon firstAttacker, Pokemon secondAttacker, int firstAttackerID, int firstMove, int secondMove) {
		
		//damage calculation
		int damageFirstAttacker = calcDamage(firstAttacker, secondAttacker, firstMove);
		int damageSecondAttacker = calcDamage(secondAttacker, firstAttacker, secondMove);
		
		//message creation
		PKMessage damageDoneByFirst = new PKMessage(Commands.MSG_DONE_DAMAGE, damageFirstAttacker);
		PKMessage damageReceivedByFirst = new PKMessage(Commands.MSG_RECEIVED_DAMAGE, damageSecondAttacker);
		PKMessage moveOfSecondAttacker = new PKMessage(Commands.MSG_OPPONENT_MOVE, secondMove);
		PKMessage moveOfFirstAttacker = new PKMessage(Commands.MSG_OPPONENT_MOVE, firstMove);
		PKMessage damageReceivedBySecond = new PKMessage(Commands.MSG_RECEIVED_DAMAGE, damageFirstAttacker);
		PKMessage damageDoneBySecond = new PKMessage(Commands.MSG_DONE_DAMAGE, damageSecondAttacker);
		
		//message sending
		if(firstAttackerID == playerOne.getClientID()) {
			//messages for first to attack
			playerOne.sendMessage(damageDoneByFirst);
			playerOne.sendMessage(moveOfSecondAttacker);
			playerOne.sendMessage(damageReceivedByFirst);
			
			//messages for second to attack
			playerTwo.sendMessage(moveOfFirstAttacker);
			playerTwo.sendMessage(damageReceivedBySecond);
			playerTwo.sendMessage(damageDoneBySecond);
		}
		else {
			//messages for first to attack
			playerTwo.sendMessage(damageDoneByFirst);
			playerTwo.sendMessage(moveOfSecondAttacker);
			playerTwo.sendMessage(damageReceivedByFirst);
			
			//messages for second to attack
			playerOne.sendMessage(moveOfFirstAttacker);
			playerOne.sendMessage(damageReceivedBySecond);
			playerOne.sendMessage(damageDoneBySecond);
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
