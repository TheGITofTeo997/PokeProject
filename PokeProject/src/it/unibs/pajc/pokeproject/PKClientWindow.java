package it.unibs.pajc.pokeproject;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PKClientWindow {

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
		frmPokebattleClientV.setTitle("PokeBattle Client v0.1");
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
		
		
		btnMultiPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ipWindow = new IpFrame();
				ipWindow.setVisible(true);
			    frmPokebattleClientV.setVisible(false);
			}
		});
	}
}
