package it.unibs.pajc.pokeproject.controller;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;

import it.unibs.pajc.pokeproject.model.*;
import it.unibs.pajc.pokeproject.util.*;
import it.unibs.pajc.pokeproject.view.*;

public class PKServerController extends Thread implements ActionListener {
	
	//Local Components
	private static final int SERVER_PORT = 50000;
	private int connectedClients;
	
	//Controller Components
	private LinkedList<PKServerProtocol> playersQueue;
	private ExecutorService playerExecutor;
	private ExecutorService matchExecutor;
	private Logger logger;
	private PKLoader loader;
	private Pokemon trainerPoke0;
	private Pokemon trainerPoke1; 

	private int firstMoveSelectedID = -1;
	
	//View Components
	private PKServerWindow view;

	public PKServerController() {
		playersQueue = new LinkedList<>();
		playerExecutor = Executors.newFixedThreadPool(64);
		matchExecutor = Executors.newFixedThreadPool(32);
		logger = new Logger(PKServerStrings.LOGFILE);
		loader = new PKLoader();
	}
	
	public void run(){
		setupServerUtils();
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
	}
	
	public void openConnection() {
		connectedClients = 0;
		
		ScheduledExecutorService checkClients = Executors.newSingleThreadScheduledExecutor();
		checkClients.scheduleAtFixedRate(new Runnable() {
			public void run() {
				//eventually remove disconnected player
				startMatch();
			}
		}, 0, 1, TimeUnit.SECONDS);
		
		try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
			view.appendTextToConsole(PKServerStrings.SERVER_STARTED_SUCCESFULLY);
			while(true) {
				Socket clientSocket = server.accept();
				logger.writeLog(PKServerStrings.CLIENT_CONNECTED);
				PKServerProtocol player = new PKServerProtocol(clientSocket, view);
				logger.writeLog(PKServerStrings.CLIENT_REQUEST_HANDLED);
				playersQueue.add(player);
				playerExecutor.execute(player);
				connectedClients++;
				logger.writeLog(PKServerStrings.CLIENT_REQUEST_HANDLED_OK);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startMatch() {
		if(playersQueue.size() >= 2) {
			PKServerProtocol player1 = playersQueue.poll();
			PKServerProtocol player2 = playersQueue.poll();
			MatchThread match = new MatchThread(player1, player2, loader);
			matchExecutor.execute(match);
		}
	}
	
	public void drawGUI() {
		view = new PKServerWindow(this);
		view.addWindowAdapter(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				logger.writeLog(view.getConsoleText());
				logger.closeLogger();
				System.exit(0);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		view.disableServerButton();
		this.start();
	}
}