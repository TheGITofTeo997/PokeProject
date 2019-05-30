package it.unibs.pajc.pokeproject.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import it.unibs.pajc.pokeproject.model.PKMove;

import javax.swing.JButton;

public class BattlePanel extends JPanel {
	
	private static final long serialVersionUID = 6693764125058218350L;
	private static final String PKM_FONT = "PKMN_RBYGSC.ttf";
	
	private JLabel lblBackMyPoke;
	private JLabel lblFrontTrainerPoke;
	private JLabel lblNameOpponentPoke;
	private JLabel lblNameMyPoke;
	private JButton[] btnMoves;
	private JProgressBar trainerHPbar;
	private JProgressBar enemyHPbar;
	
	private ArrayList<ActionListener> listenerList = new ArrayList<>();

	/**
	 * Create the panel.
	 */
	public BattlePanel() {
		try {
		     GraphicsEnvironment ge = 
		         GraphicsEnvironment.getLocalGraphicsEnvironment();
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(PKM_FONT)));
		} catch (IOException|FontFormatException e) {
		     e.printStackTrace();
		}
		
		setLayout(null);
		setBounds(100, 100, 618, 400);
		setVisible(false);
		
		trainerHPbar = new JProgressBar();
		trainerHPbar.setValue(100);
		trainerHPbar.setStringPainted(true);
		trainerHPbar.setString("");
		trainerHPbar.setForeground(Color.GREEN);
		trainerHPbar.setBounds(488, 248, 99, 6);
		trainerHPbar.setString(""); //percentage string fix, please do NOT set to null
		add(trainerHPbar);
		
		enemyHPbar = new JProgressBar();
		enemyHPbar.setStringPainted(true);
		enemyHPbar.setValue(100);
		enemyHPbar.setForeground(Color.GREEN);
		enemyHPbar.setBounds(123, 72, 99, 6);
		enemyHPbar.setString(""); //percentage string fix, please do NOT set to null
		add(enemyHPbar);
		
		lblNameOpponentPoke = new JLabel("");
		lblNameOpponentPoke.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNameOpponentPoke.setBounds(111, 39, 155, 22);
		lblNameOpponentPoke.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 15));
		add(lblNameOpponentPoke);
		
		lblNameMyPoke = new JLabel("");
		lblNameMyPoke.setBounds(387, 217, 155, 24);
		lblNameMyPoke.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 15));
		add(lblNameMyPoke);
		
		JLabel lblEnemyHPLabel = new JLabel();
		lblEnemyHPLabel.setBounds(0, 28, 291, 69);
		lblEnemyHPLabel.setIcon(new ImageIcon(new ImageIcon(BattlePanel.class.getResource("/img/inbattle/utils/battleFoeBoxD.png")).getImage().getScaledInstance(lblEnemyHPLabel.getWidth(), lblEnemyHPLabel.getHeight(), Image.SCALE_DEFAULT))); //box scale
		add(lblEnemyHPLabel);
		
		JLabel lblTrainerHPLabel = new JLabel();
		lblTrainerHPLabel.setBounds(344, 205, 274, 69);
		lblTrainerHPLabel.setIcon(new ImageIcon(new ImageIcon(BattlePanel.class.getResource("/img/inbattle/utils/battlePlayerBoxD.png")).getImage().getScaledInstance(lblTrainerHPLabel.getWidth(), lblTrainerHPLabel.getHeight(), Image.SCALE_DEFAULT))); //box scale
		add(lblTrainerHPLabel);
		
		JButton btnMove1 = new JButton("MOVE1");
		btnMove1.setBounds(25, 323, 122, 31);
		btnMove1.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 10));
		btnMove1.setBorderPainted(false);
		add(btnMove1);
		
		JButton btnMove2 = new JButton("MOVE2");
		btnMove2.setBounds(157, 323, 122, 31);
		btnMove2.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 10));
		btnMove1.setBorderPainted(false);
		add(btnMove2);
		
		JButton btnMove3 = new JButton("MOVE3");
		btnMove3.setBounds(25, 358, 122, 31);
		btnMove3.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 10));
		btnMove1.setBorderPainted(false);
		add(btnMove3);
		
		JButton btnMove4 = new JButton("MOVE4");
		btnMove4.setBounds(157, 358, 122, 31);
		btnMove4.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 10));
		btnMove1.setBorderPainted(false);
		add(btnMove4);
		
		btnMoves = new JButton[]{btnMove1, btnMove2, btnMove3, btnMove4};
		
		JLabel lblTextBoxLabel = new JLabel();
		lblTextBoxLabel.setBounds(0, 302, 618, 98);
		lblTextBoxLabel.setIcon(new ImageIcon(new ImageIcon(BattlePanel.class.getResource("/img/inbattle/utils/battleCommand.png")).getImage().getScaledInstance(lblTextBoxLabel.getWidth(), lblTextBoxLabel.getHeight(), Image.SCALE_DEFAULT))); //box scale
		add(lblTextBoxLabel);
		
		lblBackMyPoke = new JLabel();
		lblBackMyPoke.setBounds(42, 163, 196, 196);
		add(lblBackMyPoke);
		
		lblFrontTrainerPoke = new JLabel();
		lblFrontTrainerPoke.setBounds(446, 72, 96, 96);
		add(lblFrontTrainerPoke);
			
		JLabel lblTrainerBaseLabel = new JLabel();
		lblTrainerBaseLabel.setBounds(-28, 217, 319, 155);
		lblTrainerBaseLabel.setIcon(new ImageIcon(new ImageIcon(BattlePanel.class.getResource("/img/inbattle/enemybaseFieldGrass.png")).getImage().getScaledInstance(lblTrainerBaseLabel.getWidth(), lblTrainerBaseLabel.getHeight(), Image.SCALE_DEFAULT))); //base scale
		add(lblTrainerBaseLabel);
		
		JLabel lblEnemyBaseLabel = new JLabel();
		lblEnemyBaseLabel.setBounds(377, 101, 231, 125);
		lblEnemyBaseLabel.setIcon(new ImageIcon(new ImageIcon(BattlePanel.class.getResource("/img/inbattle/enemybaseFieldGrass.png")).getImage().getScaledInstance(lblEnemyBaseLabel.getWidth(), lblEnemyBaseLabel.getHeight(), Image.SCALE_DEFAULT))); //base scale
		add(lblEnemyBaseLabel);
		
		JLabel background = new JLabel();
		background.setIcon(new ImageIcon(new ImageIcon(BattlePanel.class.getResource("/img/inbattle/battlebgField.png")).getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT))); //back scale
		background.setBounds(0, 0, 618, 400);
		add(background);
		
	}
	
	public void setSprites(URL backSpriteUrl, URL frontSpriteUrl) {
		lblBackMyPoke.setIcon(new ImageIcon(backSpriteUrl));
		lblFrontTrainerPoke.setIcon(new ImageIcon(new ImageIcon(frontSpriteUrl).getImage().getScaledInstance(lblFrontTrainerPoke.getWidth(), lblFrontTrainerPoke.getHeight(), Image.SCALE_DEFAULT)));
	}
	
	public void setPokeNames(String trainer, String opponent) {
		lblNameMyPoke.setText(trainer);
		lblNameOpponentPoke.setText(opponent);
	}
	
	public void setMoveNames(PKMove[] moves) {
		for(int i=0;i<4;i++) {
			int j = i;
			btnMoves[i].setText(moves[i].getName());
			btnMoves[i].setActionCommand(String.valueOf(i));
			btnMoves[i].addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					ActionEvent event = new ActionEvent(btnMoves[j], ActionEvent.ACTION_PERFORMED, btnMoves[j].getActionCommand());
					fireActionPerformed(event);
				}
			});
		}
	}
	
	public void setPokeHP(int trainerHP, int enemyHP) {
		trainerHPbar.setMaximum(trainerHP);
		enemyHPbar.setMaximum(enemyHP);
	}
	
	private void setTrainerBarColor() {
		double percentage = ((double)trainerHPbar.getValue()/(double)trainerHPbar.getMaximum())*100;
		percentage = (int)percentage;
		System.out.println(percentage + "%");
		if(percentage>50) 
			trainerHPbar.setForeground(Color.GREEN);
		else if(percentage>20 && percentage<=50) 
			trainerHPbar.setForeground(Color.ORANGE);
		else
			trainerHPbar.setForeground(Color.RED);
	}
	
	private void setEnemyBarColor() {
		double percentage = ((double)enemyHPbar.getValue()/(double)enemyHPbar.getMaximum())*100;
		percentage = (int)percentage;
		System.out.println(percentage + "%");
		if(percentage>50) 
			enemyHPbar.setForeground(Color.GREEN);
		else if(percentage>20 && percentage<=50) 
			enemyHPbar.setForeground(Color.ORANGE);
		else
			enemyHPbar.setForeground(Color.RED);
	}
	
	public void setTrainerHPLevel(int value) {
		trainerHPbar.setValue(value); //remaining hp after damage
		setTrainerBarColor();
	}
	
	public void setEnemyHPLevel(int value) {
		enemyHPbar.setValue(value); //remaining hp after damage
		setEnemyBarColor();
	}
	
	public void addListener(ActionListener e) {
			listenerList.add(e);
	}
	
	private void fireActionPerformed(ActionEvent e) {
		for(ActionListener l : listenerList) {
			l.actionPerformed(e);
		}
	}
}
