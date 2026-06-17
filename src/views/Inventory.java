package views;

import static main.Main.currentUser;
import static main.Main.scanner;
import static main.Main.userManager;
import static main.Main.listingManager;

import java.util.List;
import models.Hangpie;
import controllers.AlertManager;
import controllers.LogManager;
import interfaces.Colorable;
import main.Main;

public class Inventory
{
	public static void showInventory()
	{
		while (true)
		{
			Main.clearScreen();

			System.out.println("   _________________________________________________________________________________________________________________________________________________________________ \n"
							 + "  |                                                                                                                                                                 |");
			System.out.println("  |  " + Colorable.YELLOW + "░█▄█░█░█░░░▀█▀░█▀█░█░█░█▀▀░█▀█░▀█▀░█▀█░█▀▄░█░█" + Colorable.RESET + "                                                                                                                 |\n"
							 + "  |  " + Colorable.YELLOW + "░█░█░░█░░░░░█░░█░█░▀▄▀░█▀▀░█░█░░█░░█░█░█▀▄░░█░" + Colorable.RESET + "                                                                                                                 |\n"
							 + "  |  " + Colorable.YELLOW + "░▀░▀░░▀░░░░▀▀▀░▀░▀░░▀░░▀▀▀░▀░▀░░▀░░▀▀▀░▀░▀░░▀░" + Colorable.RESET + "                                                                                                                 |\n"
							 + "  |_________________________________________________________________________________________________________________________________________________________________|\n");

			List<Hangpie> inventory = currentUser.getInventory();
			
			int totalCount = 0;
			
			if (inventory.isEmpty())
			{
				System.out.println("    Your inventory is empty. Visit the Marketplace or Shop to buy a Hangpie!");
			}

			else
			{
				int count = 1;
				
				for (Hangpie pet : inventory)
				{
					// We'll use a different toString() for the inventory
					System.out.printf("    %d. [%s] [%s] Lvl:%d (HP:%d, Atk:%d)\n",
							count++,
							pet.getUniqueId().substring(0, 8).toUpperCase(),
							pet.getName(), // The custom name
							pet.getLevel(),
							pet.getMaxHealth(),
							pet.getAttackPower());
					totalCount++;
					
					try
					{
						Thread.sleep(16); // Short pause for effect on list view
					}
					
					catch (Exception e)
					{
						
					}
				}
				
			}
			
			Main.fillUpList(20, totalCount, "");
			
			System.out.println("\n   " + AlertManager.getAndClearAlert());
			
			System.out.println("\n   Options:");
			System.out.println("   [1] - Rename a Hangpie");
			System.out.println("   [2] - Sell to Shop");
			System.out.println("   [3] - List to Marketplace");
			System.out.println("   [4] - Back to Dashboard");
			System.out.print("   " + Colorable.BLUE + "[Enter your choice]: " + Colorable.RESET);

			String choice = scanner.nextLine();

			if (choice.equals("1"))
			{
				doRenameHangpie();
			}

			else if (choice.equals("2"))
			{
				doSellToShop();
			}

			else if (choice.equals("3"))
			{
				doListToMarketplace();
			}

			else if (choice.equals("4"))
			{
				break;
			}

			else
			{
				AlertManager.setError("Invalid choice.");
			}
		}
	}

	private static void doRenameHangpie()
	{
		System.out.println(Colorable.YELLOW + "\n   [Rename Hangpie]" + Colorable.RESET);
		System.out.println(Colorable.YELLOW + "   (Press Enter to keep the current value)\n" + Colorable.RESET);
		List<Hangpie> inventory = currentUser.getInventory();

		if (inventory.isEmpty())
		{
			AlertManager.setError("You have no Hangpies to rename.");
			return;
		}

		String input = Main.validateInput("   Enter the number of the Hangpie to rename (e.g., 1): ", "   ");
		if (input == null) return;

		int petNumber;

		try
		{
			petNumber = Integer.parseInt(input);

			if (petNumber < 1 || petNumber > inventory.size())
			{
				AlertManager.setError("Invalid number. Cancelling operation...");
				return;
			}
		}

		catch (NumberFormatException e)
		{
			AlertManager.setError("Invalid input. Please enter a number. Cancelling operation...");
			return;
		}

		// Get the index of the selected hangpie
		Hangpie petToRename = inventory.get(petNumber - 1);

		String newName = Main.validateInput("   Enter new name for " + petToRename.getName() + ": ", "   ");
		if (newName == null) return;

		// Set new pet name
		petToRename.setName(newName);	
		
		// Update User data
		userManager.updateUser(currentUser);

		// Send Notification
		AlertManager.setSuccess("Your Hangpie has been renamed to " + newName + "!");
		String logMsg = "Renamed hangpie named: " + newName;
		
		// Record Activity
		LogManager.log(currentUser.getUsername(), logMsg);
		
	}

