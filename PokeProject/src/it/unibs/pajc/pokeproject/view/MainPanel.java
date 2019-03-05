package it.unibs.pajc.pokeproject.view;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainPanel extends JPanel {

	private static final String PKM_FONT = "PKMN_RBYGSC.ttf";
	private static final long serialVersionUID = -291833896767000359L;
	private JButton btnSinglePlayer;
	private JButton btnMultiPlayer;
	

	/**
	 * Create the panel.
	 */
	public MainPanel() {
		try {
		     GraphicsEnvironment ge = 
		         GraphicsEnvironment.getLocalGraphicsEnvironment();
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(PKM_FONT)));
		} catch (IOException|FontFormatException e) {
		     e.printStackTrace();
		}
		setLayout(null);
		
		btnSinglePlayer = new JButton("Single Player");
		btnSinglePlayer.setBounds(96, 160, 152, 58);
		add(btnSinglePlayer);
		btnSinglePlayer.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		btnMultiPlayer = new JButton("Multi Player");
		btnMultiPlayer.setBounds(358, 160, 152, 58);
		add(btnMultiPlayer);
		btnMultiPlayer.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JLabel background = new JLabel();
		background.setBounds(0, 0, 594, 421);
		add(background);
		background.setIcon(new ImageIcon(new ImageIcon(MainPanel.class.getResource("/img/client_back.jpg")).getImage().getScaledInstance(background.getWidth(), background.getHeight(), Image.SCALE_DEFAULT)));	
	}
	
	public void addSinglePlayerListener(ActionListener l) {
		btnSinglePlayer.addActionListener(l);
	}
	
	public void addMultiPlayerListener(ActionListener l) {
		btnMultiPlayer.addActionListener(l);
	}
}
