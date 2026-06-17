package controllers;

import interfaces.Colorable;

// This class servers as the notifier of the system, producing Success and Error Alerts used to notify Users
public class AlertManager implements Colorable {
	private static String currentAlert = "";
	private static String alertColor = Colorable.RESET;

	// Sets a success message (Green).
	public static void setSuccess(String message) {
		currentAlert = message;
		alertColor = Colorable.GREEN;
	}

	// Sets an error message (Red).
	public static void setError(String message) {
		currentAlert = message;
		alertColor = Colorable.RED;
	}

	// Checks if there is an alert waiting to be displayed.
	public static boolean hasAlert() {
		return !currentAlert.isEmpty();
	}

	// Returns the formatted alert string and CLEARS the alert.
	// This ensures the alert only shows once.
	public static String getAndClearAlert() {

		if (currentAlert.isEmpty()) {
			return "";
		}

		String formattedAlert = alertColor + "【ALERT】: " + currentAlert + RESET;

		// Clear the alert after retrieving it
		currentAlert = "";
		alertColor = RESET;

		return formattedAlert;
	}
}