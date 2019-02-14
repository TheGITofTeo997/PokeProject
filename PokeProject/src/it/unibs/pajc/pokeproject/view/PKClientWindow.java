package it.unibs.pajc.pokeproject.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PKClientWindow {
	private static final String TITLE = "PokeBattle Client v0.1a";
	private JFrame frmPokebattleClientV;
	private IpFrame ipWindow;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PKClientWindow window = new PKClientWindow();
					window.frmPokebattleClientV.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PKClientWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPokebattleClientV = new JFrame();
		frmPokebattleClientV.setTitle(TITLE);
		frmPokebattleClientV.setResizable(false);
		frmPokebattleClientV.setBounds(100, 100, 600, 450);
		frmPokebattleClientV.setLocationRelativeTo(null);
		frmPokebattleClientV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPokebattleClientV.getContentPane().setLayout(null);	
		
		JButton btnSinglePlayer = new JButton("Single Player");
		btnSinglePlayer.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSinglePlayer.setBounds(119, 164, 152, 58);
		frmPokebattleClientV.getContentPane().add(btnSinglePlayer);
		
		JButton btnMultiPlayer = new JButton("Multi Player");
		btnMultiPlayer.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnMultiPlayer.setBounds(325, 164, 152, 58);
		frmPokebattleClientV.getContentPane().add(btnMultiPlayer);
		
		JLabel background = new JLabel();
		background.setBounds(0, 0, 594, 421);
		background.setIcon(new ImageIcon(new ImageIcon(BattleFrame.class.getResource("/img/client_back.jpg")).getImage().getScaledInstance(frmPokebattleClientV.getWidth(), frmPokebattleClientV.getHeight(), Image.SCALE_DEFAULT))); //back scale
		frmPokebattleClientV.getContentPane().add(background);
		
		
		btnMultiPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ipWindow = new IpFrame();
				ipWindow.setVisible(true);
			    frmPokebattleClientV.setVisible(false);
			    
			}
		});
	}
}
