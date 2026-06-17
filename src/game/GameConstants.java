package game;

import java.awt.Color;
import java.awt.Font;

// This class holds all the fixed numbers, texts, colors, and file paths used throughout the game
public class GameConstants {
	// Resolution (Landscape)
	public static final int WINDOW_WIDTH = 1280; // The width of the game window in pixels
	public static final int WINDOW_HEIGHT = 720; // The height of the game window in pixels

	// Texts
	public static final String GAME_TITLE = "Battle Hangpies - Arena";
	public static final String SUBTITLE_TEXT = "AN INTEGRATED GAMING AND MARKETPLACE SYSTEM"; 
	public static final String INSTRUCTION_TITLE = "HOW TO PLAY";
	public static final String INSTRUCTION_TEXT = "Type the letters on your keyboard to guess the word.\n\n"
			+ "Correct Guess:\n" + "Your Hangpie attacks! Damage dealt increases with each correct letter.\n\n"
			+ "Wrong Guess / Time Out:\n" + "Your Hangpie takes damage!\n\n" + "Objectives:\n"
			+ "Defeat enemy to advance stage. Clear the word to get a new one.\n"
			+ "Win the World Boss (Stage 5) to advance world level and gain better rewards!"; // The full text for the instruction modal

	// Colors
	public static final Color PRIMARY_COLOR = new Color(41, 128, 185);
	public static final Color ACCENT_COLOR = new Color(231, 76, 60);
	public static final Color BACKGROUND_COLOR = new Color(30, 30, 30);
	public static final Color TEXT_COLOR = Color.WHITE;
	public static final Color SELECTION_COLOR = Color.YELLOW; // Color used for highlighted text or buttons
	public static final Color TIMER_PANIC_COLOR = new Color(231, 76, 60); // Red color for when the timer is low

	// Game Logic Timer
	public static final int GUESS_TIME_LIMIT_SECONDS = 30; // How long the player has to guess a letter
	public static final int PANIC_THRESHOLD_SECONDS = 5; // When the timer starts flashing red
	public static final int ALL_IN_GUESS_TIME_SECONDS = 10; // Time limit for the all-in guess

	// Fonts
	public static final Font HEADER_FONT = new Font("Monospaced", Font.BOLD, 40);
	public static final Font UI_FONT = new Font("Monospaced", Font.BOLD, 24);
	public static final Font BUTTON_FONT = new Font("Monospaced", Font.BOLD, 20);
	public static final Font SMALLER_BUTTON_FONT = new Font("Monospaced", Font.BOLD, 18);
	public static final Font SUBTITLE_FONT = new Font("Monospaced", Font.BOLD, 18);
	public static final Font LIST_FONT = new Font("Monospaced", Font.PLAIN, 20);
	public static final Font INSTRUCTION_FONT = new Font("Monospaced", Font.BOLD, 16);

	// Paths
	public static final String ASSET_DIR = "images/"; // Base folder for all game graphics
	public static final String HANGPIE_DIR = ASSET_DIR + "hangpies/";
	public static final String ENEMY_DIR = ASSET_DIR + "enemies/";
	public static final String BG_DIR = ASSET_DIR + "bg/";
	public static final String GUI_DIR = ASSET_DIR + "utilities/gui/";
	public static final String CHAR_UTIL_DIR = ASSET_DIR + "utilities/characters/";

	// Assets - GUI
	public static final String MAIN_BG = "game_interface.gif";
	public static final String TITLE_IMG = "title.png";
	public static final String TITLE_COVER_IMG = "titlecover.png";
	public static final String INVENTORY_BG = "inventory_bg.gif";
	
	// Assets - Frame
	public static final String FRAME_IMG = GUI_DIR + "frame.png";
	public static final String NAME_FRAME_IMG = GUI_DIR + "nameframe.png";
	public static final String SETTINGS_BTN_IMG = GUI_DIR + "settings.png";
	public static final String MODAL_IMG = GUI_DIR + "modal.png";
	public static final String RABBIT_IMG = GUI_DIR + "rabbit.png";
	
	// Assets - Stats
	public static final String HEART_IMG = CHAR_UTIL_DIR + "heart.png";
	public static final String EMPTY_HEART_IMG = CHAR_UTIL_DIR + "empty_heart.png";
	public static final String COIN_IMG = CHAR_UTIL_DIR + "coin.png";
	public static final String ATTACK_IMG = CHAR_UTIL_DIR + "attack.png";
}