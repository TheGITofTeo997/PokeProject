package it.unibs.pajc.pokeproject.view;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


// Do we need to migrate this frame as a panel? In that case:


/*
 * 
 * 			PLEASE MIGRATE TO PANEL AS SOON AS POSSIBLE /!\
 * 		If we don't migrate to panel we cannot proceed fixing MVC,
 * 			this class will be ghosting around while we'll
 * 		  be migrating it and after that, it will be deleted.
 * 
 */

public class WaitingFrame extends JFrame {
	private static final long serialVersionUID = 3669221117391479061L;
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
		
		JLabel lblGIFLabel = new JLabel();
		lblGIFLabel.setIcon(new ImageIcon(WaitingFrame.class.getResource("/img/wait.gif")));
		lblGIFLabel.setBounds(25, 83, 310, 100);
		contentPane.add(lblGIFLabel);
		
		JLabel lblWaitLabel = new JLabel(WAITING_OTHER_PLAYER);
		lblWaitLabel.setEnabled(false);
		lblWaitLabel.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 16));
		lblWaitLabel.setBounds(25, 26, 310, 21);
		contentPane.add(lblWaitLabel);
		
		lblGIFLabel.setIcon(new ImageIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/wait.gif")).getImage().getScaledInstance(310, 100, Image.SCALE_DEFAULT))); //gif scale
	}
}
