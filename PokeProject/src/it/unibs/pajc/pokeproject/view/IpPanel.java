package it.unibs.pajc.pokeproject.view;

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

public class IpPanel extends JPanel {

	private static final long serialVersionUID = -1844742030413196713L;
	private static final String INSERT_SERVER_IP = "Insert Server IP:";
	private static final String BTN_CONNECT_TEXT = "Connect";
	private static final String BTN_BACK = "Back";
	private static final String PKM_FONT = "PKMN_RBYGSC.ttf";
	private JTextField addressField;
	private ArrayList<ActionListener> listenerList = new ArrayList<>();
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
		addressField.setBounds(189, 176, 300, 50);
		add(addressField);
		
		JLabel lblTitleLabel = new JLabel(INSERT_SERVER_IP);
		lblTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitleLabel.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 20));
		lblTitleLabel.setBounds(189, 23, 300, 50);
		add(lblTitleLabel);
		
		JButton btnConnectButton = new JButton(BTN_CONNECT_TEXT);
		btnConnectButton.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 20));
		btnConnectButton.setBounds(108, 325, 200, 50);
		add(btnConnectButton);
		
		JButton btnBackButton = new JButton(BTN_BACK);
		btnBackButton.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 20));
		btnBackButton.setBounds(370, 325, 200, 50);
		add(btnBackButton);
		
		JLabel background = new JLabel();
		background.setBounds(0, 0, 663, 429);
		background.setIcon(new ImageIcon(new ImageIcon(IpPanel.class.getResource("/img/ip_back.jpg")).getImage().getScaledInstance(background.getWidth(), background.getHeight(), Image.SCALE_DEFAULT))); //back scale
		add(background);
		
		btnConnectButton.addActionListener(new ActionListener() { //temporary action listener in order to test, this will need to be changed/moved
			public void actionPerformed(ActionEvent e) {
				if(checkIPCorrectness()) {
					fireActionPerformed(e);
				}
				else {
					showErrorPopup();
				}
					
			}
		});
		
		btnBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
	}
	

	private boolean checkIPCorrectness() {    //still need to make this less verbose
		String address = addressField.getText();
		StringTokenizer st = new StringTokenizer(address, ".", false);
		int nTokens = st.countTokens();
		if(st.hasMoreTokens()) { // we do this to avoid the null case
			if(nTokens != 4) {
				System.out.println("false");
				return false;
			}
			while(st.hasMoreTokens()) {	
				String token = st.nextToken().toString();			
		
				if(token.length() > 3 || token.matches(("[a-zA-Z]+"))) {
					System.out.println("false");
					return false;
				}
				
				int tokenValue = Integer.parseInt(token);
				
				if(tokenValue < 0 || tokenValue > 255) {
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
	
	private void showErrorPopup() {
		JOptionPane error = new JOptionPane();
		error.setBounds(getBounds());
		error.showMessageDialog(this, "Indirizzo IP inserito non valido", "ATTENZIONE", JOptionPane.ERROR_MESSAGE);
	}
	
	public void addActionListener(ActionListener l) {
		listenerList.add(l);
	}
	
	protected void fireActionPerformed(ActionEvent e) {
		for(ActionListener l : listenerList)
			l.actionPerformed(e);
	}
}
