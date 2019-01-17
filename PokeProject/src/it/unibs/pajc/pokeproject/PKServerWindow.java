package it.unibs.pajc.pokeproject;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PKServerWindow extends Thread{

	private JFrame frmPokeserverV;
	private Thread serverRunner = new Thread(() -> {PKMainServer.serverStart();});
	private static JTextArea consoleTextArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PKServerWindow window = new PKServerWindow();
					window.frmPokeserverV.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PKServerWindow() {
		initialize();	
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPokeserverV = new JFrame();
		frmPokeserverV.setResizable(false);
		frmPokeserverV.setTitle("PokeServer v0.1");
		frmPokeserverV.setBounds(100, 100, 460, 500);
		frmPokeserverV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPokeserverV.getContentPane().setLayout(null);
		
		consoleTextArea = new JTextArea();
		consoleTextArea.setEditable(false);
		consoleTextArea.setBounds(10, 334, 424, 116);
		frmPokeserverV.getContentPane().add(consoleTextArea);
		
		JLabel lblConsole = new JLabel("Console");
		lblConsole.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblConsole.setBounds(10, 317, 66, 14);
		frmPokeserverV.getContentPane().add(lblConsole);
		
		JButton btnStart = new JButton("Start Server ->");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				serverRunner.start();
			}
		});
		btnStart.setBounds(162, 36, 121, 60);
		frmPokeserverV.getContentPane().add(btnStart);
		
	}
	
	public static void appendTextToConsole(String text) {
		consoleTextArea.append(text);
	}
}
