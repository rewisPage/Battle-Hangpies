package views;

import static main.Main.scanner;
import static main.Main.userManager;
import static main.Main.currentUser;

import models.User;
import controllers.AlertManager;
import controllers.LogManager;
import interfaces.Colorable;
import main.Main;

public class Login implements Colorable {
	public static void doLogin() {
		Main.clearScreen();
		Main.displayLogo();
		System.out.println("\n\n\n\n\n\n\n\n");
		System.out.println("\t\t\t\t\t\t\t\t\t █   █▀█ █▀▀ ▀█▀ █▀█");
		System.out.println("\t\t\t\t\t\t\t\t\t █   █ █ █ █  █  █ █");
		System.out.println("\t\t\t\t\t\t\t\t\t ▀▀▀ ▀▀▀ ▀▀▀ ▀▀▀ ▀ ▀");

		System.out.println("\n\n\n\n\n\n\n\n");
		System.out.println("\n\n\n");
		System.out.print("\t\t\t" + Colorable.BLUE + "[Enter Username]: " + Colorable.RESET);
		String username = scanner.nextLine();

		System.out.print("\t\t\t" + Colorable.BLUE + "[Enter Password]: " + Colorable.RESET);
		String password = scanner.nextLine();

		User user = userManager.login(username, password);

		if (user != null) {
			AlertManager.setSuccess("Login successful! Welcome, " + user.getFirstName());
			currentUser = user;

			// Added to the Activity Log
			LogManager.log(currentUser.getUsername(), "Logged in successfully.");
		} else {
			AlertManager.setError("Login failed. Invalid username or password.");
		}
	}
}