package views;

import static main.Main.currentUser;
import static main.Main.productManager;
import static main.Main.purchaseManager;
import static main.Main.scanner;
import static main.Main.userManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.Hangpie;
import models.Purchase;
import controllers.AlertManager;
import controllers.LogManager;
import interfaces.Colorable;
import main.Main;

public class Shop implements Colorable
{
	public static void showShop()
	{
		while (true)
		{
			Main.clearScreen();
			
			System.out.println("   _________________________________________________________________________________________________________________________________________________________________ \n"
							 + "  |                                                                                                                                                                 |");
			System.out.println("  |  " + Colorable.YELLOW + "░█▀█░█▀▀░█▀▀░▀█▀░█▀▀░▀█▀░█▀█░█░░░░░█▀▀░█░█░█▀█░█▀█" + Colorable.RESET + "                                                                                                             |\n"
							 + "  |  " + Colorable.YELLOW + "░█░█░█▀▀░█▀▀░░█░░█░░░░█░░█▀█░█░░░░░▀▀█░█▀█░█░█░█▀▀" + Colorable.RESET + "                                                                                                             |\n"
							 + "  |  " + Colorable.YELLOW + "░▀▀▀░▀░░░▀░░░▀▀▀░▀▀▀░▀▀▀░▀░▀░▀▀▀░░░▀▀▀░▀░▀░▀▀▀░▀░░" + Colorable.RESET + "                                                                                                             |\n"
							 + "  |_________________________________________________________________________________________________________________________________________________________________|");
			
			System.out.println("\n    Welcome! Browse our available Hangpies. Your balance: " + Colorable.YELLOW + currentUser.getGoldBalance() + "G" + Colorable.RESET + "\n");
			System.out.println(Colorable.YELLOW + "    [NO] [ID]\t\t[NAME]\t\t\t[LEVEL]\t\t\t[STATS]\t\t\t[PRICE]\t\t\t[DESCRIPTION]\n" + Colorable.RESET
					+ "   _________________________________________________________________________________________________________________________________________________________________\n");
			
			// 1. Get the list of B2C products
			List<Hangpie> productList = new ArrayList<>(productManager.getAllProducts());

			// Sort them by ID
			Collections.sort(productList);

			if (productList.isEmpty())
			{
				System.out.println("    The shop is currently empty. Please check back later.");
			}

			else
			{
				// 2. Display them as a numbered list
				int count  = 1;

				for (Hangpie product : productList)
				{
					
					System.out.println("    [" + count++ + "]  " + product.toString());
				}
				
				Main.fillUpList(16, productList.size(), "");

			}
			
			System.out.println("\n  " + AlertManager.getAndClearAlert());

			System.out.println("\n  " + Colorable.YELLOW + "[OPTIONS]:" + Colorable.RESET);
			System.out.println("  [1] - Buy a Hangpie");
			System.out.println("  [2] - Back to Dashboard");
			System.out.print("  " + Colorable.BLUE + "[Enter your choice]: " + Colorable.RESET);

			String choice = scanner.nextLine();

			if (choice.equals("1"))
			{
				// 3. Pass the list to the "buy" method
				doBuyHangpie(productList);
			} 

			else if (choice.equals("2"))
			{
				break;
			}

			else
			{
				AlertManager.setError("Invalid choice.");
			}
			
			try
			{
				Thread.sleep(16);
			}
			
			catch (Exception e)
			{
				
			}
		}
	}

	private static void doBuyHangpie(List<Hangpie> productList)
	{
		// 4. Ask for the NUMBER, not the ID
		System.out.print("  [Enter the number of the Hangpie you wish to buy]: ");
		String inputNumber = scanner.nextLine();
		
		int productNumber;
		
		try
		{
			productNumber = Integer.parseInt(inputNumber);
			
			if (productNumber < 1 || productNumber > productList.size())
			{
				AlertManager.setError("Invalid number. Enter a valid number");
				return;
			}
		}
		
		catch (NumberFormatException e)
		{
			AlertManager.setError("Invalid input. Please enter a number.");
			return;
		}
		
		// 5. Get the selected product from the list
		Hangpie product = productList.get(productNumber - 1);

		// Confirmation
		System.out.print(Colorable.YELLOW + "  [Are you sure you want to buy " + product.getName() + " for " + product.getPrice() + "G? (YES / NO)]: " + Colorable.RESET);
		String choice = scanner.nextLine().trim();
		
		if (choice.equalsIgnoreCase("YES"))
		{
			if (currentUser.getGoldBalance() < product.getPrice())
			{
				AlertManager.setError("Error: Not enough gold. You need " + product.getPrice() + " G.");
				return;
			}

			if (currentUser.subtractGold(product.getPrice()))
			{
				// Create the local copy
				Hangpie ownedPet = new Hangpie(product);
				
				// Add to inventory
				currentUser.addToInventory(ownedPet);
				
				// Update the user data (which saves their inventory)
				userManager.updateUser(currentUser);
				
				// Create purchase record
				Purchase newPurchase = new Purchase(currentUser.getUsername(), ownedPet.getId(), ownedPet.getName(), ownedPet.getPrice(), true);
				purchaseManager.addPurchase(newPurchase);
				
				// Log the activity
				String logMsg = "Bought " + ownedPet.getName() + " from the Shop for " + ownedPet.getPrice() + "G.";
				LogManager.log(currentUser.getUsername(), logMsg);
				
				AlertManager.setSuccess("Congratulations! You have successfully purchased: " + ownedPet.getName() + ", the item is added to the inventory. Your new balance: " + currentUser.getGoldBalance() + " G");
			}
			
			else
			{
				AlertManager.setError("An unknown error occurred during purchase.");
			}
		}
		
		else if (choice.equalsIgnoreCase("NO"))
		{
			AlertManager.setError("Buy Cancelled.");
		}
		
		else
		{
			AlertManager.setError("Invalid input. Cancelling.");
		}
	}
}