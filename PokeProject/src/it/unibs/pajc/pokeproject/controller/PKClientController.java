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
	
	private boolean threw; // a boolean for connection errors
	
	//Controller Components
	private PKLoader loader;
	private PKClientConnector connector;
	private PKBattleEnvironment battleEnvironment;
	private JFrame view;
	private int myPokeID;
	
	//View Components
	private static final String TITLE = "PokeBattle Client v0.5";
	private MainPanel mainPanel = null;
	private IpPanel ipPanel = null;
	private PokeChooserPanel pokeChooserPanel = null;
	private BattlePanel battlePanel = null;
	
	
	public PKClientController() {
		threw = false;
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
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
					protected Void doInBackground() throws Exception {
						try {
							connector.connectToServer(ipPanel.getIP());
						}
						catch(Exception e)
						{
							e.printStackTrace();
							threw = true;
							showErrorPopup();
						}
						return null;
					}
				};

				Window win = SwingUtilities.getWindowAncestor((AbstractButton)e.getSource());
				JDialog dialog = new JDialog(win, "Dialog", ModalityType.APPLICATION_MODAL);
				dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
				
				mySwingWorker.addPropertyChangeListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent e) {
						if(e.getPropertyName().equals("state")) {
							if(e.getNewValue() == SwingWorker.StateValue.DONE) {
								if(threw == true)
								{
									dialog.dispose();
									threw = false;
									battleEnvironment.removeListener(); // to avoid double creation of pokechooserpanel
								}
				            }
						}
					}
				});
				
				battleEnvironment.addPropertyListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent e) {
						if(e.getPropertyName().equalsIgnoreCase("connection"))
						{
							dialog.dispose();
							ipPanel.setVisible(false);
							drawPokeChooserPanel();
							view.setBounds(view.getX(), view.getY(), pokeChooserPanel.getWidth(), pokeChooserPanel.getHeight());
						}
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
						myPokeID = Integer.parseInt(e.getActionCommand());
						PKMessage msg = new PKMessage(Commands.MSG_SELECTED_POKEMON, myPokeID);
						connector.sendMessage(msg);
						return null;
					}
				};
				
				Window win = SwingUtilities.getWindowAncestor((AbstractButton)e.getSource());
				JDialog dialog = new JDialog(win, "Waiting...", ModalityType.APPLICATION_MODAL);
				dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
				battleEnvironment.addPropertyListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent e) {
						if(e.getPropertyName().equalsIgnoreCase("wait"))
						{
							dialog.dispose();
							drawBattlePanel();
						}
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
		battlePanel.setBackSprite(loader.getPokemonFromDB(myPokeID).getBackSprite());
	}
	
	
	
	@SuppressWarnings("static-access")
	public void showErrorPopup() {
		JOptionPane error = new JOptionPane();
		error.setBounds(view.getBounds());
		error.showMessageDialog(view, PKClientStrings.CONNECTION_ERROR, "Warning", JOptionPane.ERROR_MESSAGE);
	}

}