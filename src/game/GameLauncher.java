package game;

import models.User;
import game.ui.GameWindow;
import controllers.AlertManager; // Import the AlertManager for error reporting
import static main.Main.currentUser; // Import currentUser for consistency

public class GameLauncher {

    public static void launchGame(User user) {
        if (user == null) {
            AlertManager.setError("Error: No user logged in.");
            return;
        }
        
        // Use the current user's inventory to check for Hangpies
        if (currentUser.getInventory().isEmpty())
        {
            AlertManager.setError("You cannot enter the Arena without a Hangpie! Please visit the Shop or Marketplace first.");
            return;
        }

        System.out.println("Launching Battle Arena...");
        
        // This launches the graphical game window
        new GameWindow(user);
    }
}