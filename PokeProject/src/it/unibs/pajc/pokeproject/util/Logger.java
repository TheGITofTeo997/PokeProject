package it.unibs.pajc.pokeproject.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

/*
 * @author Patrick
 * The purpose of this class is to save to a txt file the various operation made during the test/execution
 */
public class Logger {

	private File logFile;
	private BufferedWriter writer;
	
	public Logger(String logFileDirectory) {
		logFile = new File(logFileDirectory);
		
		try {
			writer = new BufferedWriter(new FileWriter(logFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Date date = new Date();
		writeLog("Logfile v0.2");
        writeLog("Application started on: " + new Timestamp(date.getTime()).toString());
        writeLog("OS information: " + System.getProperty("os.arch") + ", " + System.getProperty("os.name") + ", " + System.getProperty("os.version"));
	}
	
	public void writeLog(String text) {
		try {
			writer.write(text + '\n');
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeLogger() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
