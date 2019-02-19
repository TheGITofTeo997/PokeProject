package it.unibs.pajc.pokeproject.view;

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class IpPanel extends JPanel {

	private static final long serialVersionUID = -1844742030413196713L;
	private static final String INSERT_SERVER_IP = "Insert Server IP:";
	private static final String BTN_CONNECT_TEXT = "Connect";
	private static final String BTN_BACK = "Back";
	private static final String PKM_FONT = "PKMN_RBYGSC.ttf";
	private JTextField textField;
	/**
	 * Create the panel.
	 */
	public IpPanel() {
		try {
		     GraphicsEnvironment ge = 
		         GraphicsEnvironment.getLocalGraphicsEnvironment();
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(PKM_FONT)));
		} catch (IOException|FontFormatException e) {
		     e.printStackTrace();
		}
		
		setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(175, 175, 300, 50);
		add(textField);
		
		JLabel lblNewLabel = new JLabel(INSERT_SERVER_IP);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 20));
		lblNewLabel.setBounds(175, 25, 300, 50);
		add(lblNewLabel);
		
		JButton btnNewButton = new JButton(BTN_CONNECT_TEXT);
		btnNewButton.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 20));
		btnNewButton.setBounds(100, 325, 200, 50);
		add(btnNewButton);
		
		JButton button = new JButton(BTN_BACK);
		button.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 20));
		button.setBounds(350, 325, 200, 50);
		add(button);
	}
}
