package it.unibs.pajc.pokeproject.util;

import java.io.*;

public class PKMessage implements Serializable {
	private static final long serialVersionUID = 5755675874453942785L;
	private Commands commandBody;
	private int clientID;
	private int dataToCarry;
	
	public PKMessage(Commands commandBody, int dataToCarry) {
		this.commandBody = commandBody;
		this.dataToCarry = dataToCarry;
	}
	
	public PKMessage(Commands commandBody) {
		this.commandBody = commandBody;
		this.dataToCarry = 0;
	}
	
	public Commands getCommandBody() {
		return commandBody;
	}

	public int getDataToCarry() {
		return dataToCarry;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
	
	
}
