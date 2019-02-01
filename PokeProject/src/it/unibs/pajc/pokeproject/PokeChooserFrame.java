package it.unibs.pajc.pokeproject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;

public class PokeChooserFrame extends JFrame {

	private static final int RES = 96;
	private JPanel contentPane;	
	
	/**
	 * Create the frame.
	 */
	public PokeChooserFrame() {
		setTitle("Choose a Pokemon!");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(false);
		setBounds(100, 100, 650, 460);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JLabel bulbasaur = new JLabel("");
		bulbasaur.setIcon(new ImageIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/bulbasaur.gif")).getImage().getScaledInstance(RES, RES, Image.SCALE_DEFAULT))); //gif scale
		bulbasaur.setBounds(57, 59, 96, 96);
		contentPane.add(bulbasaur);
		
		JRadioButton rdbtnChooseMe = new JRadioButton("Choose Me!");
		rdbtnChooseMe.setBounds(57, 172, 109, 23);
		contentPane.add(rdbtnChooseMe);
		
		
		JLabel charmander = new JLabel("");
		charmander.setIcon(new ImageIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/Charmander.gif")).getImage().getScaledInstance(RES, RES, Image.SCALE_DEFAULT))); //gif scale
		charmander.setBounds(267, 59, 96, 96);
		contentPane.add(charmander);
		
		JLabel squirtle = new JLabel("");
		squirtle.setIcon(new ImageIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/Squirtle.gif")).getImage().getScaledInstance(RES, RES, Image.SCALE_DEFAULT))); //gif scale
		squirtle.setBounds(493, 59, 96, 96);
		contentPane.add(squirtle);
		
	
		JLabel chikorita = new JLabel("");
		chikorita.setIcon(new ImageIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/Chikorita.gif")).getImage().getScaledInstance(RES, RES, Image.SCALE_DEFAULT))); //gif scale
		chikorita.setBounds(57, 249, 96, 96);
		contentPane.add(chikorita);
		
		JLabel cyndaquil = new JLabel("");
		cyndaquil.setIcon(new ImageIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/Cyndaquil.gif")).getImage().getScaledInstance(RES, RES, Image.SCALE_DEFAULT))); //gif scale
		cyndaquil.setBounds(267, 249, 96, 96);
		contentPane.add(cyndaquil);
		
		JLabel totodile = new JLabel("");
		totodile.setIcon(new ImageIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/Totodile.gif")).getImage().getScaledInstance(RES, RES, Image.SCALE_DEFAULT))); //gif scale
		totodile.setBounds(493, 249, 96, 96);
		contentPane.add(totodile);
		
		JLabel background = new JLabel("");
		background.setIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/chooser_back.jpg")));
		background.setBounds(0, 0, 644, 431);
		contentPane.add(background);
		
		
	}
}
