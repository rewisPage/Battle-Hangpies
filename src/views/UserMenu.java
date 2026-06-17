package views;

import static main.Main.currentUser;
import static main.Main.scanner;

import java.util.List;

import controllers.AlertManager;
import interfaces.Colorable;
import main.Main;
import models.Hangpie;
import game.GameLauncher; // Import GameLauncher

import static main.Main.purchaseManager;
import static main.Main.productManager;

public class UserMenu
{
	public static void showUserDashboard() throws Exception
	{
		
		while (true)
		{
			// This all prints *once* when the dashboard loads
			Main.clearScreen();
			
			UserMenu.displayNavbar();                                     

			System.out.println("   ___________________________________________________________________________________    __________________________________________________________________________ \n"
							 + "  |                                                                                   |  |                                                                          |\n"
							 + "  |  ABOUT BATTLE HANGPIES:                                                           |  |  " + Colorable.YELLOW + "[EXCLUSIVE] BATTLE HANGPIES IS LIVE NOW!" + Colorable.RESET + "                                |\n"
							 + "  |                                                                                   |  |  The wait is over! Battle Hangpies has finally arrived!                  |\n"
							 + "  |  A word-guessing game where your Hangpie character battles opponents!             |  |__________________________________________________________________________|\n"
							 + "  |  ► Guess letters correctly to damage the enemy.                                   |   __________________________________________________________________________ \n"
							 + "  |  ► Guess wrong, and your Hangpie takes damage!                                    |  |                                                                          |\n"
							 + "  |                                                                                   |  |  " + Colorable.YELLOW + "[UPDATE] Version 1.1.0 - The Marketplace Update" + Colorable.RESET + "                         |\n"
							 + "  |  Join the ultimate battle arena. Collect rare Hangpies, upgrade your arsenal,     |  |  Explore the Marketplace to buy and sell with other players!             |\n"
							 + "  |  and dominate the battlefield in this intense Hangman experience.                 |  |__________________________________________________________________________|\n"
							 + "  |                                                                                   |   __________________________________________________________________________ \n"
							 + "  |  "+ Colorable.GREEN +"░█▀█░█░░░█▀█░█ █░░░█▀█░█▀█░█░█ " + Colorable.RESET + "                                                  |  |                                                                          |\n"
							 + "  |  "+ Colorable.GREEN +"░█▀▀░█░░░█▀█░░█░░░░█░█░█ █░█▄█ - [P]" + Colorable.RESET + "                                             |  |  " + Colorable.YELLOW + "[NEW] New Map: Volcanic Peak!" + Colorable.RESET + "                                           |\n"
							 + "  |  "+ Colorable.GREEN +"░▀░░░▀▀▀░▀░▀░░▀░░░░▀░▀░▀▀▀░▀░▀ " + Colorable.RESET + "                                                  |  |  Explore the treacherous Volcanic Peak map!                              |\n"
							 + "  |___________________________________________________________________________________|  |__________________________________________________________________________|\n"
							 + "   _________________________________________________________________________________________________________________________________________________________________ ");


			// --- Featured Most Bought Requirement ---
			System.out.println(Colorable.YELLOW + "\n     [Featured Most Bought Hangpies in Shop]\n" + Colorable.RESET);

			try
			{
				// 1. Get the simple list of top 3 IDs
				List<String> featuredIds = purchaseManager.getTopMostBought();

				if (featuredIds.isEmpty())
				{
					System.out.print("     No Hangpies have been purchased yet. Be the first!");
				}

				else
				{
					// 2. Display top 3 products
					int count = 1;

					for (String productId : featuredIds)
					{
						Hangpie products = productManager.getProductById(productId);

						if (products != null)
						{	
							System.out.printf(Colorable.GREEN + "     #%d Top Seller:" + Colorable.RESET + " [%s] - %s\n", count++, products.getName(), products.getDescription());
						}
						
					}
				}
				
				Main.fillUpList(3, featuredIds.size(), "");

			}

			catch (Exception e)
			{
				System.err.println("Could not load featured items.");
			}

			System.out.println("   _________________________________________________________________________________________________________________________________________________________________ ");
			
			System.out.println("\n  " + AlertManager.getAndClearAlert() + "\n");
			

			System.out.println(Colorable.YELLOW + "  [SELECTION]" + Colorable.RESET
						   + "\n  [1] - SHOP        [2] - MARKETPLACE          [3] - INVENTORY        [4] - WALLET"
						   + "\n  [5] - PROFILE     [6] - PURCHASE HISTORY     [7] - ACTIVITY LOG     [8] - LOG OUT\n\n"); // LOG OUT is back to [8]
			
			System.out.print( Colorable.YELLOW + "  [Enter your choice]: " + Colorable.RESET);

			String choice = scanner.nextLine();

			if (choice.equals("1"))
			{
				Shop.showShop();
			}
			else if (choice.equals("2"))
			{
				Marketplace.showMarketplace();
			}

			else if (choice.equals("3"))
			{
				Inventory.showInventory();
			}

			else if (choice.equals("4"))
			{
				Wallet.showWalletMenu();
			}

			else if (choice.equals("5"))
			{
				Profile.showProfile();
			}

			else if (choice.equals("6"))
			{
				PurchaseHistory.showPurchaseHistory();
			}

			else if (choice.equals("7"))
			{
				ActivityLog.showUserActivityLog();
			} 
			
			else if (choice.equalsIgnoreCase("P")) // Game Launch mapped to 'P'
			{
				GameLauncher.launchGame(currentUser);
			}

			else if (choice.equals("8")) // Log Out is now [8]
			{
				AlertManager.setSuccess("You have been Logged out.");
				break;
			}

			else
			{
				AlertManager.setError("Invalid choice. Please try again.");
			}
			
			Thread.sleep(16);
		}

	}
	
	public static void displayNavbar()
	{
		System.out.println(Colorable.YELLOW + "  █▀▄ █▀█ ▀█▀ ▀█▀ █   █▀▀   █ █ █▀█ █▀█ █▀▀ █▀█ ▀█▀ █▀▀ █▀▀\n"
											+ "  █▀▄ █▀█  █   █  █   █▀▀   █▀█ █▀█ █ █ █ █ █▀▀  █  █▀▀ ▀▀█\t\t\t\t\t\t\t\t🪙 GOLD: " + currentUser.getGoldBalance() + " G      (👤) " + currentUser.getUsername() + "\n"
											+ "  ▀▀  ▀ ▀  ▀   ▀  ▀▀▀ ▀▀▀   ▀ ▀ ▀ ▀ ▀ ▀ ▀▀▀ ▀   ▀▀▀ ▀▀▀ ▀▀▀" + Colorable.RESET);
		System.out.println("_______________________________________________________________________________________________________________________________________________________________________");
	}
}