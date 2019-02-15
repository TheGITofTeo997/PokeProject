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
	private static final int FIRST_QUEUE = 0;
	private static final int SECOND_QUEUE = 1;
	private static final int DEFALT_QUEUE_ID = -1;
	private static final int SERVER_PORT = 50000;
	private static final int QUEUE_SIZE = 5;
	private static final String SERVER_STARTED_SUCCESFULLY = "\nServer started on port 50000...";
	private static final String LOADED_PK_TREEMAP_SUCCESFULLY = "\nLoaded PK treemap...";
	private static final String DATABASE_LOCATION = "pkDatabase.dat";
	private static final String BULBASAUR = "Bulbasaur";
	private static final String CHARMANDER = "Charmander";
	private static final String SQUIRTLE = "Squirtle";
	private static final String CHIKORITA = "Chikorita";
	private static final String CYNDAQUIL = "Cyndaquil";
	private static final String TOTODILE = "Totodile";
	private static final String ACQUA = "Acqua";
	private static final String FUOCO = "Fuoco";
	private static final String ERBA = "Erba";
	private static final String MSG_REMATCH_NO = "msg_rematch_no";
	private static final String MSG_REMATCH_YES = "msg_rematch_yes";
	private static final String MSG_SELECTED_MOVE = "msg_selected_move";
	private static final String MSG_SELECTED_POKEMON = "msg_selected_pokemon";
	private static final String MSG_OPPONENT_POKEMON = "msg_opponent_pokemon";
	private static final String MSG_START_BATTLE = "msg_start_battle";
	private static final String MSG_WAITING = "msg_waiting";
	private static final String MSG_WAKEUP = "msg_wakeup";
	private static final String MSG_DONE_DAMAGE = "msg_done_damage";
	private static final String MSG_OPPONENT_MOVE = "msg_opponent_move";
	private static final String MSG_RECEIVED_DAMAGE = "msg_received_damage";
	
	private static TreeMap<Integer, Pokemon> pkDatabase = new TreeMap<>();
	private static ArrayList<Pokemon> loadedPkmn = new ArrayList<>();
	
	private Pokemon trainerPoke0;
	private Pokemon trainerPoke1;
	
	private ArrayList<IdentifiedQueue<PKMessage>> fromQueues = new ArrayList<>(); 
	// array di code da cui il server prenderà i messaggi che i client hanno mandato
	private ArrayList<IdentifiedQueue<PKMessage>> toQueues = new ArrayList<>(); 
	// array di code in cui il server metterà i messaggi da inviare ai client
	
	private int firstMoveSelectedID = -1;
	
	private PKServerWindow view;
	
	//Cerco di ripristinare l'entrypoint

	public PKServerController() {
		
	}
	
	public void run(){
		setupServerUtils();
		openConnection();
		/*while(true) {
			if(!fromQueues.get(FIRST_QUEUE).isEmpty())
				executeCommand(fromQueues.get(FIRST_QUEUE).poll());
			if(!fromQueues.get(SECOND_QUEUE).isEmpty())
				executeCommand(fromQueues.get(SECOND_QUEUE).poll());
		}*/
	}
	
	public void setupServerUtils() {
		checkForFileExistance();
		setupQueues();
	}
	
	//need to check for file type existance
	
	@SuppressWarnings("unchecked")
	private void checkForFileExistance() {
		File pkDbaseFile = new File(DATABASE_LOCATION);
		if(pkDbaseFile.exists()) { // dobbiamo leggere il file solo se esiste
			try(ObjectInputStream databaseReader = new ObjectInputStream(new FileInputStream(pkDbaseFile))){
				pkDatabase = (TreeMap<Integer, Pokemon>)databaseReader.readObject(); // lettura treemap da file
				view.appendTextToConsole(LOADED_PK_TREEMAP_SUCCESFULLY);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		else { // altrimenti lo creiamo noi
			loadPkmn();
			for(int i=0; i<loadedPkmn.size(); i++)
				pkDatabase.put(loadedPkmn.get(i).getID(), loadedPkmn.get(i));
			try(ObjectOutputStream databaseWriter = new ObjectOutputStream(new FileOutputStream(pkDbaseFile))){
				databaseWriter.writeObject(pkDatabase); // scrittura treemap su file
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
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
			view.appendTextToConsole(SERVER_STARTED_SUCCESFULLY);
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
	
	public void loadPkmn() {
		Pokemon bulbasaur = new Pokemon(BULBASAUR, new PKType(ERBA));
		Pokemon charmander = new Pokemon(CHARMANDER, new PKType(FUOCO));			
		Pokemon squirtle = new Pokemon (SQUIRTLE, new PKType(ACQUA));
		Pokemon chikorita = new Pokemon(CHIKORITA, new PKType(ERBA));
		Pokemon cyndaquil = new Pokemon(CYNDAQUIL, new PKType(FUOCO));
		Pokemon totodile = new Pokemon(TOTODILE, new PKType(ACQUA));
		loadedPkmn.add(bulbasaur);
		loadedPkmn.add(charmander);
		loadedPkmn.add(squirtle);
		loadedPkmn.add(chikorita);
		loadedPkmn.add(cyndaquil);
		loadedPkmn.add(totodile);
	}
	
	public void executeCommand(PKMessage msg) {
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
		if(trainerPoke0.equals(null)) {
		trainerPoke0 = pkDatabase.get(msg.getDataToCarry());
		trainerPoke0.setBattleID(msg.getClientID());
		}
		else {
		trainerPoke1 = pkDatabase.get(msg.getDataToCarry());
		trainerPoke1.setBattleID(msg.getClientID());
		}
		if(!trainerPoke0.equals(null) && !trainerPoke1.equals(null)) {
			PKMessage startBattle = new PKMessage(MSG_START_BATTLE);
			PKMessage wakeup = new PKMessage(MSG_WAKEUP);
			PKMessage opponentFor0 = new PKMessage(MSG_OPPONENT_POKEMON, trainerPoke1.getID());
			PKMessage opponentFor1 = new PKMessage(MSG_OPPONENT_POKEMON, trainerPoke0.getID());
			toQueues.get(FIRST_QUEUE).add(wakeup);
			toQueues.get(SECOND_QUEUE).add(wakeup);		
			toQueues.get(FIRST_QUEUE).add(opponentFor0);
			toQueues.get(SECOND_QUEUE).add(opponentFor1);
			toQueues.get(FIRST_QUEUE).add(startBattle);
			toQueues.get(SECOND_QUEUE).add(startBattle);		
			
		}
		else {
			PKMessage wait = new PKMessage(MSG_WAITING);
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
			PKMessage wait = new PKMessage(MSG_WAITING);
			for(int i=0; i<toQueues.size(); i++) {
				if (toQueues.get(i).getId() == msg.getClientID())
					toQueues.get(i).add(wait);
			}
		}
		else {
			PKMessage wakeup = new PKMessage(MSG_WAKEUP);
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
	
	private void sendSelectedMoveMessage(Pokemon firstAttacker, Pokemon secondAttacker, int firstAttackerID, int firstMove, int secondMove) {
		int damageFirstAttacker = calcDamage(firstAttacker, secondAttacker, firstMove);
		int damageSecondAttacker = calcDamage(secondAttacker, firstAttacker, secondMove);
		PKMessage damageDoneByFirst = new PKMessage(MSG_DONE_DAMAGE, damageFirstAttacker);
		PKMessage opponentMove = new PKMessage(MSG_OPPONENT_MOVE, secondMove);
		PKMessage damageDoneBySecond = new PKMessage(MSG_RECEIVED_DAMAGE, damageSecondAttacker);
		for(int i=0; i<toQueues.size(); i++) {
			if (toQueues.get(i).getId() == firstAttackerID) {
				toQueues.get(i).add(damageDoneByFirst);
				toQueues.get(i).add(opponentMove);
				toQueues.get(i).add(damageDoneBySecond);
			}
		}
		PKMessage trainerMove = new PKMessage(MSG_OPPONENT_MOVE, firstMove);
		PKMessage damageFirst = new PKMessage(MSG_RECEIVED_DAMAGE, damageFirstAttacker);
		PKMessage damageSecond = new PKMessage(MSG_DONE_DAMAGE, damageSecondAttacker);
		for(int i=0; i<toQueues.size(); i++) {
			if (toQueues.get(i).getId() != firstAttackerID) {
				toQueues.get(i).add(trainerMove);
				toQueues.get(i).add(damageFirst);
				toQueues.get(i).add(damageSecond);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		view.disableServerButton();
		this.start();
	}
	
	public void setView(PKServerWindow view) {
		this.view = view;
	}
	
}