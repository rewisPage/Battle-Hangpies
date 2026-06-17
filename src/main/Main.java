package main;

import java.util.Scanner;

import controllers.AlertManager;
import controllers.CodeManager;
import controllers.ListingManager;
import controllers.ProductManager;
import controllers.PurchaseManager;
import controllers.SaveManager; 
import controllers.UserManager;
import interfaces.Colorable;
import models.User;
import views.AdminMenu;
import views.Login;
import views.SignUp;
import views.UserMenu;

// This is the main class
public class Main implements Colorable
{
	// Declare Objects
	public static UserManager userManager;
	public static ProductManager productManager;
	public static CodeManager codeManager;
	public static PurchaseManager purchaseManager;
	public static ListingManager listingManager;
	public static SaveManager saveManager; 

	public static Scanner scanner;
	public static User currentUser;

	public static void main(String[] args) throws Exception
	{

		// Create Objects
		productManager = new ProductManager();
		codeManager = new CodeManager();
		userManager = new UserManager(productManager);
		purchaseManager = new PurchaseManager();
		listingManager = new ListingManager();
		saveManager = new SaveManager(); 
		scanner = new Scanner(System.in);

		while(true)
		{
			Main.clearScreen();

			try
			{
				Thread.sleep(16);
			}

			catch (Exception e)
			{

			}

			currentUser = null;

			Main.showLoginMenu();

			String choice = scanner.nextLine();

			if(choice.equals("1"))
			{
				Login.doLogin();
			}
			else if (choice.equals("2"))
			{
				SignUp.doSignUp();
			}
			else if (choice.equals("3"))
			{
				System.out.println("Thank you for playing Battle Hangpies!");
				scanner.close();
				break;
			}
			else
			{
				AlertManager.setError("Invalid choice. Please enter 1, 2, or 3.");
			}

			if (currentUser != null)
			{
				if (currentUser.isAdmin())
				{
					AdminMenu.showAdminDashboard();
				}
				else
				{
					// Re-enabling isBanned check
					if (currentUser.isBanned())
					{
						AlertManager.setError("This Account is banned.");
					}

					else
					{
						UserMenu.showUserDashboard();
					}
				}
			}
		}
	}

	public static void displayLogo()
	{
		// Single string with all formatting precomputed
		String fullOutput = 
						"\t\t\u001b[38;2;255;255;0m        ██████╗  █████╗ ████████╗████████╗██╗     ███████╗    ██╗  ██╗ █████╗ ███╗   ██╗ ██████╗ ██████╗ ██╗███████╗███████╗    \u001B[0m\n" +
						"\t\t\u001b[38;2;255;234;0m        ██╔══██╗██╔══██╗╚══██╔══╝╚══██╔══╝██║     ██╔════╝    ██║  ██║██╔══██╗████╗  ██║██╔════╝ ██╔══██╗██║██╔════╝██╔════╝    \u001B[0m\n" +
						"\t\t\u001b[38;2;255;213;0m        ██████╔╝███████║   ██║      ██║   ██║     █████╗      ███████║███████║██╔██╗ ██║██║  ███╗██████╔╝██║█████╗  ███████╗    \u001B[0m\n" +
						"\t\t\u001b[38;2;255;191;0m        ██╔══██╗██╔══██║   ██║      ██║   ██║     ██╔══╝      ██╔══██║██╔══██║██║╚██╗██║██║   ██║██╔═══╝ ██║██╔══╝  ╚════██║    \u001B[0m\n" +
						"\t\t\u001b[38;2;255;160;0m        ██████╔╝██║  ██║   ██║      ██║   ███████╗███████╗    ██║  ██║██║  ██║██║ ╚████║╚██████╔╝██║     ██║███████╗███████║    \u001B[0m\n" +
						"\t\t\u001b[38;2;255;120;0m        ╚═════╝ ╚═╝  ╚═╝   ╚═╝      ╚═╝   ╚══════╝╚══════╝    ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝ ╚═════╝ ╚═╝     ╚═╝╚══════╝╚══════╝    \u001B[0m\n";

		System.out.print(fullOutput);
		System.out.println("\n\t\t\t\t\t\t\t    AN INTEGRATED GAMING AND MARKETPLACE SYSTEM");
	}

