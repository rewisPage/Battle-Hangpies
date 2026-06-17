package views;

import static main.Main.currentUser;
import static main.Main.purchaseManager;
import static main.Main.scanner;

import java.util.ArrayList;
import java.util.List;

import controllers.AlertManager;
import interfaces.Colorable;
import main.Main;
import models.Purchase;

public class PurchaseHistory
{
	public static void showPurchaseHistory()
	{
		while(true) {
			Main.clearScreen();
			PurchaseHistory.displayTitle();

			System.out.println("  " + AlertManager.getAndClearAlert());

			System.out.println("\n  [View Options]:");
			System.out.println("  [1] - View All Purchases");
			System.out.println("  [2] - View Shop Purchases Only (B2C)");
			System.out.println("  [3] - View Marketplace Purchases Only (P2P)");
			System.out.println("  [4] - Back to Dashboard");
			System.out.print("\n  " + Colorable.BLUE + "[Enter your choice]: " + Colorable.RESET);

			String choice = scanner.nextLine();

			if (choice.equals("1"))
			{
				displayList(purchaseManager.getPurchasesForUser(currentUser.getUsername()));
			}
			
			else if (choice.equals("2"))
			{
				// Filter for Shop (true)
				displayList(filterPurchases(true));
			}
			
			else if (choice.equals("3"))
			{
				// Filter for Marketplace (false)
				displayList(filterPurchases(false));
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

	// Helper to filter the list
	private static List<Purchase> filterPurchases(boolean isShop)
	{
		List<Purchase> all = purchaseManager.getPurchasesForUser(currentUser.getUsername());
		List<Purchase> filtered = new ArrayList<>();

		for (Purchase p : all)
		{
			if (p.isShopPurchase() == isShop)
			{
				filtered.add(p);
			}
		}
		
		Main.fillUpList(28, filtered.size(), "");
		
		return filtered;
	}

	// Helper to display the list
	private static void displayList(List<Purchase> history)
	{
		Main.clearScreen();
		PurchaseHistory.displayTitle();
		
		if (history.isEmpty())
		{
			System.out.println("\n    No records found for this category.");
		}
		
		else
		{
			// Loop in reverse to show newest first
			for (int i = history.size() - 1; i >= 0; i--)
			{
				System.out.println("    ◉ " + history.get(i).toString());
			}
		}
		
		Main.fillUpList(28, history.size(), "");

		System.out.print("\n    (Press Enter to return to filter menu)");
		scanner.nextLine();
	}
	
	private static void displayTitle()
	{
		System.out.println("\n"
				+ "   _________________________________________________________________________________________________________________________________________________________________ \n"
				+ "  |                                                                                                                                                                 |\n"
				+ "  |  " + Colorable.YELLOW + "░█▄█░█░█░░░█▀█░█░█░█▀▄░█▀▀░█░█░█▀█░█▀▀░█▀▀░░░█░█░▀█▀░█▀▀░▀█▀░█▀█░█▀▄░█░█" + Colorable.RESET + "                                                                                       |\n"
				+ "  |  " + Colorable.YELLOW + "░█░█░░█░░░░█▀▀░█░█░█▀▄░█░░░█▀█░█▀█░▀▀█░█▀▀░░░█▀█░░█░░▀▀█░░█░░█░█░█▀▄░░█░" + Colorable.RESET + "                                                                                       |\n"
				+ "  |  " + Colorable.YELLOW + "░▀░▀░░▀░░░░▀░░░▀▀▀░▀░▀░▀▀▀░▀░▀░▀░▀░▀▀▀░▀▀▀░░░▀░▀░▀▀▀░▀▀▀░░▀░░▀▀▀░▀░▀░░▀░" + Colorable.RESET + "                                                                                       |\n"
				+ "  |_________________________________________________________________________________________________________________________________________________________________|\n");
	}
}