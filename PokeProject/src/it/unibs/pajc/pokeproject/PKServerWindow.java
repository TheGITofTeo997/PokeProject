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
import javax.swing.text.DefaultCaret;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import java.awt.ScrollPane;
import javax.swing.ScrollPaneConstants;

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
		//frmPokeserverV.getContentPane().add(consoleTextArea);
				
		JScrollPane scrollPane = new JScrollPane(consoleTextArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 335, 428, 119);
		frmPokeserverV.getContentPane().add(scrollPane);
		
		consoleTextArea = new JTextArea();
		DefaultCaret caret = (DefaultCaret)consoleTextArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollPane.setViewportView(consoleTextArea);
		consoleTextArea.setEditable(false);
	}
	
	public static void appendTextToConsole(String text) {
		consoleTextArea.append(text);
	}
}
