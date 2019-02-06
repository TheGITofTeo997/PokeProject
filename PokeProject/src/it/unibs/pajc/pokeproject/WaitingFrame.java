package it.unibs.pajc.pokeproject;

import java.awt.BorderLayout;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class WaitingFrame extends JFrame {

	private static final String PKM_FONT = "PKMN_RBYGSC.ttf";
	private static final String TITLE = "Waiting...";
	private static final String WAITING_OTHER_PLAYER = "Waiting for other Player...";
	
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WaitingFrame frame = new WaitingFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WaitingFrame() {
		setEnabled(false);
		setAlwaysOnTop(true);
		
		try {
		     GraphicsEnvironment ge = 
		         GraphicsEnvironment.getLocalGraphicsEnvironment();
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(PKM_FONT)));
		} catch (IOException|FontFormatException e) {
		     //Handle exception
		}
		setVisible(false);
		setTitle(TITLE);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 361, 233);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setLocationRelativeTo(null);
		contentPane.setLayout(null);
		
		JLabel lblGIFLabel = new JLabel("");
		lblGIFLabel.setIcon(new ImageIcon(WaitingFrame.class.getResource("/img/wait.gif")));
		lblGIFLabel.setBounds(25, 83, 310, 100);
		contentPane.add(lblGIFLabel);
		
		JLabel lblNewLabel = new JLabel(WAITING_OTHER_PLAYER);
		lblNewLabel.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 16));
		lblNewLabel.setBounds(25, 26, 310, 21);
		contentPane.add(lblNewLabel);
		
		lblGIFLabel.setIcon(new ImageIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/wait.gif")).getImage().getScaledInstance(310, 100, Image.SCALE_DEFAULT))); //gif scale
	}
}
