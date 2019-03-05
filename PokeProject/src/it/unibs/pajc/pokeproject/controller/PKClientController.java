package it.unibs.pajc.pokeproject.controller;

import java.io.*;
import java.net.*;
import java.util.*;

import it.unibs.pajc.pokeproject.model.*;
import it.unibs.pajc.pokeproject.view.*;

public class PKClientController{
	
	private PKLoader loader;
	private PKClientConnector connector;
	private PKBattleEnvironment battleEnvironment;
	
	private PKClientWindow view;
	
	public PKClientController() {
		loader = new PKLoader();
		connector = new PKClientConnector();
		battleEnvironment = new PKBattleEnvironment();
	}
	
	public boolean tryConnection(String ip) {
		return connector.connectToServer(ip);
	}
		
	public void setupClientUtils() {
		loader.loadTypes();
		loader.loadPokemon();
	}
	
	public void drawGUI() {
		view = new PKClientWindow();
		view.setController(this);
	}
}