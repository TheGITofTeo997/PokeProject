package it.unibs.pajc.pokeproject.model;

import java.io.*;
import java.util.HashMap;
import java.util.StringTokenizer;

public class PKType implements Serializable{
	private static final long serialVersionUID = -1261663080265777425L;
	// hashmap contenente le associazioni tra il tipo considerato e l'efficacia sugli altri tipi
	private HashMap<String, Double> typeDatabase = new HashMap<>();
	private String type;
	
	public PKType(String type) {
		this.type = type;
		try {
			checkForFileTypeExistance();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		fillTypeMap(type);
		
	}
	
	public double getEffectiveness(String type) {
		return typeDatabase.get(type);
	}
	
	public String getTypeName() {
		return this.type;
	}
	
	/**
	 * Checks if the file of the type specified in the constructor exists, if not throws an exception
	 * @return
	 */
	
	private void checkForFileTypeExistance() throws FileNotFoundException{
		File typeFile = new File(type + "tp");
		if(!typeFile.exists())
			throw new FileNotFoundException();
	}
	
	private void fillTypeMap(String type) {
		String filename = type + ".tp";
		File typeFile = new File(filename);
		if(typeFile.isFile())
		{
			try (BufferedReader br = new BufferedReader(new FileReader(typeFile))) {
				String text;
				while((text=br.readLine())!=null) {
					StringTokenizer st = new StringTokenizer(text, ":");
					String key = st.nextToken();
					String value = st.nextToken();
					double doubleValue = Double.parseDouble(value);
					typeDatabase.put(key, doubleValue);
				}	
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
