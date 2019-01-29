package it.unibs.pajc.pokeproject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
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

public class PokeChooserFrame extends JFrame {

	private JPanel contentPane;
	

	/**
	 * Create the frame.
	 */
	public PokeChooserFrame() {
		setTitle("Choose a Pokemon!");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(false);
		setBounds(100, 100, 656, 463);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/bulbasaur-3.gif")));
		lblNewLabel.setBounds(41, 31, 147, 164);
		contentPane.add(lblNewLabel);
		
		
		
	}
	
}