	private static void doSellToShop()
	{
		System.out.println(Colorable.YELLOW + "\n   [Sell Hangpie to Shop]" + Colorable.RESET);
		System.out.println(Colorable.YELLOW + "   [NOTE]: Enter 'exit' to cancel operation.\n" + Colorable.RESET);
		List<Hangpie> inventory = currentUser.getInventory();

		if (inventory.isEmpty())
		{
			AlertManager.setError("You have no Hangpies to sell.");
			return;
		}
		
		String input = Main.validateInput("   Enter the number of the Hangpie to sell (e.g., 1): ", "   ");
		if (input == null) return;

		int petNumber;

		try
		{
			petNumber = Integer.parseInt(input);

			if (petNumber < 1 || petNumber > inventory.size())
			{
				AlertManager.setError("Invalid number. Cancelling operation...");
				return;
			}
		}

		catch (NumberFormatException e)
		{
			AlertManager.setError("Invalid input. Please enter a number. Cancelling operation...");
			return;
		}

		Hangpie petToSell = inventory.get(petNumber - 1);

		// 50 Gold per level plus 10% of its original price
		double sellPrice = petToSell.getLevel() * 50 + (petToSell.getPrice() / 10);
		
		System.out.println(Colorable.YELLOW + "\n   [NOTE]: The Shop will buy items at 10% of its purchase price + 50 Gold per each level");
		System.out.print("   " + Colorable.YELLOW + "Are you sure you want to sell " + petToSell.getName() + " for " + sellPrice + "G? [YES] / [NO]: " + Colorable.RESET);
		String choice = scanner.nextLine().trim();

		if (choice.equalsIgnoreCase("yes"))
		{
			// 1. Add gold to user
			currentUser.addGold(sellPrice);

			// 2. Remove pet from inventory
			currentUser.removeFromInventory(petToSell);

			// 3. Save all changes
			userManager.updateUser(currentUser);

			AlertManager.setSuccess(petToSell.getName() + " has been sold. Your new balance: " + currentUser.getGoldBalance() + "G");

			String logMsg = "Sold " + petToSell.getName() + " to the Shop for " + sellPrice + "G.";
			LogManager.log(currentUser.getUsername(), logMsg);
		}

		else if (choice.equalsIgnoreCase("no"))
		{
			AlertManager.setError("Sale cancelled.");
		}

		else
		{
			AlertManager.setError("Invalid Input. Sale cancelled.");
		}

	}

	private static void doListToMarketplace()
	{
		System.out.println(Colorable.YELLOW + "\n   [List Hangpie for Sale (Marketplace)]" + Colorable.RESET);
		System.out.println(Colorable.YELLOW + "   [NOTE]: Enter 'exit' to cancel operation.\n" + Colorable.RESET);
		List<Hangpie> inventory = currentUser.getInventory();

		if (inventory.isEmpty())
		{
			AlertManager.setError("You have no Hangpies to list.");
			return;
		}

		// 1. Get the pet to sell
		String inputPetNumber = Main.validateInput("   Enter the number of the Hangpie to list (e.g., 1): ", "   ");
		if (inputPetNumber == null) return;

		int petNumber;

		try
		{
			petNumber = Integer.parseInt(inputPetNumber);
			if (petNumber < 1 || petNumber > inventory.size())
			{
				AlertManager.setError("Invalid number. Cancelling operation...");
				return;
			}
		}

		catch (NumberFormatException e)
		{
			AlertManager.setError("Invalid input. Please enter a number. Cancelling operation...");
			return;
		}


		Hangpie petToList = inventory.get(petNumber - 1);

		// 2. Set the desired price
		double sellingPrice;

		try
		{
			String inputSellPrice = Main.validateInput("   Enter selling price for " + petToList.getName() + ": ", "   ");
			if (inputSellPrice == null) return;

			sellingPrice = Double.parseDouble(inputSellPrice);

			if (sellingPrice < 0)
			{
				AlertManager.setError("Price must be positive. Listing cancelled.");
				return;
			}
		}

		catch (NumberFormatException e)
		{
			AlertManager.setError("Invalid input. Please enter a number. Cancelling operation...");
			return;
		}
		
		// Confirmation
		System.out.print(Colorable.YELLOW + "   Are you sure you want to sell " + petToList.getName() + " for " + sellingPrice + " G? [YES] / [NO]: " + Colorable.RESET);
		String confirmation = scanner.nextLine().trim();
		
		if (confirmation.equalsIgnoreCase("YES"))
		{
			// 3. Remove pet from user's inventory
			currentUser.removeFromInventory(petToList);

			// 4. Create the new listing in the ListingManager
			listingManager.createListing(currentUser, petToList, sellingPrice);

			// 5. Save the user's updated inventory
			userManager.updateUser(currentUser);

			// 6. Add new log to the Activity Log
			String logMsg = "Listed " + petToList.getName() + " on the P2P Marketplace for " + sellingPrice + "G.";
			LogManager.log(currentUser.getUsername(), logMsg);
			
			AlertManager.setSuccess(petToList.getName() + " is now listed on the Marketplace for " + sellingPrice + "G!");
		}
		
		else if (confirmation.equalsIgnoreCase("NO"))
		{
			AlertManager.setError("Listing to Marketplace Cancelled.");
		}
		
		else
		{
			AlertManager.setError("Invalid Input. Cancelling.");
		}
	}
}