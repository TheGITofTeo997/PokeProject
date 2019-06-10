package it.unibs.pajc.pokeproject.util;

/**
 * 
 * @author Matteo
 * This class will contain all the strings which are needed to run the client
 *
 */

public class PKClientStrings {
	
	//Logging strings
	public static final String LOGFILE = "clientLog.txt";
	public static final String UTILS_SUCCESFULLY = "\n-Utilities loaded succesfully";
	public static final String GUI_SUCCESFULLY = "-Gui drawn succesfully";
	public static final String MAIN_PANEL_SUCCESFULLY = "-Main panel drawn succesfully";
	public static final String IP_PANEL_SUCCESFULLY = "-Ip panel drawn succesfully";
	public static final String CHOOSER_PANEL_SUCCESFULLY = "-Chooser panel drawn succesfully";
	public static final String BATTLE_PANEL_SUCCESFULLY = "-Battle panel drawn succesfully";
	public static final String ERROR_POPUP_SHOWN ="Error popup shown";
	public static final String MULTI_PLAYER_LISTENERS = "-Added property change listeners for multiplayer";
	public static final String SINGLE_PLAYER_LISTENERS = "-Added property change listeners for singleplayer";
	public static final String CONNECTED_SUCCESFULLY = "-Connected succesfully to server";
	public static final String EXCEPTION_THROWN = "-Exception thrown: ";
	public static final String POKEMON_SENT = "-Chosen pokemon sent succesfully";
	public static final String MOVE_SENT = "-Chosen move sent succesfully";
	public static final String CONNECTION_CLOSED = "-Connection closed by server";
	public static final String CONNECTION_ONLINE = "-Connection tested succesfully";
	public static final String PLAYER_FOUND = "-Server found a player for us";
	public static final String OPPONENT_POKEMON = "-Server sent opponent pokemon";
	public static final String OPPONENT_MOVE = "-Server sent opponent move";
	public static final String OUR_HP = "-Updated our hp after the turn";
	public static final String OPPONENT_HP = "-Updated opponent hp after the turn";
	public static final String OUR_VICTORY = "-Match ended with our victory";
	public static final String REMATCH_ANSWER_YES = "-Player decided to have a rematch";
	public static final String REMATCH_ANSWER_NO = "-Player decided to not have a rematch";
	public static final String OPPONENT_VICTORY = "-Match ended with opponent victory";
	public static final String REMATCH_YES = "-Both player agreed to have a rematch";
	public static final String REMATCH_NO = "-At least one player did not agree to have a rematch";
	public static final String CHECK_MESSAGES = "-Check messages task created";
	public static final String MESSAGE_SENDING_FAILURE = "-Message was not send because of exception: ";
	public static final String RESOURCES_CLOSED = "-Socket and output/input streams closed";
	public static final String SINGLE_PLAYER_POKEMONS = "-Set singleplayer pokemons";
	public static final String PLAYER_HP = "-Updated player hp after the turn";
	public static final String COMPUTER_HP = "-Updated computer hp after the turn";
	public static final String START_BATTLE_SINGLE = "-Started singleplayer battle";
	public static final String PLAYER_MOVE = "-Player selected move for this turn";
	
	//View strings
	public static final String BTN_STARTBATTLE_TEXT = "Start Battle ->";
	public static final String FRAME_TITLE = "PokeBattle Client v0.6b";
	public static final String CONNECTION_ERROR = "Cannot connect to the Server, please check your connection.";
	public static final String WARNING = "Warning";
	public static final String WAITING = "Waiting...";
	public static final String REMATCH_QUESTION_WON = "You won, but do you want to have a rematch?";
	public static final String YOU_WON = "You Won!";
	public static final String YOU_LOST = "You Lost!";
	public static final String REMATCH_TITLE = "Rematch?";
	public static final String REMATCH_QUESTION_LOST = "You lost, but do you want to have a rematch?";

	//Properties
	public static final String CONNECTION_CLOSED_PROPERTY = "connection_closed";
	public static final String CONNECTION_PROPERTY = "connection";
	public static final String PLAYER_FOUND_PROPERTY = "player_fond";
	public static final String OPPONENT_PROPERTY = "opponent";
	public static final String OUR_HP_PROPERTY = "our_hp";
	public static final String OPPONENT_HP_PROPERTY = "opponent_hp";
	public static final String OUR_VICTORY_PROPERTY = "our_victory";
	public static final String OPPONENT_VICTORY_PROPERTY = "opponent_victory";
	public static final String REMATCH_YES_PROPERTY = "rematch_yes";
	public static final String REMATCH_NO_PROPERTY = "rematch_no";
	public static final String START_BATTLE_PROPERTY = "start_battle";
	public static final String COMPLETE_TURN_PLAYER_FIRST_PROPERTY = "complete_player_first";
	public static final String COMPLETE_TURN_COMPUTER_FIRST_PROPERTY = "complete_computer_first";
	public static final String HALF_TURN_PLAYER_FIRST_PROPERTY = "half_player_first";
	public static final String HALF_TURN_COMPUTER_FIRST_PROPERTY = "half_computer_first";
	public static final String PLAYER_VICTORY_PROPERTY = "player_victory";
	public static final String PLAYER_DEFEAT_PROPERTY = "player_defeat";
	public static final String COMPLETE_TURN_US_FIRST_PROPERTY = "complete_us_first";
	public static final String COMPLETE_TURN_OPP_FIRST_PROPERTY = "complete_opponent_first";
	public static final String HALF_TURN_US_FIRST_PROPERTY = "half_us_first";
	public static final String HALF_TURN_OPP_FIRST_PROPERTY = "half_opponent_first";
}