	private static void showLoginMenu()
	{
		Main.displayLogo();

		System.out.println("\n\n\t\t\t" + Colorable.YELLOW + "[PATCH NOTES:]" + Colorable.RESET);

		System.out.println("\t\t\t __________________________________________________________________________                   ▄▄▄▄▄▄▄▄      ");
		System.out.println("\t\t\t|                                                                          |            █   ▄██████████▄    ");
		System.out.println("\t\t\t|  " + Colorable.YELLOW + "[EXCLUSIVE] BATTLE HANGPIES IS LIVE NOW!" + Colorable.RESET + "                                |           █▐   ████████████    ");
		System.out.println("\t\t\t|  The wait is over! Battle Hangpies has finally arrived!                  |           ▌▐  ██▄▀██████▀▄██   ");
		System.out.println("\t\t\t|__________________________________________________________________________|          ▐ ▐  ██▄▄▄▄██▄▄▄▄██   ");
		System.out.println("\t\t\t __________________________________________________________________________           ▐ ▐  ██████████████   ");
		System.out.println("\t\t\t|                                                                          |          ▐▄▐████ ▀▐▐▀█ █ ▌▐██▄ ");
		System.out.println("\t\t\t|  " + Colorable.YELLOW + "[UPDATE] Version 1.1.0 - The Marketplace Update" + Colorable.RESET + "                         |            █████          ▐███▌");
		System.out.println("\t\t\t|  Explore the Marketplace to buy and sell with other players!             |            █▀▀██▄█ ▄   ▐ ▄███▀ ");
		System.out.println("\t\t\t|__________________________________________________________________________|            █  ███████▄██████   ");
		System.out.println("\t\t\t __________________________________________________________________________                ██████████████   ");
		System.out.println("\t\t\t|                                                                          |               █████████▐▌██▌   ");
		System.out.println("\t\t\t|  " + Colorable.YELLOW + "[NEW] New Map: Volcanic Peak!" + Colorable.RESET + "                                           |               ▐▀▐ ▌▀█▀ ▐ █     ");
		System.out.println("\t\t\t|  Explore the treacherous Volcanic Peak map!                              |                     ▐    ▌     ");
		System.out.println("\t\t\t|__________________________________________________________________________|");


		if (AlertManager.hasAlert())
		{
			System.out.println("\n\t\t\t" + AlertManager.getAndClearAlert());
		}
		else
		{
			System.out.println("\n");
		}

		System.out.println("\n\t\t\t" + Colorable.YELLOW + "[SYSTEM]: Welcome! Please log in or create an account." + Colorable.RESET);
		System.out.println("\n\t\t\t[1] - LOG IN");
		System.out.println("\t\t\t[2] - SIGN UP");
		System.out.println("\t\t\t[3] - EXIT");
		System.out.print("\n\t\t\t" + Colorable.BLUE + "[Enter your choice]: " + Colorable.RESET);
	}

	public static void clearScreen()
	{
		for (int i = 0; i < 50; i++)
		{
			System.out.println();
		}
	}

	public static void fillUpList(int desiredFill, int occupied, String fill)
	{
		int count = desiredFill - occupied;

		if (occupied > desiredFill)
		{
			return;
		}

		else
		{
			for (int i = 0; i < count; i++)
			{
				System.out.println(fill);
			}
		}

	}
	
	public static String validateInput(String message, String gap)
	{
		while (true)
		{
			System.out.print(message);
			String input = scanner.nextLine().trim();

			// Check for cancellation
			if (input.equalsIgnoreCase("exit"))
			{
				return null;
			}

			// Check for empty input
			if (!input.isEmpty())
			{
				return input;
			}

			// Error message
			AlertManager.setError("This field cannot be empty. Please enter a value.");
			System.out.println(gap + AlertManager.getAndClearAlert() + "\n");
		}
	}
}