package it.unibs.pajc.pokeproject;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class IpFrame extends JFrame {
	private static final long serialVersionUID = -5554319631552427590L;
	private static final String INSERT_SERVER_IP = "Insert Server IP:";
	private static final String BTN_CONNECT_TEXT = "Connect ->";
	private static final String PKM_FONT = "PKMN_RBYGSC.ttf";
	private JPanel contentPane;
	private JTextField textField;
	private PokeChooserFrame chooserWindow;
	private PKMainClient pkClient = new PKMainClient();

	
	/**
	 * Create the frame.
	 */
	public IpFrame() {
		try {
		     GraphicsEnvironment ge = 
		         GraphicsEnvironment.getLocalGraphicsEnvironment();
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(PKM_FONT)));
		} catch (IOException|FontFormatException e) {
		     //Handle exception
		}
		
		
		setType(Type.POPUP);
		setTitle("Insert Server IP");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(false);
		setBounds(100, 100, 400, 200);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblInsertServerIp = new JLabel(INSERT_SERVER_IP);
		lblInsertServerIp.setHorizontalAlignment(SwingConstants.CENTER);
		lblInsertServerIp.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 20));
		lblInsertServerIp.setBounds(40, 11, 307, 25);
		contentPane.add(lblInsertServerIp);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField.setBounds(67, 47, 256, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnConnect = new JButton(BTN_CONNECT_TEXT);
		btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnConnect.setBounds(126, 89, 131, 42);
		contentPane.add(btnConnect);
		
		JLabel background = new JLabel();
		background.setBounds(0, 0, 394, 171);
		background.setIcon(new ImageIcon(new ImageIcon(BattleFrame.class.getResource("/img/ip_back.jpg")).getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT))); //back scale
		contentPane.add(background);
		
		
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pkClient.start();
				pkClient.setIP(textField.getText());
				if(pkClient.connectToServer()) {
					chooserWindow = new PokeChooserFrame(pkClient);
					chooserWindow.setVisible(true);
					setVisible(false);
				}
			}
		});
		
	}
}
