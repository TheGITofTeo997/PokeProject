package it.unibs.pajc.pokeproject.view;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class WaitingPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public WaitingPanel() {
		setEnabled(false);
		setVisible(false);
		setBounds(100, 100, 361, 233);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(null);
	}

}
