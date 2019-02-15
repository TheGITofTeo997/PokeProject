package it.unibs.pajc.pokeproject.controller;

import it.unibs.pajc.pokeproject.view.PKServerWindow;

public class PKServer {

	//Entrypoint, instanzia la view e il controller
	public static void main(String[] args) {
		//PKServerModel model = new ServerModel();
		PKServerWindow view = new PKServerWindow();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//devo aspettare che la view si crei o qualcosa del genere
		//senza questo try tutto va a puttane
		PKServerController controller = new PKServerController();
		controller.setView(view);
		
		//controller.setModel(model);
		
		view.setController(controller);
	}

}
