package it.unibs.pajc.pokeproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;

public class WaitingFrame extends JFrame {

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
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("PKMN_RBYGSC.ttf")));
		} catch (IOException|FontFormatException e) {
		     //Handle exception
		}
		setVisible(false);
		setTitle("Waiting...");
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
		
		JLabel lblNewLabel = new JLabel("Waiting for other Player...");
		lblNewLabel.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 16));
		lblNewLabel.setBounds(25, 26, 310, 21);
		contentPane.add(lblNewLabel);
		
		lblGIFLabel.setIcon(new ImageIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/wait.gif")).getImage().getScaledInstance(310, 100, Image.SCALE_DEFAULT))); //gif scale
	}
}
