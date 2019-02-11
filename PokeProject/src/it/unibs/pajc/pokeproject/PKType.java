package it.unibs.pajc.pokeproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

public class PKType {
	// hashmap contenente le associazioni tra il tipo considerato e l'efficacia sugli altri tipi
	private HashMap<String, Double> typeDatabase = new HashMap<>();
	private String type;
	
	public PKType(String type) {
		this.type = type;
	}
	
	public double getEffectiveness(PKType type) {
		return typeDatabase.get(type);
	}
	
	public String getTypeName() {
		return this.type;
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
