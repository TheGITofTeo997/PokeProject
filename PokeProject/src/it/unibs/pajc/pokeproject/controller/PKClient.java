package it.unibs.pajc.pokeproject.controller;

public class PKClient {

	public static void main(String[] args) {
		PKClientController pkClient = new PKClientController();
		pkClient.setupClientUtils();
		pkClient.drawGUI();
	}

}
