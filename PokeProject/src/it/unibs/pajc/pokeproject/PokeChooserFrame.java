package it.unibs.pajc.pokeproject;

import java.awt.Image;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;

public class PokeChooserFrame extends JFrame implements ActionListener{
	private static final long serialVersionUID = -6147325708075015028L;
	private static final String CHOOSE_ME = "Choose Me!";
	private static final String BTN_STARTBATTLE_TEXT = "Start Battle ->";
	private static final int RES = 96;
	private JPanel contentPane;	
	private PKMainClient pkClient;
	private JButton btnStartButton;
	/**
	 * Create the frame.
	 */
	public PokeChooserFrame(PKMainClient pkClient) {
		setTitle("Choose a Pokemon!");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(false);
		setBounds(100, 100, 650, 482);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.pkClient = pkClient;
		
		
		JRadioButton rdbtnChooseMe1 = new JRadioButton("Bulbasaur");
		rdbtnChooseMe1.setOpaque(false);
		rdbtnChooseMe1.setBorderPainted(false);
		rdbtnChooseMe1.setContentAreaFilled(false);
		rdbtnChooseMe1.setBounds(57, 172, 109, 23);
		rdbtnChooseMe1.setActionCommand("Bulbasaur");
		contentPane.add(rdbtnChooseMe1);
		
		JRadioButton rdbtnChooseMe2 = new JRadioButton("Charmander");
		rdbtnChooseMe2.setOpaque(false);
		rdbtnChooseMe2.setContentAreaFilled(false);
		rdbtnChooseMe2.setBorderPainted(false);
		rdbtnChooseMe2.setBounds(278, 172, 109, 23);
		rdbtnChooseMe2.setActionCommand("Charmander");
		contentPane.add(rdbtnChooseMe2);
		
		JRadioButton rdbtnChooseMe3 = new JRadioButton("Squirtle");
		rdbtnChooseMe3.setOpaque(false);
		rdbtnChooseMe3.setContentAreaFilled(false);
		rdbtnChooseMe3.setBorderPainted(false);
		rdbtnChooseMe3.setBounds(493, 172, 109, 23);
		rdbtnChooseMe3.setActionCommand("Squirtle");
		contentPane.add(rdbtnChooseMe3);
		
		JRadioButton rdbtnChooseMe4 = new JRadioButton("Chikorita");
		rdbtnChooseMe4.setOpaque(false);
		rdbtnChooseMe4.setContentAreaFilled(false);
		rdbtnChooseMe4.setBorderPainted(false);
		rdbtnChooseMe4.setBounds(57, 354, 109, 23);
		rdbtnChooseMe4.setActionCommand("Chikorita");
		contentPane.add(rdbtnChooseMe4);
		
		JRadioButton rdbtnChooseMe5 = new JRadioButton("Cyndaquil");
		rdbtnChooseMe5.setOpaque(false);
		rdbtnChooseMe5.setContentAreaFilled(false);
		rdbtnChooseMe5.setBorderPainted(false);
		rdbtnChooseMe5.setBounds(278, 353, 109, 23);
		rdbtnChooseMe5.setActionCommand("Cyndaquil");
		contentPane.add(rdbtnChooseMe5);
		
		JRadioButton rdbtnChooseMe6 = new JRadioButton("Totodile");
		rdbtnChooseMe6.setOpaque(false);
		rdbtnChooseMe6.setContentAreaFilled(false);
		rdbtnChooseMe6.setBorderPainted(false);
		rdbtnChooseMe6.setBounds(493, 352, 109, 23);
		rdbtnChooseMe6.setActionCommand("Totodile");
		contentPane.add(rdbtnChooseMe6);
		
		
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnChooseMe1);
		group.add(rdbtnChooseMe2);
		group.add(rdbtnChooseMe3);
		group.add(rdbtnChooseMe4);
		group.add(rdbtnChooseMe5);
		group.add(rdbtnChooseMe6);
		
		rdbtnChooseMe1.addActionListener(this);
		rdbtnChooseMe2.addActionListener(this);
		rdbtnChooseMe3.addActionListener(this);
		rdbtnChooseMe4.addActionListener(this);
		rdbtnChooseMe5.addActionListener(this);
		rdbtnChooseMe6.addActionListener(this);
	
		
		JLabel bulbasaur = new JLabel();
		bulbasaur.setIcon(new ImageIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/Bulbasaur_F.gif")).getImage().getScaledInstance(RES, RES, Image.SCALE_DEFAULT))); //gif scale
		bulbasaur.setBounds(57, 59, 96, 96);
		contentPane.add(bulbasaur);
		
		
		JLabel charmander = new JLabel();
		charmander.setIcon(new ImageIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/Charmander_F.gif")).getImage().getScaledInstance(RES, RES, Image.SCALE_DEFAULT))); //gif scale
		charmander.setBounds(267, 59, 96, 96);
		contentPane.add(charmander);
		
		JLabel squirtle = new JLabel();
		squirtle.setIcon(new ImageIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/Squirtle_F.gif")).getImage().getScaledInstance(RES, RES, Image.SCALE_DEFAULT))); //gif scale
		squirtle.setBounds(493, 59, 96, 96);
		contentPane.add(squirtle);
		
	
		JLabel chikorita = new JLabel();
		chikorita.setIcon(new ImageIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/Chikorita_F.gif")).getImage().getScaledInstance(RES, RES, Image.SCALE_DEFAULT))); //gif scale
		chikorita.setBounds(57, 249, 96, 96);
		contentPane.add(chikorita);
		
		JLabel cyndaquil = new JLabel();
		cyndaquil.setIcon(new ImageIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/Cyndaquil_F.gif")).getImage().getScaledInstance(RES, RES, Image.SCALE_DEFAULT))); //gif scale
		cyndaquil.setBounds(267, 249, 96, 96);
		contentPane.add(cyndaquil);
		
		JLabel totodile = new JLabel();
		totodile.setIcon(new ImageIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/Totodile_F.gif")).getImage().getScaledInstance(RES, RES, Image.SCALE_DEFAULT))); //gif scale
		totodile.setBounds(493, 249, 96, 96);
		contentPane.add(totodile);
		
	    btnStartButton = new JButton(BTN_STARTBATTLE_TEXT);
	    btnStartButton.setEnabled(false);
		btnStartButton.setBounds(267, 390, 109, 41);
		contentPane.add(btnStartButton);
		btnStartButton.addActionListener(pkClient);	
		
		JLabel background = new JLabel();
		background.setIcon(new ImageIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/chooser_back.jpg")).getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT))); //back scale
		background.setBounds(0, 0, 644, 453);
		contentPane.add(background);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(btnStartButton.isEnabled()==false) btnStartButton.setEnabled(true);
		btnStartButton.setActionCommand(e.getActionCommand());
	}
}
