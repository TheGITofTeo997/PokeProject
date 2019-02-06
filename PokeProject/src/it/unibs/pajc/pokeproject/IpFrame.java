package it.unibs.pajc.pokeproject;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.*;

public class IpFrame extends JFrame {
	private static final String INSERT_SERVER_IP = "Insert Server IP:";
	private static final String BTN_CONNECT_TEXT = "Connect ->";
	private JPanel contentPane;
	private JTextField textField;
	private PokeChooserFrame chooserWindow;
	private PKMainClient pkClient = new PKMainClient();

	
	/**
	 * Create the frame.
	 */
	public IpFrame() {
		setType(Type.POPUP);
		setTitle("Insert Server IP");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(false);
		setBounds(100, 100, 411, 200);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblInsertServerIp = new JLabel(INSERT_SERVER_IP);
		lblInsertServerIp.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblInsertServerIp.setBounds(120, 11, 203, 25);
		contentPane.add(lblInsertServerIp);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField.setBounds(67, 47, 256, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnConnect = new JButton(BTN_CONNECT_TEXT);
		btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnConnect.setBounds(137, 90, 131, 42);
		contentPane.add(btnConnect);
		
		
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
