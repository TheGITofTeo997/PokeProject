package it.unibs.pajc.pokeproject;
import java.io.*;
import java.util.*;

public class Pokemon {
	private String name;
	private String type;
	private PKMove[] moves = new PKMove[4];
	public HashMap<String, Integer> stats;
	
	
	
	public Pokemon(String name,  String type) {
		this.name = name;
		this.type = type;
		this.stats = fillStats(stats, name);
	}
	
	private HashMap<String, Integer> fillStats(HashMap<String, Integer> stats, String name) {
		String filename = name + ".txt";
		File poke = new File(filename);
		if(poke.isFile())
		{
			BufferedReader br=null;
			try {
				br = new BufferedReader(new FileReader(poke));
				String text;
				while((text=br.readLine())!=null) {
					StringTokenizer st = new StringTokenizer(text, ":");
					String key = st.nextToken();
					String value = st.nextToken();
					int intvalue = Integer.parseInt(value);
					stats.put(key, intvalue);
				}	
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			finally {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return stats;		
	}	
}
