package it.unibs.pajc.pokeproject.controller;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import it.unibs.pajc.pokeproject.util.*;
import it.unibs.pajc.pokeproject.view.*;

public class PKClientController{
	
	//Booleans for listeners control
	private boolean connected;
	
	//Logger
	private Logger logger;
	
	//Controller Components
	private PKLoader loader;
	private PKClientConnector connector;
	private PKBattleEnvironment battleEnvironment;
	private JFrame view;
	private JDialog dialog;
	private int myPokeID;
	
	//View Components
	private MainPanel mainPanel = null;
	private IpPanel ipPanel = null;
	private PokeChooserPanel pokeChooserPanel = null;
	private BattlePanel battlePanel = null;
	
	
	public PKClientController() {
		connected = false;
		logger = new Logger(PKClientStrings.LOGFILE);
		loader = new PKLoader();
		battleEnvironment = new PKBattleEnvironment();
		connector = new PKClientConnector(battleEnvironment, logger);
	}
		
	public void setupClientUtils() {
		loader.loadTypes();
		loader.loadPokemon();
		logger.writeLog(PKClientStrings.UTILS_SUCCESFULLY);
	}
	
	public void drawGUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();
					logger.writeLog(PKClientStrings.GUI_SUCCESFULLY);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void initialize() {
		view = new JFrame(PKClientStrings.FRAME_TITLE);
		view.setResizable(false);
		view.setBounds(100, 100, 600, 450);
		view.setLocationRelativeTo(null);
		view.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				logger.closeLogger();
				if(connected) {
					PKMessage connectionClosed = new PKMessage(Commands.MSG_CONNECTION_CLOSED);
					connector.sendMessage(connectionClosed);
					connector.closeConnection();
				}
				System.exit(0);
			}
		});
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
		
		addBattleEnvironmentListeners();
		logger.writeLog(PKClientStrings.BATTLE_ENVIRONMENT_LISTENERS);
		
		logger.writeLog(PKClientStrings.MAIN_PANEL_SUCCESFULLY);
	}
	
	
	public void drawIpPanel() {
		ipPanel = new IpPanel();
		ipPanel.setBounds(0, 0, 663, 429);
		ipPanel.setVisible(true);
		view.getContentPane().add(ipPanel);
		
		ipPanel.addBackButtonListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainPanel.setVisible(true);
				view.setBounds(100, 100, 600, 450);
				view.setLocationRelativeTo(null);
				ipPanel.setVisible(false);
			}
		});
			
		ipPanel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
					protected Void doInBackground() throws Exception {
						try {
							connector.connectToServer(ipPanel.getIP());
							connected = true;
							logger.writeLog(PKClientStrings.CONNECTED_SUCCESFULLY);
						}
						catch(Exception e)
						{
							e.printStackTrace();
							connected = false;
							showErrorPopup();
							logger.writeLog(PKClientStrings.EXCEPTION_THROWN + e.toString());
						}
						return null;
					}
				};
			
				Window win = SwingUtilities.getWindowAncestor((AbstractButton)e.getSource());
				dialog = new JDialog(win, "Waiting...", ModalityType.APPLICATION_MODAL);
				
				mySwingWorker.addPropertyChangeListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent e) {
						if(e.getPropertyName().equals("state")) {
							if(e.getNewValue() == SwingWorker.StateValue.DONE) {
								if(!connected)
								{
									dialog.dispose();
								}
				            }
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
		
		logger.writeLog(PKClientStrings.IP_PANEL_SUCCESFULLY);
	}
	
	public void drawPokeChooserPanel() {
		pokeChooserPanel = new PokeChooserPanel(loader.getPkDatabase());
		view.getContentPane().add(pokeChooserPanel);
		pokeChooserPanel.setVisible(true);
		
		pokeChooserPanel.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
					protected Void doInBackground() throws Exception {
						myPokeID = Integer.parseInt(e.getActionCommand());
						battleEnvironment.setOurPokemon(loader.getPokemonFromDB(myPokeID));
						PKMessage msg = new PKMessage(Commands.MSG_SELECTED_POKEMON, myPokeID);
						connector.sendMessage(msg);
						logger.writeLog(PKClientStrings.POKEMON_SENT);
						return null;
					}
				};
				
				Window win = SwingUtilities.getWindowAncestor((AbstractButton)e.getSource());
				dialog = new JDialog(win, "Waiting...", ModalityType.APPLICATION_MODAL);
			
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
		
		logger.writeLog(PKClientStrings.CHOOSER_PANEL_SUCCESFULLY);
	}
	
	private void drawBattlePanel() {
		battlePanel = new BattlePanel();
		battlePanel.setBounds(0, 0, 618, 400);
		battlePanel.setVisible(true);
		view.getContentPane().add(battlePanel);
		pokeChooserPanel.setVisible(false);	
		battlePanel.setSprites(battleEnvironment.getOurPokemon().getBackSprite(), battleEnvironment.getOpponentPokemon().getFrontSprite());
		battlePanel.setPokeNames(battleEnvironment.getOurPokemon().getName(), battleEnvironment.getOpponentPokemon().getName());
		battlePanel.setMoveNames(battleEnvironment.getOurPokemon().getMoveSet());
		battlePanel.setPokeHP(battleEnvironment.getOurPokemon().getHP(), battleEnvironment.getOpponentPokemon().getHP());
		battlePanel.addListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
					protected Void doInBackground() throws Exception {
						int moveID = Integer.parseInt(e.getActionCommand());
						PKMessage msg = new PKMessage(Commands.MSG_SELECTED_MOVE, moveID);
						battleEnvironment.setOurMove(moveID);
						connector.sendMessage(msg);
						logger.writeLog(PKClientStrings.MOVE_SENT);
						return null;
					}
				};
				
				Window win = SwingUtilities.getWindowAncestor((AbstractButton)e.getSource());
				dialog = new JDialog(win, "Waiting...", ModalityType.APPLICATION_MODAL);	
				
				mySwingWorker.execute();
					
				JLabel lblGIFLabel = new JLabel();
				lblGIFLabel.setIcon(new ImageIcon(new ImageIcon(PokeChooserPanel.class.getResource("/img/miniwait.gif")).getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT)));	
				lblGIFLabel.setBounds(25, 83, 64, 64);
				JPanel panel = new JPanel();
				panel.add(lblGIFLabel);
				dialog.add(panel);
				dialog.pack();
				dialog.setLocationRelativeTo(win);
				dialog.setVisible(true);
			}
		});		
		logger.writeLog(PKClientStrings.BATTLE_PANEL_SUCCESFULLY);
	}
	
	private void addBattleEnvironmentListeners() {
		battleEnvironment.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if(e.getPropertyName().equalsIgnoreCase("connection_closed"))
				{
					showErrorPopup();
					if(ipPanel.isVisible())
						ipPanel.setVisible(false);
					else if(pokeChooserPanel.isVisible())
						pokeChooserPanel.setVisible(false);
					else if(battlePanel.isVisible())
						battlePanel.setVisible(false);
					connector.closeConnection();
					dialog.dispose();
					mainPanel.setVisible(true);
					view.setBounds(view.getX(), view.getY(), mainPanel.getWidth()+16, mainPanel.getHeight()+39);
					logger.writeLog(PKClientStrings.CONNECTION_CLOSED);
				}
			}
		});
		
		battleEnvironment.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if(e.getPropertyName().equalsIgnoreCase("connection"))
					logger.writeLog(PKClientStrings.CONNECTION_ONLINE);
			}
		});
		
		battleEnvironment.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if(e.getPropertyName().equalsIgnoreCase("player_found"))
				{
					logger.writeLog(PKClientStrings.PLAYER_FOUND);
					dialog.dispose();
					ipPanel.setVisible(false);
					drawPokeChooserPanel();
					view.setBounds(view.getX(), view.getY(), pokeChooserPanel.getWidth()+16, pokeChooserPanel.getHeight()+39);
				}
			}
		});
		
		battleEnvironment.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if(e.getPropertyName().equalsIgnoreCase("opponent"))
				{
					logger.writeLog(PKClientStrings.OPPONENT_POKEMON);
					battleEnvironment.setOpponentPokemon(loader.getPokemonFromDB((Integer)e.getNewValue()));
					dialog.dispose();
					drawBattlePanel();
					view.setBounds(view.getX(), view.getY(), battlePanel.getWidth()+16, battlePanel.getHeight()+39);
					//this is just a temporary resize workaround and it should be NEVER used this way, this is just
					//for esthetic
				}
			}
		});
		
		/*
		battleEnvironment.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if(e.getPropertyName().equalsIgnoreCase("opponent_move"))
				{
					logger.writeLog(PKClientStrings.OPPONENT_MOVE);
					battlePanel.writeTextInMoveBox(battleEnvironment.getOpponentPokemon().getName() + " nemico usa " + e.getNewValue());
				}
			}
		});
		*/
		
		/*
		battleEnvironment.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if(e.getPropertyName().equalsIgnoreCase("our_move"))
				{
					logger.writeLog(PKClientStrings.OPPONENT_MOVE);
					battlePanel.writeTextInMoveBox(battleEnvironment.getOurPokemon().getName() + " usa " + e.getNewValue());
				}
			}
		});
		*/
		
		battleEnvironment.addPropertyListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if(e.getPropertyName().equalsIgnoreCase("ourHP")) {
					logger.writeLog(PKClientStrings.OUR_HP);
					battlePanel.setTrainerHPLevel((Integer)e.getNewValue());
					dialog.dispose();
				}
				else if(e.getPropertyName().equalsIgnoreCase("opponentHP")) {
					logger.writeLog(PKClientStrings.OPPONENT_HP);
					battlePanel.setOpponentHPLevel((Integer)e.getNewValue());
					dialog.dispose();
				}
			}
		});	
		
		battleEnvironment.addPropertyListener(new PropertyChangeListener() {
			@SuppressWarnings("static-access")
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if(e.getPropertyName().equalsIgnoreCase("ourVictory")) {
					logger.writeLog(PKClientStrings.OUR_VICTORY);
					JOptionPane victory = new JOptionPane();
					victory.showMessageDialog(null, "You Won!");
					int reply = JOptionPane.showConfirmDialog(null, "You won, but do you want to have a rematch?", "Rematch?", JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION) 
					{
						logger.writeLog(PKClientStrings.REMATCH_ANSWER_YES);
						PKMessage rematchYes = new PKMessage(Commands.MSG_REMATCH, 1);
						connector.sendMessage(rematchYes);
					}
					else 
					{
						logger.writeLog(PKClientStrings.REMATCH_ANSWER_NO);
						PKMessage rematchNo = new PKMessage(Commands.MSG_REMATCH, 0);
						connector.sendMessage(rematchNo);
						connector.closeConnection();
						battlePanel.setVisible(false);
						view.setBounds(view.getX(), view.getY(), mainPanel.getWidth()+16, mainPanel.getHeight()+39);
						mainPanel.setVisible(true);
					}
				}
				else if(e.getPropertyName().equalsIgnoreCase("opponentVictory")) {
					logger.writeLog(PKClientStrings.OPPONENT_VICTORY);
					JOptionPane victory = new JOptionPane();
					victory.showMessageDialog(null, "You Lost!");
					int reply = JOptionPane.showConfirmDialog(null, "You lost, but do you want to have a rematch?", "Rematch?", JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION) 
					{				
						logger.writeLog(PKClientStrings.REMATCH_ANSWER_YES);
						PKMessage rematchYes = new PKMessage(Commands.MSG_REMATCH, 1);
						connector.sendMessage(rematchYes);
					}
					else 
					{
						logger.writeLog(PKClientStrings.REMATCH_ANSWER_NO);
						PKMessage rematchNo = new PKMessage(Commands.MSG_REMATCH, 0);	
						connector.sendMessage(rematchNo);
						connector.closeConnection();
						battlePanel.setVisible(false);
						view.setBounds(view.getX(), view.getY(), mainPanel.getWidth()+16, mainPanel.getHeight()+39);
						mainPanel.setVisible(true);
					}
				}
			}
		});
		
		battleEnvironment.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if(e.getPropertyName().equalsIgnoreCase("rematch_yes"))
				{
					logger.writeLog(PKClientStrings.REMATCH_YES);
					dialog.dispose();
					battlePanel.setVisible(false);
					drawPokeChooserPanel();
					view.setBounds(view.getX(), view.getY(), pokeChooserPanel.getWidth()+16, pokeChooserPanel.getHeight()+39);
				}
			}	
		});
		
		battleEnvironment.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if(e.getPropertyName().equalsIgnoreCase("rematch_no"))
				{
					logger.writeLog(PKClientStrings.REMATCH_NO);
					dialog.dispose();
					battlePanel.setVisible(false);
					mainPanel.setVisible(true);
				}
			}	
		});
	}
	
	@SuppressWarnings("static-access")
	public void showErrorPopup() {
		JOptionPane error = new JOptionPane();
		error.setBounds(view.getBounds());
		error.showMessageDialog(view, PKClientStrings.CONNECTION_ERROR, "Warning", JOptionPane.ERROR_MESSAGE);
		logger.writeLog(PKClientStrings.ERROR_POPUP_SHOWN);
	}

}