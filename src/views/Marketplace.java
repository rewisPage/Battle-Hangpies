package views;

import static main.Main.currentUser;
import static main.Main.listingManager;
import static main.Main.productManager;
import static main.Main.scanner;
import static main.Main.userManager;
import static main.Main.purchaseManager;

import java.util.ArrayList;
import java.util.List;
import models.Hangpie;
import models.Listing;
import models.Purchase;
import models.User;
import controllers.AlertManager;
import controllers.LogManager;
import interfaces.Colorable;
import main.Main;

public class Marketplace
{
	public static void showMarketplace()
	{
		while (true)
		{
			Main.clearScreen();
			System.out.println("   _________________________________________________________________________________________________________________________________________________________________ \n"
							 + "  |                                                                                                                                                                 |");
			System.out.println("  |  " + Colorable.YELLOW + "░█▀█░█░░░█▀█░█░█░█▀▀░█▀▄░░░█▄█░█▀█░█▀▄░█░█░█▀▀░▀█▀░█▀█░█░░░█▀█░█▀▀░█▀▀" + Colorable.RESET + "                                                                                         |\n"
							 + "  |  " + Colorable.YELLOW + "░█▀▀░█░░░█▀█░░█░░█▀▀░█▀▄░░░█░█░█▀█░█▀▄░█▀▄░█▀▀░░█░░█▀▀░█░░░█▀█░█░░░█▀▀" + Colorable.RESET + "                                                                                         |\n"
							 + "  |  " + Colorable.YELLOW + "░▀░░░▀▀▀░▀░▀░░▀░░▀▀▀░▀░▀░░░▀░▀░▀░▀░▀░▀░▀░▀░▀▀▀░░▀░░▀░░░▀▀▀░▀░▀░▀▀▀░▀▀▀" + Colorable.RESET + "                                                                                         |\n"
							 + "  |_________________________________________________________________________________________________________________________________________________________________|\n");

			System.out.println("    Here you can buy unique Hangpies from other players. Your Gold Balance: " + Colorable.YELLOW+ currentUser.getGoldBalance() + " G" + Colorable.RESET + "\n");

			// 1. Get the list of listings
			List<Listing> listings = new ArrayList<>(listingManager.getAllListings());
			
			if (listings.isEmpty())
			{
				System.out.println("    The marketplace is currently empty. Check back later!");
			}

			else
			{
				// 2. Display them as a numbered list
				int count = 1;

				for (Listing listing : listings)
				{
					System.out.println("    " + count++ + ". " + listing.toString());
				}
				
			}
			
			Main.fillUpList(19, listingManager.getListingCount(), "");
			
			System.out.println("\n  " + AlertManager.getAndClearAlert());

			System.out.println("\n  [Options]:");
			System.out.println("  [1] - Buy a Hangpie from a Player");
			System.out.println("  [2] - Back to Dashboard");
			System.out.print("  " + Colorable.BLUE + "[Enter your choice]: " + Colorable.RESET);
			String choice = scanner.nextLine();

			if (choice.equals("1"))
			{
				if (listings.isEmpty())
				{
					AlertManager.setError("There are no items to buy.");
				}

				else
				{
					// 3. Pass the list to the "buy" method
					doBuyFromMarketplace(listings);
				}
			}

			else if (choice.equals("2"))
			{
				break;
			}

			else
			{
				AlertManager.setError("Invalid choice.");
			}

		}
	}

