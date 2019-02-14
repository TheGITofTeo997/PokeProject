package it.unibs.pajc.pokeproject.util;

import java.io.*;

public class PKMessage implements Serializable {
	private static final long serialVersionUID = 5755675874453942785L;
	private String commandBody;
	private int clientID;
	private int dataToCarry;
	
	public PKMessage(String commandBody, int dataToCarry) {
		this.commandBody = commandBody;
		this.dataToCarry = dataToCarry;
	}
	
	public PKMessage(String commandBody) {
		this.commandBody = commandBody;
		this.dataToCarry = 0;
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
