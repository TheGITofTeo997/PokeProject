package it.unibs.pajc.pokeproject.controller;

import java.awt.EventQueue;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import it.unibs.pajc.pokeproject.model.*;
import it.unibs.pajc.pokeproject.util.PKMessage;
import it.unibs.pajc.pokeproject.view.*;
import it.unibs.pajc.pokeproject.util.Commands;
import it.unibs.pajc.pokeproject.util.PKClientStrings;

public class PKClientController{
	
	//Controller Components
	private PKLoader loader;
	private PKClientConnector connector;
	private PKBattleEnvironment battleEnvironment;
	private JFrame view;
	
	//View Components
	private static final String TITLE = "PokeBattle Client v0.5";
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
				}
				else {
					showErrorPopup();
				}
			}
		});
	}
	
	
	public void drawPokeChooserPanel() {
		pokeChooserPanel = new PokeChooserPanel(loader.getPkDatabase());
		view.getContentPane().add(pokeChooserPanel);
		pokeChooserPanel.setVisible(true);
		
		/*
		 * @author Patrick
		 * This is the part where we create the popup waiting window.
		 * At the moment this is not done by using the WaitingFrame because, despite
		 * various attempts i could not fit it in. 
		 */
		pokeChooserPanel.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
					protected Void doInBackground() throws Exception {
						PKMessage msg = new PKMessage(Commands.MSG_SELECTED_POKEMON, Integer.parseInt(e.getActionCommand()));
						connector.sendMessage(msg);
						return null;
					}
				};
				
				Window win = SwingUtilities.getWindowAncestor((AbstractButton)e.getSource());
				final JDialog dialog = new JDialog(win, "Dialog", ModalityType.APPLICATION_MODAL);
				dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
				battleEnvironment.addPropertyListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent e) {
						dialog.dispose();
						drawBattlePanel();
					}
					
				});
			
				mySwingWorker.execute();

				JLabel lblGIFLabel = new JLabel();
				lblGIFLabel.setIcon(new ImageIcon(PKClientController.class.getResource("/img/wait.gif")));
				lblGIFLabel.setBounds(25, 83, 310, 100);
				JPanel panel = new JPanel();
				panel.add(lblGIFLabel);
				dialog.add(panel);
				dialog.pack();
				dialog.setLocationRelativeTo(win);
				dialog.setVisible(true);
			}
		});
	}
	
	private void drawBattlePanel() {
		battlePanel = new BattlePanel();
		battlePanel.setBounds(0, 0, 618, 400);
		battlePanel.setVisible(true);
		view.getContentPane().add(battlePanel);
		pokeChooserPanel.setVisible(false);
	}
	
	@SuppressWarnings("static-access")
	public void showErrorPopup() {
		JOptionPane error = new JOptionPane();
		error.setBounds(view.getBounds());
		error.showMessageDialog(view, PKClientStrings.CONNECTION_ERROR, "Warning", JOptionPane.ERROR_MESSAGE);
	}

}