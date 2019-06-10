package it.unibs.pajc.pokeproject.util;

public class PKTurnMessage extends PKMessage {

	private static final long serialVersionUID = -8649012472096941718L;

	private boolean opponentFirst;
	
	private int opponentHP;
	private int playerHP; 
	private int opponentMoveID;
	private int playerMoveID; 
	private double effectivenessOpponent;
	private double effectivenessPlayer;

	public PKTurnMessage(Commands commandBody, boolean opponentFirst, int opponentHP, int playerHP, int opponentMoveID, int playerMoveID,
			double effectivenessOpponent, double effectivenessPlayer) {
		super(commandBody);
		this.opponentFirst = opponentFirst;
		this.opponentHP = opponentHP;
		this.playerHP = playerHP;
		this.opponentMoveID = opponentMoveID;
		this.playerMoveID = playerMoveID;
		this.effectivenessOpponent = effectivenessOpponent;
		this.effectivenessPlayer = effectivenessPlayer;
	}

	public boolean getOpponentFirst() {
		return opponentFirst;
	}
	
	public int getPlayerHP() {
		return playerHP;
	}

	public int getOpponentHP() {
		return opponentHP;
	}

	public int getPlayerMoveID() {
		return playerMoveID;
	}

	public int getOpponentMoveID() {
		return opponentMoveID;
	}
	
	public double getEffectivenessOpponent() {
		return effectivenessOpponent;
	}

	public double getEffectivenessPlayer() {
		return effectivenessPlayer;
	}
}
