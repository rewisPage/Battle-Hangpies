package interfaces;

// This interface used for coloring the system, embedding it to the String text
public interface Colorable
{
	public static final String RESET = "\u001B[0m";
	public static final String YELLOW = "\u001b[38;2;255;255;0m";
	public static final String BLUE = "\u001B[38;2;0;150;255m";
	public static final String RED = "\u001B[38;2;238;75;43m";
	public static final String GREEN = "\u001B[38;2;144;238;144m";

	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
}