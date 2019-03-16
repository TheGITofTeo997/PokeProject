package it.unibs.pajc.pokeproject.view;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import it.unibs.pajc.pokeproject.model.Pokemon;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.awt.event.ActionEvent;

public class PokeChooserPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = -251102778958545816L;
	private static final int RES = 96;
	private static final int BUTTON_HEIGHT = 16;
	private static final int HORIZONTAL_SPACING = 90;
	private static final int VERTICAL_SPACING = 30;
	private static final int WIDTH_LIMIT = 468;
	private static final int PANEL_WIDTH = 648;
	private static final int PANEL_HEIGHT = 500;
	private static final String CHOOSE_ME = "Choose Me!";
	private static final String BTN_STARTBATTLE_TEXT = "Start Battle";
	private ArrayList<ActionListener> listenerList = new ArrayList<>();
	private ButtonGroup group;
	private JButton btnStartButton;
	private JLabel pokemon;
	private JRadioButton rdbtnChooseMe;
	
	/**
	 * Create the panel.
	 */
	public PokeChooserPanel(TreeMap<Integer, Pokemon> db) {
		setVisible(false);
		setLayout(null);
		setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
		
		btnStartButton = new JButton(BTN_STARTBATTLE_TEXT);
	    btnStartButton.setEnabled(false);
		btnStartButton.setBounds(PANEL_WIDTH/2-3*RES/4, PANEL_HEIGHT-RES, RES*3/2, 2*BUTTON_HEIGHT);
		add(btnStartButton);
		btnStartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionEvent arg = new ActionEvent(btnStartButton, ActionEvent.ACTION_PERFORMED, btnStartButton.getActionCommand());
				fireActionPerformed(arg);
			}
		});	
		
		drawPokemons(db);
		
		JLabel background = new JLabel();
		background.setIcon(new ImageIcon(new ImageIcon(PokeChooserPanel.class.getResource("/img/chooser_back.jpg")).getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT))); //back scale
		background.setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
		add(background);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(btnStartButton.isEnabled()==false) btnStartButton.setEnabled(true);
		btnStartButton.setActionCommand(e.getActionCommand());
	}
	
	private void drawPokemons(TreeMap<Integer, Pokemon> db) {
		group = new ButtonGroup();
		int x = 90;
		int y = 50;
		for(Entry<Integer, Pokemon> entry : db.entrySet())
		{
			if(x > WIDTH_LIMIT) {
				x = 90;
				y = y + BUTTON_HEIGHT + 2*VERTICAL_SPACING + RES;
			}
			
			Pokemon toDraw = entry.getValue();
			
			pokemon = new JLabel();
			pokemon.setIcon(new ImageIcon(new ImageIcon(toDraw.getFrontSprite()).getImage().getScaledInstance(RES, RES, Image.SCALE_DEFAULT))); //gif scale
			pokemon.setBounds(x, y, RES, RES);
			add(pokemon);
			
			rdbtnChooseMe = new JRadioButton(toDraw.getName());
			rdbtnChooseMe.setOpaque(false);
			rdbtnChooseMe.setBounds(x, y + RES + VERTICAL_SPACING, RES, BUTTON_HEIGHT);
			rdbtnChooseMe.setBorderPainted(false);
			rdbtnChooseMe.setContentAreaFilled(false);
			rdbtnChooseMe.setActionCommand(String.valueOf(entry.getKey()));
			add(rdbtnChooseMe);
			
			rdbtnChooseMe.addActionListener(this);
			group.add(rdbtnChooseMe);
			
			x = x + RES + HORIZONTAL_SPACING;
		}
	}
	
	public void fireActionPerformed(ActionEvent e) {
		for(ActionListener l : listenerList)
			l.actionPerformed(e);
	}
	
	public void addListener(ActionListener l) {
		listenerList.add(l);
	}
}
