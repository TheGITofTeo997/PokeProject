package it.unibs.pajc.pokeproject;

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

public class PKServerWindow extends Thread{

	private JFrame frmPokeserverV;
	private PKMainServer pkServer = new PKMainServer();
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
		frmPokeserverV.setTitle("PokeServer v0.1c");
		frmPokeserverV.setBounds(100, 100, 460, 500);
		frmPokeserverV.setLocationRelativeTo(null);
		frmPokeserverV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPokeserverV.getContentPane().setLayout(null);
		
		JLabel lblConsole = new JLabel("Console");
		lblConsole.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblConsole.setBounds(10, 317, 66, 14);
		frmPokeserverV.getContentPane().add(lblConsole);
		
		JButton btnStart = new JButton("Start Server ->");
		btnStart.setBounds(163, 36, 121, 60);
		frmPokeserverV.getContentPane().add(btnStart);
		
		JScrollPane scrollPane = new JScrollPane(consoleTextArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 335, 428, 119);
		frmPokeserverV.getContentPane().add(scrollPane);
		
		consoleTextArea = new JTextArea();
		DefaultCaret caret = (DefaultCaret)consoleTextArea.getCaret();
		scrollPane.setViewportView(consoleTextArea);
		consoleTextArea.setEditable(false);
		
		JLabel background = new JLabel();
		background.setIcon(new ImageIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/server_back.jpg")).getImage().getScaledInstance(frmPokeserverV.getWidth(), frmPokeserverV.getHeight(), Image.SCALE_DEFAULT))); //back scale
		background.setBounds(0, 0, 454, 471);
		frmPokeserverV.getContentPane().add(background);
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pkServer.start();
				btnStart.setEnabled(false);
			}
		});
	}
	
	public static void appendTextToConsole(String text) {
		consoleTextArea.append(text);
	}
}
