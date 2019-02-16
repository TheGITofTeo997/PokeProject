package it.unibs.pajc.pokeproject.view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import javax.swing.JButton;
import javax.swing.text.DefaultCaret;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.ImageIcon;

import it.unibs.pajc.pokeproject.controller.*;

public class PKServerWindow {

	private JFrame frmPokeserverV;
	private JTextArea consoleTextArea;
	private JButton btnStart;

	/**
	 * Create the application.
	 */
	public PKServerWindow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize(); // senza questo non so perché ma non carica il background
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPokeserverV = new JFrame("PokeServer v0.3");
		frmPokeserverV.setResizable(false);
		frmPokeserverV.setBounds(100, 100, 460, 500);
		frmPokeserverV.setLocationRelativeTo(null);
		frmPokeserverV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPokeserverV.getContentPane().setLayout(null);
		frmPokeserverV.setVisible(true);
		
		btnStart = new JButton("Start Server ->");
		btnStart.setBounds(163, 36, 121, 60);
		frmPokeserverV.getContentPane().add(btnStart);
		
		JLabel lblConsole = new JLabel("Console");
		lblConsole.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblConsole.setBounds(10, 317, 66, 14);
		frmPokeserverV.getContentPane().add(lblConsole);
		
		JScrollPane scrollPane = new JScrollPane(consoleTextArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 335, 428, 119);
		frmPokeserverV.getContentPane().add(scrollPane);
		
		consoleTextArea = new JTextArea();
		DefaultCaret caret = (DefaultCaret)consoleTextArea.getCaret();
		scrollPane.setViewportView(consoleTextArea);
		consoleTextArea.setEditable(false);
		
		JLabel background = new JLabel();
		background.setIcon(new ImageIcon(new ImageIcon(PKServerWindow.class.getResource("/img/server_back.jpg")).getImage().getScaledInstance(frmPokeserverV.getWidth(), frmPokeserverV.getHeight(), Image.SCALE_DEFAULT))); //back scale
		background.setBounds(0, 0, 454, 471);
		frmPokeserverV.getContentPane().add(background);
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}
	
	public void appendTextToConsole(String text) {
		consoleTextArea.append(text);
	}
	
	public void setController(PKServerController controller) {
		btnStart.addActionListener(controller);
	}
	
	public void disableServerButton() {
		btnStart.setEnabled(false);
	}
}