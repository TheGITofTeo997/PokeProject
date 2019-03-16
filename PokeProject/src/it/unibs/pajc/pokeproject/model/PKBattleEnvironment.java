package it.unibs.pajc.pokeproject.model;

import it.unibs.pajc.pokeproject.util.PKMessage;

public class PKBattleEnvironment {

	private Pokemon ourPokemon;
	private Pokemon enemyPokemon;
	
	public void executeCommand(PKMessage msg) {
		switch(msg.getCommandBody()) {
		case MSG_WAITING:
			break;
		case MSG_WAKEUP:
			//wakeup();
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
}
