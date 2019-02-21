package it.unibs.pajc.pokeproject.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PKClientWindow {
	private static final String TITLE = "PokeBattle Client v0.1a";
	private JFrame frmPokebattleClientV;
	
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
		frmPokebattleClientV = new JFrame();
		frmPokebattleClientV.setTitle("PokeBattle Client v0.1b");
		frmPokebattleClientV.setResizable(false);
		frmPokebattleClientV.setBounds(100, 100, 600, 450);
		frmPokebattleClientV.setLocationRelativeTo(null);
		frmPokebattleClientV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPokebattleClientV.getContentPane().setLayout(null);	
		frmPokebattleClientV.setVisible(true);
		
		IpPanel ipPanel = new IpPanel();
		ipPanel.setBounds(0, 0, 594, 421);
		ipPanel.setVisible(false);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 594, 421);
		frmPokebattleClientV.getContentPane().add(mainPanel);
		mainPanel.setLayout(null);
		
		JButton btnSinglePlayer = new JButton("Single Player");
		btnSinglePlayer.setBounds(96, 161, 152, 58);
		mainPanel.add(btnSinglePlayer);
		btnSinglePlayer.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JButton btnMultiPlayer = new JButton("Multi Player");
		btnMultiPlayer.setBounds(358, 159, 152, 58);
		mainPanel.add(btnMultiPlayer);
		btnMultiPlayer.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JLabel background = new JLabel();
		background.setBounds(0, 0, 594, 421);
		mainPanel.add(background);
		background.setIcon(new ImageIcon(new ImageIcon(BattleFrame.class.getResource("/img/client_back.jpg")).getImage().getScaledInstance(frmPokebattleClientV.getWidth(), frmPokebattleClientV.getHeight(), Image.SCALE_DEFAULT)));
		
		frmPokebattleClientV.getContentPane().add(ipPanel);
		
		btnMultiPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ipPanel.setVisible(true);
				mainPanel.setVisible(false);
			    
			}
		});
		
	}
}
