package it.unibs.pajc.pokeproject.view;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.awt.event.ActionEvent;

import it.unibs.pajc.pokeproject.model.Pokemon;
import it.unibs.pajc.pokeproject.util.PKClientStrings;



public class PokeChooserPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = -251102778958545816L;
	private static final int RES = 96;
	private static final int BUTTON_HEIGHT = 16;
	private static final int HORIZONTAL_SPACING = 90;
	private static final int VERTICAL_SPACING = 30;
	private static final int WIDTH_LIMIT = 468;
	private static final int PANEL_WIDTH = 648;
	private static final int PANEL_HEIGHT = 500;
	private ArrayList<ActionListener> listenerList = new ArrayList<>();
	private ButtonGroup group;
	private JButton btnStartButton;
	private JLabel pokemon;
	private JRadioButton rdbtnChooseMe;
	private JPanel infoPokemon;
	private JLabel lblInfoIconPokemon;
	private JLabel lblInfoNamePokemon;
	private JLabel lblInfoTypePokemon;
	private JLabel lblInfoHPPokemon;
	private JLabel lblInfoAttackPokemon;
	private JLabel lblInfoDefensePokemon;
	
	/**
	 * Create the panel.
	 */
	public PokeChooserPanel(TreeMap<Integer, Pokemon> db) {
		setVisible(false);
		setLayout(null);
		setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
		
		btnStartButton = new JButton(PKClientStrings.BTN_STARTBATTLE_TEXT);
	    btnStartButton.setEnabled(false);
		btnStartButton.setBounds(PANEL_WIDTH/2-3*RES/4, PANEL_HEIGHT-RES, RES*3/2, 2*BUTTON_HEIGHT);
		add(btnStartButton);
		btnStartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnStartButton.setEnabled(false);
				ActionEvent arg = new ActionEvent(btnStartButton, ActionEvent.ACTION_PERFORMED, btnStartButton.getActionCommand());
				fireActionPerformed(arg);
			}
		});	
		
		infoPokemon = new JPanel();
		infoPokemon.setVisible(false);
		infoPokemon.setBackground(Color.GRAY);
		infoPokemon.setLayout(null);
		add(infoPokemon);
		
		lblInfoIconPokemon = new JLabel();
		lblInfoIconPokemon.setVisible(false);
		infoPokemon.add(lblInfoIconPokemon);
		
		lblInfoNamePokemon= new JLabel();
		lblInfoNamePokemon.setForeground(Color.WHITE);
		infoPokemon.add(lblInfoNamePokemon);
		
		lblInfoTypePokemon= new JLabel();
		lblInfoTypePokemon.setVisible(false);
		lblInfoTypePokemon.setForeground(Color.WHITE);
		infoPokemon.add(lblInfoTypePokemon);
		
		lblInfoHPPokemon= new JLabel();
		lblInfoHPPokemon.setVisible(false);
		lblInfoHPPokemon.setForeground(Color.WHITE);
		infoPokemon.add(lblInfoHPPokemon);
		
		lblInfoAttackPokemon= new JLabel();
		lblInfoAttackPokemon.setVisible(false);
		lblInfoAttackPokemon.setForeground(Color.WHITE);
		infoPokemon.add(lblInfoAttackPokemon);
		
		lblInfoDefensePokemon= new JLabel();
		lblInfoDefensePokemon.setForeground(Color.WHITE);
		lblInfoIconPokemon.setVisible(false);
		infoPokemon.add(lblInfoDefensePokemon);
		
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
			
			pokemon.addMouseListener(new MouseAdapter() {
				int x = pokemon.getBounds().x;
				int y = pokemon.getBounds().y;
				
		         public void mouseEntered(MouseEvent me) {
		        	 infoPokemon.setVisible(true);
		        	 infoPokemon.setBounds(x, y, 176, 96);
		        	 
		        	 lblInfoIconPokemon.setVisible(true);
		        	 lblInfoIconPokemon.setBounds(0,0, 48, 48);
		        	 lblInfoIconPokemon.setIcon(new ImageIcon(new ImageIcon(toDraw.getFrontSprite()).getImage()
		        			 .getScaledInstance(48, 48, Image.SCALE_DEFAULT)));
		        	 
		        	lblInfoNamePokemon.setVisible(true);
		        	lblInfoNamePokemon.setBounds(60, 5, 140, 15);
		        	lblInfoNamePokemon.setText(toDraw.getName().toUpperCase());
		        	
		        	lblInfoTypePokemon.setVisible(true);
		        	lblInfoTypePokemon.setBounds(60, 25, 140, 15);
		        	lblInfoTypePokemon.setText("TYPE: " +toDraw.getType().getTypeName());
		        	
		        	lblInfoHPPokemon.setVisible(true);
		        	lblInfoHPPokemon.setBounds(60, 40, 140, 15);
		        	lblInfoHPPokemon.setText("HP: " + toDraw.getHP());
		        	
		        	lblInfoAttackPokemon.setVisible(true);
		        	lblInfoAttackPokemon.setBounds(60, 55, 140, 15);
		        	lblInfoAttackPokemon.setText("ATTACK: " + toDraw.getAttack());
		        	
		        	lblInfoDefensePokemon.setVisible(true);
		        	lblInfoDefensePokemon.setBounds(60, 70, 140, 15);
		        	lblInfoDefensePokemon.setText("DEFENSE: " + toDraw.getDefense());
		        	 
		         }
		         public void mouseExited(MouseEvent me) {
		        	 infoPokemon.setVisible(false);
		         }
				});
			
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
