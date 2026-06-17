package views;

import static main.Main.currentUser;
import static main.Main.scanner;
import static main.Main.userManager;

import controllers.AlertManager;
import controllers.LogManager;
import interfaces.Colorable;
import main.Main;

public class Profile
{
	public static void showProfile()
	{
		while (true)
		{
			Main.clearScreen();

			System.out.println(Colorable.RESET
							 + "   _________________________________________________________________________________________________________________________________________________________________ \n"
							 + "  |                                                                                                                                                                 |");
			System.out.println("  |  " + Colorable.YELLOW + "░█▄█░█░█░░░█▀█░█▀▄░█▀█░█▀▀░▀█▀░█░░░█▀▀" + Colorable.RESET + "                                                                                                                         |\n"
							 + "  |  " + Colorable.YELLOW + "░█░█░░█░░░░█▀▀░█▀▄░█░█░█▀▀░░█░░█░░░█▀▀" + Colorable.RESET + "                                                                                                                         |\n"
							 + "  |  " + Colorable.YELLOW + "░▀░▀░░▀░░░░▀░░░▀░▀░▀▀▀░▀░░░▀▀▀░▀▀▀░▀▀▀" + Colorable.RESET + "                                                                                                                         |\n"
							 + "  |_________________________________________________________________________________________________________________________________________________________________|\n");

			System.out.println("   Username:\t\t" + Colorable.YELLOW + currentUser.getUsername() + " (Permanent)" + Colorable.RESET);			
			System.out.println("   First Name:\t\t" + Colorable.YELLOW + currentUser.getFirstName() + Colorable.RESET);
			System.out.println("   Last Name:\t\t" + Colorable.YELLOW + currentUser.getLastName() + Colorable.RESET);
			System.out.println("   Contact Number:\t" + Colorable.YELLOW + currentUser.getContactNum() + Colorable.RESET);
			
			Main.fillUpList(12, 0, "");
			
			System.out.println("\n  " + AlertManager.getAndClearAlert());
			
			System.out.println("\n  Options:");
			System.out.println("  [1] - Update Profile Information");
			System.out.println("  [2] - Change Password");
			System.out.println("  [3] - Back to Dashboard");

			System.out.print("  " + Colorable.BLUE + "[Enter your choice]: " + Colorable.RESET);
			String choice = scanner.nextLine();

			if (choice.equals("1"))
			{
				doUpdateProfile();
			}

			else if (choice.equals("2"))
			{
				doChangePassword();
			}

			else if (choice.equals("3"))
			{
				break;
			}

			else
			{
				AlertManager.setError("Invalid choice.");
			}
		}
	}

	private static void doUpdateProfile()
	{

		System.out.println(Colorable.YELLOW + "\n  [ Update User ]");
		System.out.println(Colorable.YELLOW + "  [NOTE]: Enter 'exit' to cancel operation.\n" + Colorable.RESET);
		System.out.println("  (Press Enter to keep the current value)" + Colorable.RESET);

		System.out.print("  [Enter new First Name (current: " + currentUser.getFirstName() + ")]: " + Colorable.GREEN);
		String newFirstName = scanner.nextLine().trim();

		if (!newFirstName.isEmpty())
		{
			if (newFirstName.equalsIgnoreCase("exit"))
			{
				AlertManager.setError("Operation cancelled.");
				return;
			}
			
			if (newFirstName.matches("[A-Za-z- ]+"))
			{
				currentUser.setFirstName(newFirstName);
			}

			else
			{
				AlertManager.setError("Invalid name format. Skipping update.");
				System.out.println("  " + AlertManager.getAndClearAlert());
			}
		}

		System.out.print(Colorable.RESET + "  [Enter new Last Name (current: " + currentUser.getLastName() + ")]: " + Colorable.GREEN);
		String newLastName = scanner.nextLine().trim();

		if (!newLastName.isEmpty())
		{
			if (newLastName.equalsIgnoreCase("exit"))
			{
				AlertManager.setError("Operation cancelled.");
				return;
			}
			
			if (newLastName.matches("[A-Za-z- ]+"))
			{
				currentUser.setLastName(newLastName);
			}

			else
			{	
				AlertManager.setError("Invalid name format. Skipping update.");
				System.out.println("  " + AlertManager.getAndClearAlert());
			}
		}

		System.out.print(Colorable.RESET + "  [Enter new Contact Number (current: " + currentUser.getContactNum() + ")]: " + Colorable.GREEN);
		String newContact = scanner.nextLine().trim();

		if (!newContact.isEmpty())
		{
			if (newContact.equalsIgnoreCase("exit"))
			{
				AlertManager.setError("Operation cancelled.");
				return;
			}
			
			if (newContact.length() == 11 && newContact.matches("[0-9]+"))
			{
				currentUser.setContactNum(newContact);
			}

			else
			{				
				AlertManager.setError("Invalid contact number format. Skipping update.");
				System.out.println("  " + AlertManager.getAndClearAlert());
			}
		}

		userManager.updateUser(currentUser);
		AlertManager.setSuccess("Profile updated successfully!");
		
		// Log the user's activity
		String logMsg = "Updated Account Information";
		LogManager.log(currentUser.getUsername(), logMsg);
	}

	private static void doChangePassword()
	{
		System.out.println(Colorable.YELLOW + "\n  [Change Password]" + Colorable.RESET);
		System.out.println(Colorable.YELLOW + "  [NOTE]: Enter 'exit' to cancel operation.\n" + Colorable.RESET);
		String oldPassword = Main.validateInput(Colorable.YELLOW + "  [Enter your CURRENT password to verify]: " + Colorable.RESET, "  ");
		if (oldPassword == null) return;

		if (!currentUser.getPassword().equals(oldPassword))
		{
			AlertManager.setError("Incorrect password. Password not changed.");
			return;
		}

		String newPassword;

		while (true)
		{
			System.out.print(Colorable.YELLOW + "\n  [Enter new password (must be at least 8 characters)]: " + Colorable.RESET);
			newPassword = scanner.nextLine().trim();

			if (newPassword.isEmpty())
			{
				AlertManager.setError("Error: Password cannot be empty.");
				System.out.println("  " + AlertManager.getAndClearAlert());
			}
			
			else if (newPassword.equalsIgnoreCase("exit"))
			{
				AlertManager.setError("Operation cancelled.");
				return;
			}

			else if (newPassword.length() < 8)
			{	
				AlertManager.setError("Password must be at least 8 characters long.");
				System.out.println("  " + AlertManager.getAndClearAlert());
			}

			else
			{
				break;
			}
		}

		currentUser.setPassword(newPassword);
		userManager.updateUser(currentUser);

		AlertManager.setSuccess("Password changed successfully!");
		
		// Log the user's activity
		String logMsg = "Changed Account Password";
		LogManager.log(currentUser.getUsername(), logMsg);
	}
}