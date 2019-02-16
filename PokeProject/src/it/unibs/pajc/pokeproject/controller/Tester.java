package it.unibs.pajc.pokeproject.controller;

import java.io.File;

public class Tester {

	private static final String POKEMON_DIR = "data\\pokemon";
	private static final String TYPE_DIR = "data\\type";
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File pokemonDir = new File(POKEMON_DIR);
		File typeDir = new File(TYPE_DIR);
		if(!pokemonDir.exists()) System.out.println("NAY");
		if(pokemonDir.isDirectory()) {
			System.out.println("YAY");
			File[] theList = pokemonDir.listFiles();
			for(int i = 0; i < theList.length; i++) {
				System.out.println(theList[i].toString());
			}
		}
		if(!typeDir.exists()) System.out.println("NAY");
		if(typeDir.isDirectory()) {
			System.out.println("YAY");
			File[] theList = typeDir.listFiles();
			for(int i = 0; i < theList.length; i++) {
				System.out.println(theList[i].toString());
			}
		}
	}

}
