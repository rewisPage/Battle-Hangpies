package views;

import static main.Main.currentUser;
import static main.Main.scanner;
import static main.Main.userManager;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import controllers.AlertManager;
import controllers.LogManager;
import interfaces.Colorable;
import main.Main;
import models.User;

public class UserManagement implements Colorable
{
	public static void showUserManagement()
	{
		while (true)
		{
			Main.clearScreen();

			System.out.println("\n\n"
					+ "   _________________________________________________________________________________________________________________________________________________________________ \n"
					+ "  |                                                                                                                                                                 |");
			System.out.println("  |  " + Colorable.YELLOW + "░█░█░█▀▀░█▀▀░█▀▄░░░█▄█░█▀█░█▀█░█▀█░█▀▀░█▀▀░█▄█░█▀▀░█▀█░▀█▀" + Colorable.RESET + "                                                                                                     |\n"
					+ "  |  " + Colorable.YELLOW + "░█░█░▀▀█░█▀▀░█▀▄░░░█░█░█▀█░█░█░█▀█░█░█░█▀▀░█░█░█▀▀░█░█░░█░" + Colorable.RESET + "                                                                                                     |\n"
					+ "  |  " + Colorable.YELLOW + "░▀▀▀░▀▀▀░▀▀▀░▀░▀░░░▀░▀░▀░▀░▀░▀░▀░▀░▀▀▀░▀▀▀░▀░▀░▀▀▀░▀░▀░░▀░" + Colorable.RESET + "                                                                                                     |\n"
					+ "  |_________________________________________________________________________________________________________________________________________________________________|\n");

			System.out.println(Colorable.YELLOW + "  [NO]\t[Username]\t\t[Full Name]\t\t[Contact]\t[Status]\t[Role]" + Colorable.RESET);
			System.out.println("  ------------------------------------------------------------------------------------------------------------------------------------------------------------------");

			//GET AND SORT USERS
			List<User> userList = new ArrayList<>(userManager.getAllUsers());

			Collections.sort(userList);

			int count = 1;

			if (userList.isEmpty())
			{
				System.out.println("  No users found in the system.");
			}
			
			else
			{
				for (User user : userList)
				{
					String role = user.isAdmin() ? Colorable.BLUE + "ADMIN" + Colorable.RESET : "USER";
					String status = user.isBanned() ? Colorable.RED + "BANNED" + Colorable.RESET : Colorable.GREEN + "ACTIVE" + Colorable.RESET;

					System.out.printf("  [%d]\t%-15s\t\t%s %s\t\t%s\t%s\t\t\t%s\n", 
							count++, user.getUsername(), user.getFirstName(), user.getLastName(), user.getContactNum(),status, role);
				}
			}

			Main.fillUpList(18, userList.size(), "  ");

			System.out.println("\n  " + AlertManager.getAndClearAlert());

			System.out.println("\n  Options:");
			System.out.println("  [1] - Update User Details/Status");
			System.out.println("  [2] - Delete User");
			System.out.println("  [3] - Back to Admin Dashboard");
			System.out.print(Colorable.BLUE + "  [Enter your choice]: " + Colorable.RESET);

			String choice = scanner.nextLine();

			if (choice.equals("1"))
			{
				doUpdateUser();
			}

			else if (choice.equals("2"))
			{
				doDeleteUser();
			}

			else if (choice.equals("3"))
			{
				break;
			}

			else
			{
				AlertManager.setError("Invalid choice. Please try again.");
			}    
		}
	}

