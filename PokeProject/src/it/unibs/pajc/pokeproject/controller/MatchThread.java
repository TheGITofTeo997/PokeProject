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
	private Logger logger;
	private ScheduledExecutorService checkMessages;
	
	private Pokemon pokePlayerOne;
	private Pokemon pokePlayerTwo;
	
	private PKServerProtocol playerOne; 
	private PKServerProtocol playerTwo;
	
	private ArrayBlockingQueue<PKMessage> messagesFromOne;
	private ArrayBlockingQueue<PKMessage> messagesFromTwo;
	
	private int moveSelectedByOne;
	private int moveSelectedByTwo;
	
	private boolean rematchPlayerOne;
	private boolean rematchPlayerTwo;
	
	public MatchThread(PKServerProtocol playerOne, PKServerProtocol playerTwo, PKLoader loader) {
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
		this.loader = loader;
		logger = new Logger(PKServerStrings.MATCH_LOG_FILE);
		
		rematchPlayerOne = false;
		rematchPlayerTwo = false;
		
		moveSelectedByOne = -1;
		moveSelectedByTwo = -1;
		
		messagesFromOne = new ArrayBlockingQueue<>(5);
		messagesFromTwo = new ArrayBlockingQueue<>(5);
		
		playerOne.setInputBuffer(messagesFromOne);
		playerTwo.setInputBuffer(messagesFromTwo);
	}
	
	public void run() {
		PKMessage msg = new PKMessage(Commands.MSG_PLAYER_FOUND);
		playerOne.sendMessage(msg);
		playerTwo.sendMessage(msg);
		logger.writeLog(PKServerStrings.SENT_PLAYER_FOUND_MESSAGES);
		checkMessages = Executors.newSingleThreadScheduledExecutor();
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
		logger.writeLog(PKServerStrings.MATCH_CHECK_MESSAGES);
	}
	
	private void executeCommand(PKMessage msg) {
		switch(msg.getCommandBody()) {
		case MSG_SELECTED_POKEMON:
			selectedPokemon(msg);
			break;
		case MSG_SELECTED_MOVE:
			selectedMove(msg);
			break;
		case MSG_REMATCH:
			rematch(msg);
			break;	
		default:
			break;
		}
	}
	
	private void selectedPokemon(PKMessage msg) {
		if(msg.getClientID() == playerOne.getClientID()) {
			logger.writeLog(PKServerStrings.SELECTED_POKEMON_FROM_ONE);
			synchronized(loader) { 
				pokePlayerOne = loader.getPokemonFromDB(msg.getDataToCarry());
			}
			pokePlayerOne.setBattleID(msg.getClientID());
			pokePlayerOne.setBattleHP(pokePlayerOne.getHP());
		}
		else {
			logger.writeLog(PKServerStrings.SELECTED_POKEMON_FROM_TWO);
			synchronized(loader) {
				pokePlayerTwo = loader.getPokemonFromDB(msg.getDataToCarry());
			}
			pokePlayerTwo.setBattleID(msg.getClientID());
			pokePlayerTwo.setBattleHP(pokePlayerTwo.getHP());
		}
		if(!(pokePlayerOne == null) && !(pokePlayerTwo == null)) {
			PKMessage opponentFor1 = new PKMessage(Commands.MSG_OPPONENT_POKEMON, pokePlayerTwo.getID());
			PKMessage opponentFor2 = new PKMessage(Commands.MSG_OPPONENT_POKEMON, pokePlayerOne.getID());
			playerOne.sendMessage(opponentFor1);
			playerTwo.sendMessage(opponentFor2);
			logger.writeLog(PKServerStrings.OPPONENT_POKEMON_MESSAGES);
		}
	}
	
	private void selectedMove(PKMessage msg) {
		int selected = msg.getDataToCarry();
		//first, we understand which client sent the move
		if(msg.getClientID() == pokePlayerOne.getBattleID()) {
			moveSelectedByOne = selected;
			logger.writeLog(PKServerStrings.SELECTED_MOVE_FROM_ONE);
		}
		else {
			moveSelectedByTwo = selected;
			logger.writeLog(PKServerStrings.SELECTED_MOVE_FROM_TWO);
		}	
		//then we send the wait, if needed
		
		if(moveSelectedByOne != -1 && moveSelectedByTwo != -1) 
		{ // everything is ready
			int firstAttackerID = setPriorityBattle(pokePlayerOne, pokePlayerTwo);
			
			if(firstAttackerID == pokePlayerOne.getBattleID()) {
				sendSelectedMoveMessage(pokePlayerOne, pokePlayerTwo, firstAttackerID, moveSelectedByOne, moveSelectedByTwo);
			}
			else {
				sendSelectedMoveMessage(pokePlayerTwo, pokePlayerOne, firstAttackerID, moveSelectedByTwo, moveSelectedByOne);
			}
			
			logger.writeLog(PKServerStrings.TURN_DONE);
			
			//Refreshing moves for next turn
			moveSelectedByOne = -1;
			moveSelectedByTwo = -1;
		}
	}
	
	private void sendSelectedMoveMessage(Pokemon firstAttacker, Pokemon secondAttacker, int firstAttackerID, int firstMove, int secondMove) {
		int damageFirstAttacker = calcDamage(firstAttacker, secondAttacker, firstMove);
		int damageSecondAttacker = calcDamage(secondAttacker, firstAttacker, secondMove);
		
		//remaining health remaining
		firstAttacker.setBattleHP(firstAttacker.getBattleHP() - damageSecondAttacker);
		secondAttacker.setBattleHP(secondAttacker.getBattleHP() - damageFirstAttacker);
		
		//optimistic message creation
		PKMessage damageDoneByFirst = new PKMessage(Commands.MSG_DONE_DAMAGE, damageFirstAttacker);
		PKMessage moveOfFirstAttacker = new PKMessage(Commands.MSG_OPPONENT_MOVE, firstMove);
		PKMessage damageReceivedBySecond = new PKMessage(Commands.MSG_RECEIVED_DAMAGE, damageFirstAttacker);
		
		//message sending
		if(isDead(secondAttacker))
		{
			logger.writeLog(PKServerStrings.SECOND_ATTACKER_DEAD + secondAttacker.getName());
			PKMessage battleOver = new PKMessage(Commands.MSG_BATTLE_OVER);
			if(playerOne.getClientID() == firstAttackerID)
			{
				//messages for first to attack
				playerOne.sendMessage(damageDoneByFirst);
				playerOne.sendMessage(battleOver);
				
				//messages for second to attack
				playerTwo.sendMessage(moveOfFirstAttacker);
				playerTwo.sendMessage(damageReceivedBySecond);
				playerTwo.sendMessage(battleOver);
			}
			else
			{
				//messages for first to attack
				playerTwo.sendMessage(damageDoneByFirst);
				playerTwo.sendMessage(battleOver);
				
				//messages for second to attack
				playerOne.sendMessage(moveOfFirstAttacker);
				playerOne.sendMessage(damageReceivedBySecond);
				playerOne.sendMessage(battleOver);
			}
		}
		else 
		{
			//creation of remaining messages
			PKMessage damageReceivedByFirst = new PKMessage(Commands.MSG_RECEIVED_DAMAGE, damageSecondAttacker);
			PKMessage moveOfSecondAttacker = new PKMessage(Commands.MSG_OPPONENT_MOVE, secondMove);
			PKMessage damageDoneBySecond = new PKMessage(Commands.MSG_DONE_DAMAGE, damageSecondAttacker);
				
			if(playerOne.getClientID() == firstAttackerID)
			{
				//messages for first to attack
				playerOne.sendMessage(damageDoneByFirst);
				playerOne.sendMessage(moveOfSecondAttacker);
				playerOne.sendMessage(damageReceivedByFirst);
					
				//messages for second to attack
				playerTwo.sendMessage(moveOfFirstAttacker);
				playerTwo.sendMessage(damageReceivedBySecond);
				playerTwo.sendMessage(damageDoneBySecond);
			}
			else
			{
				//messages for first to attack
				playerTwo.sendMessage(damageDoneByFirst);
				playerTwo.sendMessage(moveOfSecondAttacker);
				playerTwo.sendMessage(damageReceivedByFirst);
					
				//messages for second to attack
				playerOne.sendMessage(moveOfFirstAttacker);
				playerOne.sendMessage(damageReceivedBySecond);
				playerOne.sendMessage(damageDoneBySecond);
			}
			if(isDead(firstAttacker))
			{
				logger.writeLog(PKServerStrings.FIRST_ATTACKER_DEAD + firstAttacker.getName());
				PKMessage battleOver = new PKMessage(Commands.MSG_BATTLE_OVER);
				playerOne.sendMessage(battleOver);
				playerTwo.sendMessage(battleOver);
			}
		}
	}
	
	private boolean isDead(Pokemon poke) {
		return (poke.getBattleHP() == 0);
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
	
	private void rematch(PKMessage msg) {
		if(playerOne.getClientID() == msg.getClientID())
			if(msg.getDataToCarry() == 1)
				rematchPlayerOne = true;
			else
			{
				PKMessage noRematch = new PKMessage(Commands.MSG_REMATCH_NO);
				playerTwo.sendMessage(noRematch);
			}
		else if(msg.getDataToCarry() == 1)
			rematchPlayerTwo = true;
		else
		{
			PKMessage noRematch = new PKMessage(Commands.MSG_REMATCH_NO);
			playerOne.sendMessage(noRematch);
		}
		if(rematchPlayerOne && rematchPlayerTwo)
		{
			pokePlayerOne = null;
			pokePlayerTwo = null;
			PKMessage rematch = new PKMessage(Commands.MSG_REMATCH_YES);
			playerOne.sendMessage(rematch);
			playerTwo.sendMessage(rematch);
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
	
	public void end() {
		checkMessages.shutdown();
		playerOne.closeConnection();
		playerTwo.closeConnection();
	}
	
	public boolean checkConnection() {
		if(!playerOne.isConnected() || !playerTwo.isConnected())
			return true;
		else
			return false;
	}
	
	public void writeConnectionClosed(PKMessage connectionClosed) {
		playerOne.sendMessage(connectionClosed);
		playerTwo.sendMessage(connectionClosed);
	}
}
