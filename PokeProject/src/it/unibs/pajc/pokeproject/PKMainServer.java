package it.unibs.pajc.pokeproject;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.io.*;
import java.net.*;

public class PKMainServer extends Thread{
	private static final int FIRST_QUEUE = 0;
	private static final int SECOND_QUEUE = 1;
	private static final int DEFALT_QUEUE_ID = -1;
	private static final int SERVER_PORT = 50000;
	private static final int QUEUE_SIZE = 5;
	private static final String SERVER_STARTED_SUCCESFULLY = "\nServer started on port 50000...";
	private static final String LOADED_PK_TREEMAP_SUCCESFULLY = "\nLoaded PK treemap...";
	private static final String DATABASE_LOCATION = "pkDatabase.dat";
	private static final String ACQUA = "Acqua";
	private static final String FUOCO = "Fuoco";
	private static final String ERBA = "Erba";
	private static final String MSG_REMATCH_NO = "msg_rematch_no";
	private static final String MSG_REMATCH_YES = "msg_rematch_yes";
	private static final String MSG_SELECTED_MOVE = "msg_selected_move";
	private static final String MSG_SELECTED_POKEMON = "msg_selected_pokemon";
	private static final String MSG_START_BATTLE = "msg_start_battle";
	private static final String MSG_WAITING = "msg_waiting";
	private static final String MSG_WAKEUP = "msg_wakeup";
	private static final String SUCCESFULLY_ADDED_MESSAGE_TO_SENDING_QUEUE = "\nSuccesfully added the message to the sending queue :)";
	
	private static TreeMap<Integer, Pokemon> pkDatabase = new TreeMap<>();
	private static ArrayList<Pokemon> loadedPkmn = new ArrayList<>();
	private Pokemon trainerPoke0;
	private Pokemon trainerPoke1;
	private ArrayList<IdentifiedQueue<PKMessage>> fromQueues = new ArrayList<>(); 
	// coda da cui il server prenderà i messaggi che i client hanno mandato
	private ArrayList<IdentifiedQueue<PKMessage>> toQueues = new ArrayList<>(); 
	// coda in cui il server metterà i messaggi da inviare ai client
	
	//Questo era il vecchio main, ora non è più entrypoint poichè viene fatto dalla window
	public void run(){
		initialize();
	}
	
	public void initialize() {
		checkForFileExistance();
		setupQueues();
	    openConnection();
	}
	
