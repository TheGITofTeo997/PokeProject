package it.unibs.pajc.pokeproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Window.Type;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class IpFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
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
		
		JLabel lblInsertServerIp = new JLabel("Insert Server IP:");
		lblInsertServerIp.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblInsertServerIp.setBounds(120, 11, 203, 25);
		contentPane.add(lblInsertServerIp);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField.setBounds(67, 47, 256, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnConnect = new JButton("Connect ->");
		btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnConnect.setBounds(137, 90, 131, 42);
		contentPane.add(btnConnect);
		
		
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pkClient.start();
				pkClient.setIP(textField.getText());
				pkClient.connectToServer();
				
			}
		});
	}
}
