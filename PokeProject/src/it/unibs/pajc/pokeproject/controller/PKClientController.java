package it.unibs.pajc.pokeproject.controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import it.unibs.pajc.pokeproject.model.*;
import it.unibs.pajc.pokeproject.util.PKMessage;
import it.unibs.pajc.pokeproject.view.*;
import it.unibs.pajc.pokeproject.util.Commands;

public class PKClientController{
	
	private PKLoader loader;
	private PKClientConnector connector;
	private PKBattleEnvironment battleEnvironment;
	private JFrame view;
	
	private static final String TITLE = "PokeBattle Client v0.4";
	private MainPanel mainPanel = null;
	private IpPanel ipPanel = null;
	private PokeChooserPanel pokeChooserPanel = null;
	private BattlePanel battlePanel = null;
	
	
	public PKClientController() {
		loader = new PKLoader();
		battleEnvironment = new PKBattleEnvironment();
		connector = new PKClientConnector(battleEnvironment);
	}
		
	public void setupClientUtils() {
		loader.loadTypes();
		loader.loadPokemon();
	}
	
	public void drawGUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		view = new JFrame(TITLE);
		view.setResizable(false);
		view.setBounds(100, 100, 600, 450);
		view.setLocationRelativeTo(null);
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view.getContentPane().setLayout(null);	
		view.setVisible(true);
		drawMainPanel();
	}
	
	public void drawIpPanel() {
		ipPanel = new IpPanel();
		ipPanel.setBounds(0, 0, 663, 429);
		ipPanel.setVisible(true);
		view.getContentPane().add(ipPanel);
		ipPanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(connector.connectToServer(ipPanel.getIP())) {		
					drawPokeChooserPanel();
					view.setBounds(view.getX(), view.getY(), pokeChooserPanel.getWidth(), pokeChooserPanel.getHeight());
					ipPanel.setVisible(false);
					//ciclo for per mostrare i pokemon
				}
				else {
					showErrorPopup();
				}
			}
		});
	}
	
	public void drawMainPanel() {
		mainPanel = new MainPanel();
		mainPanel.setBounds(0, 0, 594, 421);
		mainPanel.setVisible(true);
		view.getContentPane().add(mainPanel);
		
		mainPanel.addSinglePlayerListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawPokeChooserPanel();
				view.setBounds(view.getX(), view.getY(), pokeChooserPanel.getWidth(), pokeChooserPanel.getHeight());
				mainPanel.setVisible(false); 
			}
		});
		
		mainPanel.addMultiPlayerListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawIpPanel();
				view.setBounds(view.getX(), view.getY(), ipPanel.getWidth(), ipPanel.getHeight());
				mainPanel.setVisible(false); 
			}
		});
	}
	
	public void drawPokeChooserPanel() {
		pokeChooserPanel = new PokeChooserPanel();
		pokeChooserPanel.setBounds(0, 0, 650, 482);
		pokeChooserPanel.setVisible(true);
		view.getContentPane().add(pokeChooserPanel);
		pokeChooserPanel.addListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//model.sendChoosenPokemon(id);
				PKMessage msg = new PKMessage(Commands.MSG_SELECTED_POKEMON, Integer.parseInt(e.getActionCommand()));
				try {
					connector.sendMessage(msg);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				
				drawBattlePanel();
			}
		});
	}
	
	private void drawBattlePanel() {
		battlePanel = new BattlePanel();
		battlePanel.setBounds(0, 0, 650, 482);
		battlePanel.setVisible(true);
		view.getContentPane().add(battlePanel);
		pokeChooserPanel.setVisible(false);
	}
	
	@SuppressWarnings("static-access")
	public void showErrorPopup() {
		JOptionPane error = new JOptionPane();
		error.setBounds(view.getBounds());
		error.showMessageDialog(view, "Cannot connect to Server", "Warning", JOptionPane.ERROR_MESSAGE);
	}

}