	private void checkForFileExistance() {
		File pkDbaseFile = new File(DATABASE_LOCATION);
		if(pkDbaseFile.exists()) { // dobbiamo leggere il file solo se esiste
			try(ObjectInputStream databaseReader = new ObjectInputStream(new FileInputStream(pkDbaseFile))){
				pkDatabase = (TreeMap<Integer, Pokemon>)databaseReader.readObject(); // lettura treemap da file
				PKServerWindow.appendTextToConsole(LOADED_PK_TREEMAP_SUCCESFULLY);
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
			server = new ServerSocket(SERVER_PORT);
			PKServerWindow.appendTextToConsole(SERVER_STARTED_SUCCESFULLY);
			while(true) {  
		    Socket client = server.accept();
		    PKServerProtocol protocol = new PKServerProtocol(client);
		    
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
		   	//solo quando vengono assegnate le seconde code posso fare il test
		   	//e ciò avviene quando i 2 client sono connessi
		    if(toQueues.get(SECOND_QUEUE).getId()!=DEFALT_QUEUE_ID) 
				test();
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadPkmn() {
		Pokemon bulbasaur = new Pokemon("Bulbasaur", ERBA);
		Pokemon charmander = new Pokemon("Charmander", FUOCO);		
		Pokemon squirtle = new Pokemon ("Squirtle", ACQUA);
		Pokemon chikorita = new Pokemon("Chikorita", ERBA);
		Pokemon cyndaquil = new Pokemon("Cyndaquil", FUOCO);
		Pokemon totodile = new Pokemon("Totodile", ACQUA);
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
			break;
		case MSG_REMATCH_YES:
			break;
		case MSG_REMATCH_NO:
			break;	
		}
	}
	
	private void test() {
		/*
		 * Prendo il messaggio che il primo client ha inviato, che si trova nella prima coda di ricezione, e lo metto
		 * sulla seconda di invio, quella per mandare al secondo client.
		 */
		if(toQueues.get(SECOND_QUEUE).add((fromQueues.get(FIRST_QUEUE).poll()))) 
			PKServerWindow.appendTextToConsole(SUCCESFULLY_ADDED_MESSAGE_TO_SENDING_QUEUE);
		
		//questo è un workaround temporaneo perché forse c'è un problema di concorrenza
		if(fromQueues.get(SECOND_QUEUE).poll() == null) {
			/*
			 * La teoria dietro a questo è che il MainServer debba aspettare che il serverReceiver metta
			 * il messaggio nella coda. Quindi si addormenta per un secondo, il serverReceiver mette il messaggio,
			 * il mainServer lo prende e fa quello che deve fare
			 */
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			toQueues.get(FIRST_QUEUE).add((fromQueues.get(SECOND_QUEUE).poll()));
			PKServerWindow.appendTextToConsole(SUCCESFULLY_ADDED_MESSAGE_TO_SENDING_QUEUE);
		}
		else
		{
			toQueues.get(FIRST_QUEUE).add((fromQueues.get(SECOND_QUEUE).poll()));
			PKServerWindow.appendTextToConsole(SUCCESFULLY_ADDED_MESSAGE_TO_SENDING_QUEUE);
		}
	}
	
	/*
	public int setPriorityBattle(Pokemon p0, Pokemon p1) {
		int speedP0 = p0.getSpeed();
		int speedP1 = p1.getSpeed();
		if(speedP0>=speedP1) {
			return ID0;
		}
		else
			return ID1;
	}*/
	
	/*public int damageToSend(int pwrAttack) {
		int damageToSend=0;
		return damageToSend;
	}
	*/
	//Il server riceverà in ingresso i due pkmn e ad ognuno assegnerà l'ID, in base al quale
	//attaccheranno e verranno identificati all'interno dei metodi.
	
	
	private void selectedPokemon(PKMessage msg) {
		if(trainerPoke0.equals(null))
		trainerPoke0 = pkDatabase.get(msg.getDataToCarry());
		else
		trainerPoke1 = pkDatabase.get(msg.getDataToCarry());
		if(!trainerPoke0.equals(null) && !trainerPoke1.equals(null)) {
			PKMessage startBattle = new PKMessage(MSG_START_BATTLE);
			PKMessage wakeup = new PKMessage(MSG_WAKEUP);
			toQueues.get(FIRST_QUEUE).add(wakeup);
			toQueues.get(SECOND_QUEUE).add(wakeup);		
			toQueues.get(FIRST_QUEUE).add(startBattle);
			toQueues.get(SECOND_QUEUE).add(startBattle);		
		}
		else {
			PKMessage wait = new PKMessage(MSG_WAITING);
			toQueues.get(msg.getClientID()).add(wait);
		}
	}
	
	private void calcDamage(Pokemon attacker, Pokemon defender, int moveID) {
		double N = ThreadLocalRandom.current().nextDouble(0.85, 1);
		double stab = (attacker.getType().compareToIgnoreCase(attacker.getMove(moveID).getType())==0) ? 1.5 : 1;
		double damage = ((((2*attacker.getLevel()+10)*attacker.getAttack()*attacker.getMove(moveID).getPwr()) / 
				(250*defender.getDefense()) ) + 2) * stab * calcEffectiveness(attacker.getMove(moveID).getType(), defender.getType())
				* N;
		
	}
	
	private double calcEffectiveness(String moveType, String DefenderType) {
		//still needs implementation
		double effectiveness = 0;
		return effectiveness;
	}
	
}
