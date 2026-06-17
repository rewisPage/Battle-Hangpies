package views;

import static main.Main.scanner;
import static main.Main.userManager;

import controllers.AlertManager;
import interfaces.Colorable;
import main.Main;
import models.User;

public class SignUp
{
	public static void doSignUp()
	{
		Main.clearScreen();
		Main.displayLogo();
		
		System.out.println("\n\n\n\n\n\n\n\n" + Colorable.GREEN
							+ "\n\t\t\t\t\t\t       █▀▀ █▀▄ █▀▀ █▀█ ▀█▀ █▀▀   █▀█ █▀▀ █▀▀ █▀█ █ █ █▀█ ▀█▀"
							+ "\n\t\t\t\t\t\t       █   █▀▄ █▀▀ █▀█  █  █▀▀   █▀█ █   █   █ █ █ █ █ █  █ "
							+ "\n\t\t\t\t\t\t       ▀▀▀ ▀ ▀ ▀▀▀ ▀ ▀  ▀  ▀▀▀   ▀ ▀ ▀▀▀ ▀▀▀ ▀▀▀ ▀▀▀ ▀ ▀  ▀ "
							+ "\n\n\n\n\n\n\n\n" + Colorable.RESET);
		
		String firstName;
		String lastName;
		String contactNum;
		String username;
		String password;
		
		while (true)
		{
			System.out.println(Colorable.YELLOW + "\t\t\t\t[NOTE]: Enter 'exit' to cancel operation.\n" + Colorable.RESET);
			
			firstName = Main.validateInput("\t\t\t[Enter First Name]: " + Colorable.GREEN, "\t\t\t");
			if (firstName == null) return;

			lastName = Main.validateInput("\t\t\t" + Colorable.RESET + "[Enter Last Name]: " + Colorable.GREEN,"\t\t\t");
			if (lastName == null) return;
			
			if (firstName.isEmpty() || lastName.isEmpty())
			{
				AlertManager.setError("First name and Last name cannot be empty.\n");
				System.out.println("\t\t\t" + AlertManager.getAndClearAlert());
			}
			
			else if (!firstName.matches("[A-Za-z- ]+") || !lastName.matches("^[A-Za-z- ]+"))
			{
				AlertManager.setError("First name and Last name must contain only letters or hypen.\n");
				System.out.println("\t\t\t" + AlertManager.getAndClearAlert());
			}
			
			else
			{
				break;
			}
			
			try
			{
				Thread.sleep(16);
			}
			
			catch (Exception e)
			{
				
			}
		}

		while (true)
		{
			contactNum = Main.validateInput("\t\t\t" + Colorable.RESET + "[Enter Contact Number (must be 11 digits)]: " + Colorable.GREEN, "\t\t\t");
			if (contactNum == null) return;

			if (contactNum.isEmpty())
			{
				AlertManager.setError("Contact number cannot be empty.");
				System.out.println("\t\t\t" + AlertManager.getAndClearAlert());
			}

			else if (!contactNum.matches("[0-9]+"))
			{
				AlertManager.setError("Contact number must contain only numbers.");
				System.out.println("\t\t\t" + AlertManager.getAndClearAlert());
			}

			else if (contactNum.length() != 11)
			{
				AlertManager.setError("Error: Contact number must be exactly 11 digits.");
				System.out.println("\t\t\t" + AlertManager.getAndClearAlert());
			}

			else
			{
				break;
			}
			
			try
			{
				Thread.sleep(16);
			}
			
			catch (Exception e)
			{
				
			}
		}

		while(true)
		{
			System.out.println(Colorable.YELLOW + "\t\t\t\t[NOTE]: The username cannot be changed after the account was created. Think your username thoughtfully.");
			
			username = Main.validateInput("\t\t\t" + Colorable.RESET + "[Enter new username (must be at least 8 characters)]: " + Colorable.GREEN, "\t\t\t");
			if (username == null) return;

			if (username.isEmpty())
			{
				AlertManager.setError("Username cannot be empty.");
				System.out.println("\t\t\t" + AlertManager.getAndClearAlert());
			}
			
			else if (username.length() < 8)
			{
				AlertManager.setError("Error: Username must be at least 8 characters long.");
				System.out.println("\t\t\t" + AlertManager.getAndClearAlert());
			}

			else if (!username.matches("[a-zA-Z0-9_-]+")) 
			{
				AlertManager.setError("Username must contain only alphanumeric characters, underscores, and hyphens");
				System.out.println("\t\t\t" + AlertManager.getAndClearAlert());
			}

			else
			{
				break;
			}
		}

		while (true)
		{
			password = Main.validateInput("\t\t\t" + Colorable.RESET + "[Enter new password (must be at least 8 characters)]: " + Colorable.GREEN, "\t\t\t" );
			if (password == null) return;

			if (password.isEmpty())
			{
				AlertManager.setError("Error: Password cannot be empty.");
				System.out.println("\t\t\t" + AlertManager.getAndClearAlert());
			}

			else if (password.length() < 8)
			{
				AlertManager.setError("Error: Password must be at least 8 characters long.");
				System.out.println("\t\t\t" + AlertManager.getAndClearAlert());
			}

			else
			{
				break;
			}
			
			try
			{
				Thread.sleep(16);
			}
			
			catch (Exception e)
			{
				
			}
		}
		
		System.out.print("\t\t\t" + Colorable.YELLOW + "[SYSTEM]: Create Account? [YES] / [NO]: " + Colorable.RESET);
		String confirmation = scanner.nextLine();
		
		if (confirmation.equalsIgnoreCase("YES"))
		{
			User newUser = new User(username, password, false, firstName, lastName, contactNum, false);

			boolean isCreated = userManager.createAccount(newUser);

			if (isCreated)
			{	
				AlertManager.setSuccess("Account created successfully! You may now log in.");
			}
			
			else
			{
				AlertManager.setError("Username '" + username + "' is already taken. Please Sign Up again.");
			}
		}
		
		else if (confirmation.equalsIgnoreCase("NO"))
		{
			AlertManager.setError("Account Creation cancelled.");
		}
		
		else
		{
			AlertManager.setError("Invalid Input. Account Creation cancelled.");
		}
	}
}