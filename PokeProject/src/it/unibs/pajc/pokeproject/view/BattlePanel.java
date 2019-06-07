package it.unibs.pajc.pokeproject.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import it.unibs.pajc.pokeproject.model.PKMove;

import javax.swing.JButton;

public class BattlePanel extends JPanel implements KeyListener {
	
	private static final long serialVersionUID = 6693764125058218350L;
	private static final String PKM_FONT = "PKMN_RBYGSC.ttf";
	
	private JLabel lblBackMyPoke;
	private JLabel lblFrontTrainerPoke;
	private JLabel lblNameOpponentPoke;
	private JLabel lblNameMyPoke;
	private JLabel lblMoveText;
	private JButton[] btnMoves;
	private JProgressBar trainerHPbar;
	private JProgressBar opponentHPbar;
	private JButton btnMove1;
	private JButton btnMove2;
	private JButton btnMove3;
	private JButton btnMove4;
	
	private ArrayList<ActionListener> listenerList = new ArrayList<>();
	
	private Timer trainerHPTimer;
	private Timer opponentHPTimer;
	private Timer moveTimer;
	private ActionListener trainerHPdelay;
	private ActionListener opponentHPdelay;

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
		setFocusable(true);
		addKeyListener(this); //KeyListener sul Panel
		setLayout(null);
		setBounds(0, 0, 618, 400);
		setVisible(false);
		
		trainerHPbar = new JProgressBar();
		trainerHPbar.setValue(100);
		trainerHPbar.setStringPainted(true);
		trainerHPbar.setString("");
		trainerHPbar.setForeground(Color.GREEN);
		trainerHPbar.setBounds(488, 248, 99, 6);
		trainerHPbar.setString(""); //percentage string fix, please do NOT set to null
		add(trainerHPbar);
		
		opponentHPbar = new JProgressBar();
		opponentHPbar.setStringPainted(true);
		opponentHPbar.setValue(100);
		opponentHPbar.setForeground(Color.GREEN);
		opponentHPbar.setBounds(123, 72, 99, 6);
		opponentHPbar.setString(""); //percentage string fix, please do NOT set to null
		add(opponentHPbar);
		
