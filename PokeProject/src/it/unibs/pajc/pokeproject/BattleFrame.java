package it.unibs.pajc.pokeproject;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BattleFrame extends JFrame {
	private static final long serialVersionUID = -9090665586698145181L;
	private static final String TITLE = "PokeBattle";
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BattleFrame frame = new BattleFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public BattleFrame() {
		setResizable(false);
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 624, 429);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JProgressBar trainerHPbar = new JProgressBar();
		trainerHPbar.setValue(100);
		trainerHPbar.setStringPainted(true);
		trainerHPbar.setString("");
		trainerHPbar.setForeground(Color.GREEN);
		trainerHPbar.setBounds(488, 248, 99, 6);
		trainerHPbar.setString(""); //percentage string fix, please do NOT set to null
		contentPane.add(trainerHPbar);
		
		JProgressBar enemyHPbar = new JProgressBar();
		enemyHPbar.setStringPainted(true);
		enemyHPbar.setValue(100);
		enemyHPbar.setForeground(Color.GREEN);
		enemyHPbar.setBounds(123, 72, 99, 6);
		enemyHPbar.setString(""); //percentage string fix, please do NOT set to null
		contentPane.add(enemyHPbar);
		
		JLabel lblEnemyHPLabel = new JLabel();
		lblEnemyHPLabel.setBounds(0, 28, 291, 69);
		lblEnemyHPLabel.setIcon(new ImageIcon(new ImageIcon(BattleFrame.class.getResource("/img/inbattle/utils/battleFoeBoxD.png")).getImage().getScaledInstance(lblEnemyHPLabel.getWidth(), lblEnemyHPLabel.getHeight(), Image.SCALE_DEFAULT))); //box scale
		contentPane.add(lblEnemyHPLabel);
		
		JLabel lblTrainerHPLabel = new JLabel();
		lblTrainerHPLabel.setBounds(344, 205, 274, 69);
		lblTrainerHPLabel.setIcon(new ImageIcon(new ImageIcon(BattleFrame.class.getResource("/img/inbattle/utils/battlePlayerBoxD.png")).getImage().getScaledInstance(lblTrainerHPLabel.getWidth(), lblTrainerHPLabel.getHeight(), Image.SCALE_DEFAULT))); //box scale
		contentPane.add(lblTrainerHPLabel);
		
		JLabel lblTextBoxLabel = new JLabel();
		lblTextBoxLabel.setBounds(0, 302, 618, 98);
		lblTextBoxLabel.setIcon(new ImageIcon(new ImageIcon(BattleFrame.class.getResource("/img/inbattle/utils/battleCommand.png")).getImage().getScaledInstance(lblTextBoxLabel.getWidth(), lblTextBoxLabel.getHeight(), Image.SCALE_DEFAULT))); //box scale
		contentPane.add(lblTextBoxLabel);
		
		JLabel lblTrainerBaseLabel = new JLabel();
		lblTrainerBaseLabel.setBounds(-28, 217, 319, 155);
		lblTrainerBaseLabel.setIcon(new ImageIcon(new ImageIcon(BattleFrame.class.getResource("/img/inbattle/enemybaseFieldGrass.png")).getImage().getScaledInstance(lblTrainerBaseLabel.getWidth(), lblTrainerBaseLabel.getHeight(), Image.SCALE_DEFAULT))); //base scale
		contentPane.add(lblTrainerBaseLabel);
		
		JLabel lblEnemyBaseLabel = new JLabel();
		lblEnemyBaseLabel.setBounds(377, 101, 231, 125);
		lblEnemyBaseLabel.setIcon(new ImageIcon(new ImageIcon(BattleFrame.class.getResource("/img/inbattle/enemybaseFieldGrass.png")).getImage().getScaledInstance(lblEnemyBaseLabel.getWidth(), lblEnemyBaseLabel.getHeight(), Image.SCALE_DEFAULT))); //base scale
		contentPane.add(lblEnemyBaseLabel);
		
		JLabel background = new JLabel();
		background.setIcon(new ImageIcon(new ImageIcon(BattleFrame.class.getResource("/img/inbattle/battlebgField.png")).getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT))); //back scale
		background.setBounds(0, 0, 618, 400);
		contentPane.add(background);
		setLocationRelativeTo(null);
		setVisible(false);
	}
}
