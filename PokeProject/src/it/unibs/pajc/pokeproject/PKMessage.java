package it.unibs.pajc.pokeproject;

import java.io.*;

public class PKMessage implements Serializable {
	private String commandBody;
	private int clientID;
	private int dataToCarry;
	
	public PKMessage(String commandBody, int dataToCarry) {
		this.commandBody = commandBody;
		this.dataToCarry = dataToCarry;
	}
	
	public String getCommandBody() {
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
