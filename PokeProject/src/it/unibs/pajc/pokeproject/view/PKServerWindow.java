package it.unibs.pajc.pokeproject.view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;

import javax.swing.JButton;
import javax.swing.text.DefaultCaret;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.ImageIcon;

import it.unibs.pajc.pokeproject.controller.*;
import it.unibs.pajc.pokeproject.util.PKServerStrings;

public class PKServerWindow implements KeyListener{

	private JFrame frmPokeserverV;
	private JTextArea consoleTextArea;
	private JButton btnStart;

	/**
	 * Create the application.
	 */
	public PKServerWindow(PKServerController controller) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize(); // senza questo non so perché ma non carica il background
					setController(controller);
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
		frmPokeserverV = new JFrame(PKServerStrings.FRAME_TITLE);
		frmPokeserverV.setResizable(false);
		frmPokeserverV.setBounds(100, 100, 460, 500);
		frmPokeserverV.setLocationRelativeTo(null);
		frmPokeserverV.getContentPane().setLayout(null);
		frmPokeserverV.setVisible(true);
		frmPokeserverV.setFocusable(true);
		frmPokeserverV.addKeyListener(this);
		
		btnStart = new JButton(PKServerStrings.BTN_START_TXT);
		btnStart.setBounds(163, 36, 121, 60);
		frmPokeserverV.getContentPane().add(btnStart);
		
		JLabel lblConsole = new JLabel(PKServerStrings.CONSOLE_LABEL);
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
	
	public String getConsoleText() {
		return consoleTextArea.getText();
	}
	
	public void addWindowAdapter(WindowAdapter windowAdapter) {
		// This need to be looked carefully
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmPokeserverV.addWindowListener(windowAdapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void setController(PKServerController controller) {
		btnStart.addActionListener(controller);
	}
	
	public void disableServerButton() {
		btnStart.setEnabled(false);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getExtendedKeyCode() == KeyEvent.VK_ENTER) btnStart.doClick();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}