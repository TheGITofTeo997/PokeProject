package it.unibs.pajc.pokeproject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;

public class PKMainClient extends Thread implements ActionListener{
	
	private static final String DATABASE_LOCATION = "pkDatabase.dat";
	private static final String ACQUA = "Acqua";
	private static final String FUOCO = "Fuoco";
	private static final String ERBA = "Erba";
	private static final String MSG_OPPONENT_MOVE = "msg_opponent_move";
	private static final String MSG_RECEIVED_DAMAGE = "msg_received_damage";
	private static final String MSG_DONE_DAMAGE = "msg_done_damage";
	private static final String MSG_BATTLE_OVER = "msg_battle_over";
	private static final String MSG_REMATCH = "msg_rematch";
	private static final String MSG_OPPONENT_POKEMON = "msg_opponent_pokemon";
	private static final String MSG_START_BATTLE = "msg_start_battle";
	private static final String MSG_WAITING = "msg_waiting";
	private static final String MSG_WAKEUP = "msg_wakeup";
	private static final int SERVER_PORT = 50000;
	private String SERVER_IP;
	private Socket socket;
	private ObjectInputStream fromServer;
	private ObjectOutputStream toServer;
	private ArrayList<Pokemon> loadedPkmn = new ArrayList<>();
	private TreeMap<Integer, Pokemon> pkDatabase = new TreeMap<>();
	private IdentifiedQueue<PKMessage> toSend = new IdentifiedQueue<>(10);
	private IdentifiedQueue<PKMessage> toReceive = new IdentifiedQueue<>(10);
	private PKMessage test = new PKMessage("msg_test" , 0);
	private static WaitingFrame wf = new WaitingFrame();
	private static BattleFrame bf = new BattleFrame();
	
	
	//vecchio main
	public void run() {
		checkForFileExistance();
	}
	
	public void setIP(String IP) {
		this.SERVER_IP = IP;
	}
	
	private void checkForFileExistance() {
		File pkDbase = new File(DATABASE_LOCATION);
		if(pkDbase.exists()) {
			try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pkDbase))){
				pkDatabase = (TreeMap<Integer, Pokemon>)ois.readObject();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
			loadPkmn();
			for(int i=0; i<loadedPkmn.size(); i++)
				pkDatabase.put(loadedPkmn.get(i).getID(), loadedPkmn.get(i));
			try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pkDbase))){
				oos.writeObject(pkDatabase);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean connectToServer() {
		try {
			socket = new Socket(SERVER_IP, SERVER_PORT);
			socket.setKeepAlive(true); // Potrebbe non servire
			System.out.println("Successfully connected to server at" + socket.getInetAddress());
			Thread.sleep(500);
			toServer = new ObjectOutputStream(socket.getOutputStream());
			PKClientSender sender = new PKClientSender(toServer, toSend);
			sender.start();
			fromServer = new ObjectInputStream(socket.getInputStream());
			PKClientReceiver receiver = new PKClientReceiver(fromServer, toReceive);
			receiver.start();
			//test message
			//toSend.add(new PKMessage("msg_waiting"));
			//
			
			/*while(true) {
				executeCommand(new PKMessage(MSG_WAITING));
				if(!toReceive.isEmpty())
				{
					PKMessage receivedMsg = toReceive.poll();
					System.out.println("Received " + receivedMsg.getCommandBody() + " from server(actually sent by client" + receivedMsg.getClientID() +")");			
				}
			}*/
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return socket.isConnected();
	}
	
	public void executeCommand(PKMessage msg) {
		switch(msg.getCommandBody()) {
		case MSG_WAITING:
			waiting();
			break;
		case MSG_WAKEUP:
			wakeup();
			break;
		case MSG_START_BATTLE:
			startBattle();
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
		}
	}
		
	private void loadPkmn() {
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
	
	
	//commands
	private static void waiting() {
		wf.setVisible(true);
	}
	
	private static void wakeup() {
		wf.setVisible(false);
	}
	
	private static void startBattle() {
		bf.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//start battle
	}

}