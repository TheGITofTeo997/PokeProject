package it.unibs.pajc.pokeproject.view;

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class IpPanel extends JPanel {

	private static final long serialVersionUID = -1844742030413196713L;
	private static final String INSERT_SERVER_IP = "Insert Server IP:";
	private static final String BTN_CONNECT_TEXT = "Connect";
	private static final String BTN_BACK = "Back";
	private static final String PKM_FONT = "PKMN_RBYGSC.ttf";
	private JTextField addressField;
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
		
		addressField = new JTextField();
		addressField.setBounds(175, 175, 300, 50);
		add(addressField);
		
		JLabel lblNewLabel = new JLabel(INSERT_SERVER_IP);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 20));
		lblNewLabel.setBounds(175, 25, 300, 50);
		add(lblNewLabel);
		
		JButton btnConnectButton = new JButton(BTN_CONNECT_TEXT);
		btnConnectButton.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 20));
		btnConnectButton.setBounds(100, 325, 200, 50);
		add(btnConnectButton);
		
		JButton btnBackButton = new JButton(BTN_BACK);
		btnBackButton.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 20));
		btnBackButton.setBounds(350, 325, 200, 50);
		add(btnBackButton);
		
		btnConnectButton.addActionListener(new ActionListener() { //temporary action listener in order to test, this will need to be changed/moved
			public void actionPerformed(ActionEvent arg0) {
				checkIPCorrectness();			
			}
		});
	}
	

	private boolean checkIPCorrectness() {    //still need to make this less verbose
		String address = addressField.getText();
		StringTokenizer st = new StringTokenizer(address, ".", false);
		int nTokens = st.countTokens();
		if(st.hasMoreTokens()) { // we do this to avoid the null case
			while(st.hasMoreTokens()) {	
				String token = st.nextToken().toString();	
				if(token.length() > 3 || token.matches(("[a-zA-Z]+")) ) {
					System.out.println("false");
					return false;
				}			
				if(nTokens<4 || nTokens>4) {
					System.out.println("false");
					return false;
				}
			}
		}
		else {
			System.out.println("false");
			return false;	
		}
		System.out.println("true");
		return true;
	}
}