		lblNameOpponentPoke = new JLabel("");
		lblNameOpponentPoke.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNameOpponentPoke.setBounds(111, 39, 155, 22);
		lblNameOpponentPoke.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 15));
		add(lblNameOpponentPoke);
		
		lblNameMyPoke = new JLabel("");
		lblNameMyPoke.setBounds(387, 217, 155, 24);
		lblNameMyPoke.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 15));
		add(lblNameMyPoke);
		
		lblMoveText = new JLabel("");
		lblMoveText.setForeground(Color.WHITE);
		lblMoveText.setBounds(344, 327, 254, 62);
		lblMoveText.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 10));
		add(lblMoveText);
		
		JLabel lblEnemyHPLabel = new JLabel();
		lblEnemyHPLabel.setBounds(0, 28, 291, 69);
		lblEnemyHPLabel.setIcon(new ImageIcon(new ImageIcon(BattlePanel.class.getResource("/img/inbattle/utils/battleFoeBoxD.png")).getImage().getScaledInstance(lblEnemyHPLabel.getWidth(), lblEnemyHPLabel.getHeight(), Image.SCALE_DEFAULT))); //box scale
		add(lblEnemyHPLabel);
		
		JLabel lblTrainerHPLabel = new JLabel();
		lblTrainerHPLabel.setBounds(344, 205, 274, 69);
		lblTrainerHPLabel.setIcon(new ImageIcon(new ImageIcon(BattlePanel.class.getResource("/img/inbattle/utils/battlePlayerBoxD.png")).getImage().getScaledInstance(lblTrainerHPLabel.getWidth(), lblTrainerHPLabel.getHeight(), Image.SCALE_DEFAULT))); //box scale
		add(lblTrainerHPLabel);
		
		btnMove1 = new JButton("MOVE1");
		btnMove1.setBounds(25, 323, 122, 31);
		btnMove1.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 10));
		btnMove1.setBorderPainted(false);
		btnMove1.setSelected(true);
		btnMove1.setBackground(Color.YELLOW);
		
		add(btnMove1);	
		btnMove1.addMouseListener(new MouseAdapter() {
			 int charIndex = 0;
	         public void mouseEntered(MouseEvent me) {
	        	 charIndex = 0;
	        	 String text = "Usa " + btnMoves[0].getText();
	        	 moveTimer = new Timer(75, new ActionListener() {
 	                @Override
 	                public void actionPerformed(ActionEvent ev) {
 	                    String labelText = lblMoveText.getText();
 	                    labelText += text.charAt(charIndex);
 	                    lblMoveText.setText(labelText);
 	                    charIndex++;
 	                    if (charIndex >= text.length()) {
 	                        moveTimer.stop();
 	                    }
 	                }
 	            });
 	            moveTimer.start();
	         }
	         public void mouseExited(MouseEvent me) {
	        	 lblMoveText.setText("");
	        	 moveTimer.stop();
	         }
			});
		
		btnMove2 = new JButton("MOVE2");
		btnMove2.setBounds(157, 323, 122, 31);
		btnMove2.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 10));
		btnMove2.setBorderPainted(false);
		btnMove2.setSelected(false);
		add(btnMove2);
		btnMove2.addMouseListener(new MouseAdapter() {
			int charIndex = 0;
	         public void mouseEntered(MouseEvent me) {
	        	 charIndex = 0;
	        	 String text = "Usa " + btnMoves[1].getText();
	        	 moveTimer = new Timer(75, new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent ev) {
	                    String labelText = lblMoveText.getText();
	                    labelText += text.charAt(charIndex);
	                    lblMoveText.setText(labelText);
	                    charIndex++;
	                    if (charIndex >= text.length()) {
	                        moveTimer.stop();
	                    }
	                }
	            });
	            moveTimer.start();
	         }
	         public void mouseExited(MouseEvent me) {
	        	 lblMoveText.setText("");
	        	 moveTimer.stop();
	         }
			});
		
		btnMove3 = new JButton("MOVE3");
		btnMove3.setBounds(25, 358, 122, 31);
		btnMove3.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 10));
		btnMove3.setBorderPainted(false);
		btnMove3.setSelected(false);
		add(btnMove3);
		btnMove3.addMouseListener(new MouseAdapter() {
			int charIndex = 0;
	         public void mouseEntered(MouseEvent me) {
	        	 charIndex = 0;
	        	 String text = "Usa " + btnMoves[2].getText();
	        	 moveTimer = new Timer(75, new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent ev) {
	                    String labelText = lblMoveText.getText();
	                    labelText += text.charAt(charIndex);
	                    lblMoveText.setText(labelText);
	                    charIndex++;
	                    if (charIndex >= text.length()) {
	                        moveTimer.stop();
	                    }
	                }
	            });
	            moveTimer.start();
	         }
	         public void mouseExited(MouseEvent me) {
	        	 lblMoveText.setText("");
	        	 moveTimer.stop();
	         }
			});
		
		btnMove4 = new JButton("MOVE4");
		btnMove4.setBounds(157, 358, 122, 31);
		btnMove4.setFont(new Font("PKMN RBYGSC", Font.PLAIN, 10));
		btnMove4.setBorderPainted(false);
		btnMove4.setSelected(false);
		add(btnMove4);
		btnMove4.addMouseListener(new MouseAdapter() {
			int charIndex = 0;
	         public void mouseEntered(MouseEvent me) {
	        	 charIndex = 0;
	        	 String text = "Usa " + btnMoves[3].getText();
	        	 moveTimer = new Timer(75, new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent ev) {
	                    String labelText = lblMoveText.getText();
	                    labelText += text.charAt(charIndex);
	                    lblMoveText.setText(labelText);
	                    charIndex++;
	                    if (charIndex >= text.length()) {
	                        moveTimer.stop();
	                    }
	                }
	            });
	            moveTimer.start();
	         }
	         public void mouseExited(MouseEvent me) {
	        	 lblMoveText.setText("");
	        	 moveTimer.stop();
	         }
			});
		
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
		setlblMoveText("Usa " + btnMoves[0].getText());
	}
	
	public void setPokeHP(int trainerHP, int enemyHP) {
		trainerHPbar.setMaximum(trainerHP);
		opponentHPbar.setMaximum(enemyHP);
	}
	
	private void setBarColor(JProgressBar bar) {
		int percentage = (int)((double)bar.getValue()/bar.getMaximum()*100);
		System.out.println(percentage + "%");
		if(percentage>50) 
			bar.setForeground(Color.GREEN);
		else if(percentage>20 && percentage<=50) 
			bar.setForeground(Color.ORANGE);
		else
			bar.setForeground(Color.RED);
	}
	
	public void setTrainerHPLevel(int value) {
		trainerHPdelay = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(trainerHPbar.getValue() > value) {
					trainerHPbar.setValue(trainerHPbar.getValue()-1);
					setBarColor(trainerHPbar);
				}
				else
				trainerHPTimer.stop();
			}
			
		};
		trainerHPTimer = new Timer(200, trainerHPdelay);
		trainerHPTimer.start();
	}
	
	public void setOpponentHPLevel(int value) {
		opponentHPdelay = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(opponentHPbar.getValue() > value) {
					opponentHPbar.setValue(opponentHPbar.getValue()-1);
					setBarColor(opponentHPbar);
				}
				else
				opponentHPTimer.stop();
			}
			
		};
		opponentHPTimer = new Timer(200, opponentHPdelay);
		opponentHPTimer.start();
	}
	
	public void addListener(ActionListener e) {
			listenerList.add(e);
	}
	
	private void fireActionPerformed(ActionEvent e) {
		for(ActionListener l : listenerList) {
			l.actionPerformed(e);
		}
	}
	
	public void setlblMoveText(String text){
		//metodo per animazione scrittura lblMoveText
   	 moveTimer = new Timer(45, new ActionListener() {
				int charIndex = 0;
				public void actionPerformed(ActionEvent e) {
					 String labelText = lblMoveText.getText();
		                labelText += text.charAt(charIndex);
		                lblMoveText.setText(labelText);
		                charIndex++;
		                if (charIndex >= text.length()) {
		                    moveTimer.stop();
		                }	
				} 
       });
       moveTimer.start();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getExtendedKeyCode() == KeyEvent.VK_1 || 
				e.getExtendedKeyCode() == KeyEvent.VK_NUMPAD1) btnMove1.doClick();
		if(e.getExtendedKeyCode() == KeyEvent.VK_2 || 
				e.getExtendedKeyCode() == KeyEvent.VK_NUMPAD2) btnMove2.doClick();
		if(e.getExtendedKeyCode() == KeyEvent.VK_3 || 
				e.getExtendedKeyCode() == KeyEvent.VK_NUMPAD3) btnMove3.doClick();
		if(e.getExtendedKeyCode() == KeyEvent.VK_4 || 
				e.getExtendedKeyCode() == KeyEvent.VK_NUMPAD4) btnMove4.doClick();
		
		if(e.getExtendedKeyCode() == KeyEvent.VK_RIGHT && btnMove1.isSelected()) {
			moveTimer.stop();
			lblMoveText.setText("");
			btnMove1.setBackground(null);
			btnMove1.setSelected(false);
			btnMove2.setSelected(true);
			btnMove2.setBackground(Color.YELLOW);
			setlblMoveText("Usa " + btnMoves[1].getText());
			}
		if(e.getExtendedKeyCode() == KeyEvent.VK_RIGHT && btnMove3.isSelected()) {
			moveTimer.stop();
			lblMoveText.setText("");
			btnMove3.setBackground(null);
			btnMove3.setSelected(false);
			btnMove4.setSelected(true);
			btnMove4.setBackground(Color.YELLOW);
			setlblMoveText("Usa " + btnMoves[3].getText());
			
		 	}
		if(e.getExtendedKeyCode() == KeyEvent.VK_LEFT && btnMove2.isSelected()) {
			moveTimer.stop();
			lblMoveText.setText("");
			btnMove2.setBackground(null);
			btnMove2.setSelected(false);
			btnMove1.setSelected(true);
			btnMove1.setBackground(Color.YELLOW);
			setlblMoveText("Usa " + btnMoves[0].getText());
			}
		if(e.getExtendedKeyCode() == KeyEvent.VK_LEFT && btnMove4.isSelected()) {
			moveTimer.stop();
			lblMoveText.setText("");
			btnMove4.setBackground(null);
			btnMove4.setSelected(false);
			btnMove3.setSelected(true);
			btnMove3.setBackground(Color.YELLOW);
			setlblMoveText("Usa " + btnMoves[2].getText());
		}
		if(e.getExtendedKeyCode() == KeyEvent.VK_UP && btnMove3.isSelected()) {
			moveTimer.stop();
			lblMoveText.setText("");
			btnMove3.setBackground(null);
			btnMove3.setSelected(false);
			btnMove1.setSelected(true);
			btnMove1.setBackground(Color.YELLOW);
			setlblMoveText("Usa " + btnMoves[0].getText());
		}
		if(e.getExtendedKeyCode() == KeyEvent.VK_UP && btnMove4.isSelected()) {
			moveTimer.stop();
			lblMoveText.setText("");
			btnMove4.setBackground(null);
			btnMove4.setSelected(false);
			btnMove2.setSelected(true);
			btnMove2.setBackground(Color.YELLOW);
			setlblMoveText("Usa " + btnMoves[1].getText());
		}
		if(e.getExtendedKeyCode() == KeyEvent.VK_DOWN && btnMove1.isSelected()) {
			moveTimer.stop();
			lblMoveText.setText("");
			btnMove1.setBackground(null);
			btnMove1.setSelected(false);
			btnMove3.setSelected(true);
			btnMove3.setBackground(Color.YELLOW);
			setlblMoveText("Usa " + btnMoves[2].getText());
		}
		if(e.getExtendedKeyCode() == KeyEvent.VK_DOWN && btnMove2.isSelected()) {
			moveTimer.stop();
			lblMoveText.setText("");
			btnMove2.setBackground(null);
			btnMove2.setSelected(false);
			btnMove4.setSelected(true);
			btnMove4.setBackground(Color.YELLOW);
			setlblMoveText("Usa " + btnMoves[3].getText());
		}
		if(e.getExtendedKeyCode() == KeyEvent.VK_ENTER) {
			if(btnMove1.isSelected()) btnMove1.doClick();
			if(btnMove2.isSelected()) btnMove2.doClick();
			if(btnMove3.isSelected()) btnMove3.doClick();
			if(btnMove4.isSelected()) btnMove4.doClick();
		}	
		
	}

	@Override
	public void keyReleased(KeyEvent e) {		
	}
}
