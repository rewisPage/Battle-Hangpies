package controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogManager
{
	private static String logFile = "activity_log.txt";
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * Writes a new log entry to the activity_log.txt file.
	 * This method is 'static' so we can call it from anywhere.
	 *
	 * @parameter username The user who performed the action.
	 * @parameter message  The action they performed.
	 */
	
	public static void log(String username, String message)
	{
		String timestamp = LocalDateTime.now().format(formatter);
		String logEntry = String.format("[%s] | %s | %s", timestamp, username, message);
		
		// 'true' in FileWriter means "append to file"
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true)))
        {
        	writer.write(logEntry);
        	writer.newLine();
        }
        catch (IOException e)
        {
        	System.err.println("CRITICAL: Failed to write to activity log: " + e.getMessage());
        }
	}
}
