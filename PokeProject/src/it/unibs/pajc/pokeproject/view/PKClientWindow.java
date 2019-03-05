package it.unibs.pajc.pokeproject.view;

import javax.swing.*;

import it.unibs.pajc.pokeproject.controller.PKClientController;
import it.unibs.pajc.pokeproject.model.Pokemon;

import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class PKClientWindow extends JFrame {
	
	private static final long serialVersionUID = -7852194625838135417L;
	private static final String TITLE = "PokeBattle Client v0.4";
	private MainPanel mainPanel = null;
	private IpPanel ipPanel = null;
	private PokeChooserPanel pokeChooserPanel = null;
	private BattlePanel battlePanel = null;
	private PKClientController controller = null;
	
	
	/**
	 * Create the application.
	 */
	public PKClientWindow() {
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
		setTitle(TITLE);
		setResizable(false);
		setBounds(100, 100, 600, 450);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);	
		setVisible(true);
		drawMainPanel();
	}
	
	public void drawIpPanel() {
		ipPanel = new IpPanel();
		ipPanel.setBounds(0, 0, 663, 429);
		ipPanel.setVisible(true);
		getContentPane().add(ipPanel);
		ipPanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(controller.tryConnection(ipPanel.getIP())) {		
					drawPokeChooserPanel();
					setBounds(getX(), getY(), pokeChooserPanel.getWidth(), pokeChooserPanel.getHeight());
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
		getContentPane().add(mainPanel);
		
		mainPanel.addSinglePlayerListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawPokeChooserPanel();
				setBounds(getX(), getY(), pokeChooserPanel.getWidth(), pokeChooserPanel.getHeight());
				mainPanel.setVisible(false); 
			}
		});
		
		mainPanel.addMultiPlayerListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawIpPanel();
				setBounds(getX(), getY(), ipPanel.getWidth(), ipPanel.getHeight());
				mainPanel.setVisible(false); 
			}
		});
	}
	
	public void drawPokeChooserPanel() {
		pokeChooserPanel = new PokeChooserPanel();
		pokeChooserPanel.setBounds(0, 0, 650, 482);
		pokeChooserPanel.setVisible(true);
		getContentPane().add(pokeChooserPanel);
		pokeChooserPanel.addListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int id = Integer.parseInt(e.getActionCommand());
				//model.sendChoosenPokemon(id);
				
				drawBattlePanel();
			}
		});
	}
	
	private void drawBattlePanel() {
		battlePanel = new BattlePanel();
		battlePanel.setBounds(0, 0, 650, 482);
		battlePanel.setVisible(true);
		getContentPane().add(battlePanel);
		pokeChooserPanel.setVisible(false);
	}
	
	public void setController(PKClientController controller) {
		this.controller = controller;
	}
	
	@SuppressWarnings("static-access")
	public void showErrorPopup() {
		JOptionPane error = new JOptionPane();
		error.setBounds(getBounds());
		error.showMessageDialog(this, "Cannot connect to Server", "Warning", JOptionPane.ERROR_MESSAGE);
	}
}
