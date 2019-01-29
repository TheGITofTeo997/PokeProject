package it.unibs.pajc.pokeproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class PokeChooserFrame extends JFrame {

	private JPanel contentPane;


	/**
	 * Create the frame.
	 */
	public PokeChooserFrame() {
		setTitle("Choose a Pokemon");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 701, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("");
		label.setBounds(10, 11, 153, 160);
		contentPane.add(label);
		
		
	}
}
