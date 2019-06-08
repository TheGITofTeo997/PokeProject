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

	private static final String OS_VERSION = "os.version";
	private static final String OS_NAME = "os.name";
	private static final String OS_ARCH = "os.arch";
	private static final String OS_INFORMATION = "OS information: ";
	private static final String APPLICATION_STARTED_ON = "Application started on: ";
	private static final String LOGFILE_TITLE = "Logfile v1.0";
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
		writeLog(LOGFILE_TITLE);
        writeLog(APPLICATION_STARTED_ON + new Timestamp(date.getTime()).toString());
        writeLog(OS_INFORMATION + System.getProperty(OS_ARCH) + ", " + System.getProperty(OS_NAME) + ", " + System.getProperty(OS_VERSION));
	}
	
	public synchronized void writeLog(String text) {
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
