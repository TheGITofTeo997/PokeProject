package it.unibs.pajc.pokeproject.controller;

import it.unibs.pajc.pokeproject.view.*;

public class PKClient {

	public static void main(String[] args) {
		//PKClientModel model = new ClientModel();
		PKClientWindow view = new PKClientWindow();
		PKClientController pkClient = new PKClientController();
	}

}