	private static void doUpdateUser()
	{
		//GET AND SORT USERS
		List<User> userList = new ArrayList<>(userManager.getAllUsers());

		Collections.sort(userList);

		System.out.println(Colorable.YELLOW + "\n  [Update User]" + Colorable.RESET);
		System.out.println(Colorable.YELLOW + "  (Press 'exit' to cancel operation)" + Colorable.RESET);
		System.out.print("  Enter the number of the user: ");
		String input = scanner.nextLine().trim();

		if (input.equalsIgnoreCase("exit"))
		{
			return;
		}

		int userNumber;

		try
		{
			userNumber = Integer.parseInt(input);

			if (userNumber < 1 || userNumber > userList.size())
			{
				AlertManager.setError("Invalid number. Enter a valid number. Canceling operation...");
				return;
			}
		}

		catch (NumberFormatException e)
		{
			AlertManager.setError("Invalid input. Please enter a number. Canceling operation...");
			return;
		}

		// LOCATE THE USER BY ITS INDEX
		User user = userList.get(userNumber - 1);

		System.out.println(Colorable.YELLOW + "\n  Updating user: " + user.getUsername() + Colorable.RESET);
		System.out.println(Colorable.YELLOW + "  (Press Enter to keep the current value)\n" + Colorable.RESET);

		// Update Contact
		System.out.print("  Enter new Contact Number (current: " + user.getContactNum() + "): ");
		String newContact = scanner.nextLine().trim();

		if (!newContact.isEmpty())
		{
			if (newContact.equalsIgnoreCase("exit"))
			{
				AlertManager.setError("Update User Cancelled.");
				return;
			}

			else if (newContact.length() == 11 && newContact.matches("[0-9]+"))
			{
				user.setContactNum(newContact);
			}

			else
			{
				AlertManager.setError("Invalid contact number format. Skipping update.");
				System.out.println("  " + AlertManager.getAndClearAlert());
			}
		}

		// Update Admin Status
		System.out.print("  Make this user an admin? (yes/no) (current: " + user.isAdmin() + "): ");
		String adminChoice = scanner.nextLine().trim();

		if (adminChoice.equalsIgnoreCase("yes"))
		{
			user.setAdmin(true);
		}

		else if (adminChoice.equalsIgnoreCase("no"))
		{

			if (user.isAdmin() && user.getUsername().equals(currentUser.getUsername()))
			{
				AlertManager.setError("Error: You cannot remove your own admin status.");
				return;
			}

			else
			{
				user.setAdmin(false);
			}
		}

		else if (adminChoice.equalsIgnoreCase("exit"))
		{
			AlertManager.setError("Update User Cancelled.");
			return;
		}

		else
		{
			AlertManager.setError("Invalid input. Skipping update.");
			System.out.println("  " + AlertManager.getAndClearAlert());
		}

		// Update Banned Status
		System.out.print("  Ban this user? [YES] / [NO] (current: " + user.isBanned() + "): ");
		String banChoice = scanner.nextLine().trim();

		if (banChoice.equalsIgnoreCase("yes"))
		{
			if (user.getUsername().equals(currentUser.getUsername()))
			{
				AlertManager.setError("Error: You cannot ban yourself.");
				return;
			}
			else
			{
				user.setBanned(true);
			}
		}

		else if (banChoice.equalsIgnoreCase("no"))
		{
			user.setBanned(false);
		}

		else if (banChoice.equalsIgnoreCase("exit"))
		{
			AlertManager.setError("Update User Cancelled.");
			return;
		}

		else
		{
			AlertManager.setError("Invalid input. Skipping update.");
			System.out.println("  " + AlertManager.getAndClearAlert());
		}

		userManager.updateUser(user);
		AlertManager.setSuccess("User updated successfully!");

		String logMsg = "Updated user: " + user.getUsername();
		LogManager.log(currentUser.getUsername(), logMsg);
	}

	private static void doDeleteUser()
	{
		//GET AND SORT USERS
		List<User> userList = new ArrayList<>(userManager.getAllUsers());

		Collections.sort(userList);

		System.out.println(Colorable.YELLOW + "\n  [DELETE USER]" + Colorable.RESET);
		System.out.println(Colorable.YELLOW + "  (Press 'exit' to cancel operation)" + Colorable.RESET);
		System.out.print("  Enter the number of the user: ");
		String input = scanner.nextLine().trim();

		if (input.equalsIgnoreCase("exit"))
		{
			return;
		}

		int userNumber;

		try
		{
			userNumber = Integer.parseInt(input);

			if (userNumber < 1 || userNumber > userList.size())
			{
				AlertManager.setError("Invalid number. Enter a valid number. Canceling operation...");
				return;
			}
		}

		catch (NumberFormatException e)
		{
			AlertManager.setError("Invalid input. Please enter a number. Canceling operation...");
			return;
		}

		// LOCATE THE USER BY ITS INDEX
		User user = userList.get(userNumber - 1);
		
		System.out.print(Colorable.RED + "  Are you sure you want to DELETE user '" + user.getUsername() + "'? This cannot be undone. [YES/NO]: " + Colorable.RESET);
		String confirm = scanner.nextLine().trim();
		
		if (confirm.equalsIgnoreCase("yes"))
		{
			int result = userManager.deleteUser(user.getUsername(), currentUser);
			
			String deleteResult = "";

			if (result == 1)
			{
				AlertManager.setSuccess("User '" + user.getUsername() + "' was deleted successfully.");
				deleteResult = Colorable.GREEN + "SUCCESS" + Colorable.RESET;
			}

			else if (result == 0)
			{
				AlertManager.setError("User '" + user.getUsername() + "' not found.");
				deleteResult = Colorable.RED + "FAIL" + Colorable.RESET;
			}

			else if (result == -1)
			{
				AlertManager.setError("You cannot delete your own admin account.");
				deleteResult = Colorable.RED + "FAIL" + Colorable.RESET;
			}

			String logMsg = "Attempted to delete user: " + user.getUsername() + ". Result: " + deleteResult;
			LogManager.log(currentUser.getUsername(), logMsg);
		}
		
		else if (confirm.equalsIgnoreCase("no"))
		{
			AlertManager.setError("Delete cancelled.");
			return;
		}
		
		else if (confirm.equalsIgnoreCase("exit"))
		{
			AlertManager.setError("Delete cancelled.");
			return;
		}
	}
}