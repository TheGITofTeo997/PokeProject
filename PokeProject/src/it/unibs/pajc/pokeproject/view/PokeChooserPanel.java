package it.unibs.pajc.pokeproject.view;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import it.unibs.pajc.pokeproject.model.Pokemon;

import java.awt.GridLayout;
import java.awt.Image;

public class PokeChooserPanel extends JPanel {

	private static final int RES = 192;
	private static final String CHOOSE_ME = "Choose Me!";
	private static final String BTN_STARTBATTLE_TEXT = "Start Battle";
	private ButtonGroup group;
	/**
	 * Create the panel.
	 */
	public PokeChooserPanel() {
		ButtonGroup group = new ButtonGroup();
		setLayout(null);
		
		JButton btnStartButton = new JButton(BTN_STARTBATTLE_TEXT);
	    btnStartButton.setEnabled(false);
		btnStartButton.setBounds(0, 0, 225, 300);
		add(btnStartButton);
		
		JLabel background = new JLabel();
		background.setBounds(225, 0, 225, 300);
		background.setIcon(new ImageIcon(new ImageIcon(PokeChooserFrame.class.getResource("/img/chooser_back.jpg")).getImage())); //back scale
		add(background);
	}
	
	// THIS IS THE PROBLEM.
	public void drawPokemon(Pokemon toDraw) {
		JRadioButton rdbtnChooseMe = new JRadioButton(toDraw.getName());
		rdbtnChooseMe.setOpaque(false);
		rdbtnChooseMe.setBounds(57, 172, 109, 23);
		rdbtnChooseMe.setBorderPainted(false);
		rdbtnChooseMe.setContentAreaFilled(false);
		rdbtnChooseMe.setActionCommand(String.valueOf(toDraw.getID()));
		add(rdbtnChooseMe);
		
		JLabel pokemon = new JLabel();
		pokemon.setIcon(new ImageIcon(new ImageIcon(toDraw.getBackSprite()).getImage().getScaledInstance(RES, RES, Image.SCALE_DEFAULT))); //gif scale
		pokemon.setBounds(493, 59, 96, 96);
		add(pokemon);
		
		group.add(rdbtnChooseMe);
	}

}