	private static void doBuyFromMarketplace(List<Listing> listings)
	{
		System.out.println(Colorable.YELLOW + "  [NOTE]: Enter 'exit' to cancel operation.\n" + Colorable.RESET);
		String inputSellNumber = Main.validateInput("  [Enter the number of the pet you wish to buy] : ", "  ");
		if (inputSellNumber == null) return;
		
		int sellNumber;

		try
		{
			sellNumber = Integer.parseInt(inputSellNumber);

			if (sellNumber < 1 || sellNumber > listings.size())
			{
				AlertManager.setError("Invalid number. Enter a valid number from the list");
				return;
			}			
		}

		catch (NumberFormatException e)
		{
			AlertManager.setError("Invalid input. Please enter a number.");
			return;
		}

		Listing listing = listings.get(sellNumber - 1);
		
		// Confirmation
		System.out.print(Colorable.YELLOW + "  [Are you sure you want to buy " + listing.getPetName() + " for " + listing.getPrice() + "G? (YES / NO)]: " + Colorable.RESET);
		String choice = scanner.nextLine().trim();
		
		if (choice.equalsIgnoreCase("YES"))
		{
			if (listing.getSellerUsername().equals(currentUser.getUsername()))
			{
				AlertManager.setError("You cannot buy your own item.");
				return;
			}

			if (currentUser.getGoldBalance() < listing.getPrice())
			{
				AlertManager.setError("Not enough gold. You need " + listing.getPrice() + " G.");
				return;
			}

			User seller = userManager.getUserByUsername(listing.getSellerUsername());


			if (seller == null)
			{
				AlertManager.setError("CRITICAL ERROR: The seller no longer exists. Cancelling sale.");
				listingManager.removeListing(listing.getUniqueId());
				return;
			}
			
			// Transfer Gold
			currentUser.subtractGold(listing.getPrice());
			seller.addGold(listing.getPrice());

			// "Rebuild" the Hangpie
			Hangpie blueprint = productManager.getProductById(listing.getProductId());

			if (blueprint == null)
			{
				AlertManager.setError("CRITICAL ERROR: The original product blueprint no longer exists. Cancelling.");
				return;
			}

			// Create a new copy
			Hangpie purchasedPet = new Hangpie(blueprint);

			// Pass all data from the listing to the new Hangpie object
			purchasedPet.setUniqueId(listing.getUniqueId());
			purchasedPet.setName(listing.getPetName());
			purchasedPet.setLevel(listing.getPetLevel());
			purchasedPet.setMaxHealth(listing.getPetHealth());
			purchasedPet.setAttackPower(listing.getPetAttack());
			purchasedPet.setCurrentExp(listing.getPetExp());
			purchasedPet.setPrice(listing.getPrice());

			// Add pet to buyer's inventory
			currentUser.addToInventory(purchasedPet);

			// Remove listing from marketplace
			listingManager.removeListing(listing.getUniqueId());

			// Save both users and the listings
			userManager.updateUser(currentUser);
			userManager.updateUser(seller);
			
			// Create purchase record
			Purchase purchaseFromMarketplace = new Purchase(currentUser.getUsername(), purchasedPet.getUniqueId(), purchasedPet.getName(), purchasedPet.getPrice(), false);
			purchaseManager.addPurchase(purchaseFromMarketplace);

			// Update Buyer's Activity Log
			String logMsg = "Bought " + purchasedPet.getName() + " from " + seller.getUsername() + " for " + listing.getPrice() + "G.";
			LogManager.log(currentUser.getUsername(), logMsg);

			// Update Seller's Activity Log
			String sellerLogMsg = "Sold " + purchasedPet.getName() + " to " + currentUser.getUsername() + " for " + listing.getPrice() + "G.";
			LogManager.log(seller.getUsername(), sellerLogMsg);
			
			// Set Success Alert
			AlertManager.setSuccess("Congratulations! You have purchased " + purchasedPet.getName() + " from " + seller.getUsername() + "!");
		}
		
		else if (choice.equalsIgnoreCase("NO"))
		{
			AlertManager.setError("Buy Cancelled.");
		}
		
		else if (choice.equalsIgnoreCase("exit"))
		{
			AlertManager.setError("Buy Cancelled");
			return;
		}
		
		else
		{
			AlertManager.setError("Invalid input. Cancelling.");
		}
	}
}