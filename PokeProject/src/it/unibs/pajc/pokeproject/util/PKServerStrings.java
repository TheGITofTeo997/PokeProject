package it.unibs.pajc.pokeproject.util;

/**
 * 
 * @author Matteo
 * This class will contain all the strings which are needed to run the server
 *
 */
public class PKServerStrings {
	
	//Logging strings for server controller
	public static final String FROM = "from: ";
	public static final String SERVER_RECEIVED = "\nServer received ";
	public static final String LOGFILE = "serverLog.txt";
	public static final String GUI_SUCCESFULLY = "\n-Gui drawn succesfully";
	public static final String SERVER_STARTED_SUCCESFULLY = "\n-[OK]Server started on port 50000...";
	public static final String LOADED_PK_TREEMAP_SUCCESFULLY = "\n-[OK]Loaded PK treemap...";
	public static final String PK_TREEMAP_LOADING_FAILURE = "\n-[!]Error while loading PK treemap!";
	public static final String LOADED_TYPE_ARRAYLIST_SUCCESFULLY = "-[OK]Loaded PKType arraylist...";
	public static final String TYPE_ARRAYLIST_LOADING_FAILURE = "\n-[!]Error while loading PKType arraylist!";
	public static final String SERVING_CLIENT_STRING = "\n-[i]Serving client with address ";
	public static final String MSG_ADDED_CORRECTLY = "\n-[i]Message added to the queue correctly. Proceeding...";
	public static final String CLIENT_CONNECTED = "\n-A client has connected to the server";
	public static final String CLIENT_REQUEST_HANDLED = "\n-The client request is being handled";
	public static final String CLIENT_REQUEST_HANDLED_OK = "\n-Client request handled succesfully";
	public static final String MESSAGE_SEND_FAIL = "\n-Failure while sending message to ";
	public static final String CONNECTED_CLIENTS = "\n-Number of connected clients: ";
	public static final String TASKS_STOPPED = "-Executor services for match and player execution stopped";
	
	//Logging strings for match thread
	public static final String MATCH_LOG_FILE = "matchLog.txt";
	public static final String MATCH_CHECK_MESSAGES = "-Check messages task created in match thread";
	public static final String SENT_PLAYER_FOUND_MESSAGES = "\n-Notified both clients that server found them an opponent";
	public static final String SELECTED_POKEMON_FROM_ONE = "-Received selected pokemon from player one";
	public static final String SELECTED_POKEMON_FROM_TWO = "-Received selected pokemon from player two";
	public static final String OPPONENT_POKEMON_MESSAGES = "-Notified both clients about the pokemon chosen by the ohter ";
	public static final String SELECTED_MOVE_FROM_ONE = "-Received move selected by player one";
	public static final String SELECTED_MOVE_FROM_TWO = "-Received move selected by player two";
	public static final String TURN_DONE = "-Turn damage calculation and messages sending done";
	public static final String SECOND_ATTACKER_DEAD = "-Second attacker is dead. Pokemon name: ";
	public static final String FIRST_ATTACKER_DEAD = "-First attacker is dead. Pokemon name: ";
	public static final String MATCH_CONNECTION_CLOSED = "-Match connection closed by server";
	public static final String REMATCH_MESSAGES_SENT = "-Rematch messages sent to both player";
	public static final String PLAYER_TWO_DID_NOT_AGREE_REMATCH = "-Player two did not agree to rematch";
	public static final String PLAYER_TWO_AGREED_REMATCH = "-Player two agreed to rematch";
	public static final String PLAYER_ONE_DID_NOT_AGREE_REMATCH = "-Player one did not agree to rematch";
	public static final String PLAYER_ONE_AGREED_REMATCH = "-Player one agreed to rematch";
	public static final String FIRST_ATTACKER_PLAYER_ONE = "-Player one is the first to attack";
	public static final String FIRST_ATTACKER_PLAYER_TWO = "-Player two is the first to attack";
	
	//View strings
	public static final String FRAME_TITLE = "PokeServer v0.5.1";
	public static final String BTN_START_TXT = "Start Server ->";
	public static final String BTN_STOP_TXT = "Stop Server ->";
	public static final String CONSOLE_LABEL = "Console";
}
