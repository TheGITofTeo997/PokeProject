package it.unibs.pajc.pokeproject;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PKClientWindow {

	private JFrame frame;
	private IpFrame ipWindow;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PKClientWindow window = new PKClientWindow();
					window.frame.setVisible(true);
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
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 600, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);	
		
		JButton btnSinglePlayer = new JButton("Single Player");
		btnSinglePlayer.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSinglePlayer.setBounds(119, 164, 152, 58);
		frame.getContentPane().add(btnSinglePlayer);
		
		JButton btnMultiPlayer = new JButton("Multi Player");
		btnMultiPlayer.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnMultiPlayer.setBounds(325, 164, 152, 58);
		frame.getContentPane().add(btnMultiPlayer);
		
		
		btnMultiPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ipWindow = new IpFrame();
				ipWindow.setVisible(true);
			    frame.setVisible(false);
			}
		});
	}
}
