package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Purchase;

public class PurchaseManager
{
	private String databaseFile = "purchases.txt";

	public void addPurchase(Purchase purchase)
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(databaseFile, true)))
		{
			writer.write(purchase.toFileString());
			writer.newLine();
		}
		catch (IOException e)
		{
			System.err.println("CRITICAL ERROR: Could not save purchase history: " + e.getMessage());
		}
	}

	// Helper to read all purchases
	private List<Purchase> getAllPurchases()
	{
		List<Purchase> allHistory = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(databaseFile)))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				if (line.startsWith("//") || line.trim().isEmpty())
				{
					continue;
				}

				String[] parts = line.split("\\|");

				// Format: username|productId|productName|price|timestamp|isShopPurchase
				if (parts.length < 6)
				{
					continue; 
				}

				Purchase p = new Purchase(
						parts[0],	// username
						parts[1],	// productId
						parts[2],	// productName
						Double.parseDouble(parts[3]),	// pricePaid
						parts[4],	// timestampStr
						Boolean.parseBoolean(parts[5])); // isShopPurchase

				allHistory.add(p);
			}
		}
		catch (IOException | NumberFormatException e)
		{
			System.err.println("Error loading purchase history: " + e.getMessage());
		}

		return allHistory;
	}

	public List<Purchase> getPurchasesForUser(String username)
	{
		List<Purchase> userHistory = new ArrayList<>();
		
		for (Purchase p : getAllPurchases())
		{
			if (p.getUsername().equals(username))
			{
				userHistory.add(p);
			}
		}
		return userHistory;
	}

	public List<String> getTopMostBought()
	{
		List<Purchase> allPurchases = getAllPurchases();

		Map<String, Integer> purchaseCounts = new HashMap<>();
		for (Purchase p : allPurchases)
		{
			String productId = p.getProductId();
			purchaseCounts.put(productId, purchaseCounts.getOrDefault(productId, 0) + 1);
		}

		// Find Top 3 Manually
		String top1_Id = null, top2_Id = null, top3_Id = null;
		int top1_Count = -1, top2_Count = -1, top3_Count = -1;

		for (Map.Entry<String, Integer> entry : purchaseCounts.entrySet())
		{
			String currentId = entry.getKey();
			int currentCount = entry.getValue();

			if (currentCount > top1_Count)
			{
				top3_Id = top2_Id; top3_Count = top2_Count;
				top2_Id = top1_Id; top2_Count = top1_Count;
				top1_Id = currentId; top1_Count = currentCount;
			}
			
			else if (currentCount > top2_Count)
			{
				top3_Id = top2_Id; top3_Count = top2_Count;
				top2_Id = currentId; top2_Count = currentCount;
			}
			
			else if (currentCount > top3_Count)
			{
				top3_Id = currentId; top3_Count = currentCount;
			}
		}

		List<String> top3List = new ArrayList<>();
		if (top1_Id != null) top3List.add(top1_Id);
		if (top2_Id != null) top3List.add(top2_Id);
		if (top3_Id != null) top3List.add(top3_Id);

		return top3List;
	}
	
	public void deletePurchaseHistoryByUser(String username)
	{
		List<Purchase> allPurchases = getAllPurchases();
		List<Purchase> remainingPurchases = new ArrayList<>();
		
		boolean changed = false;
		
		for (Purchase p : allPurchases)
		{
			// Keep the purchase ONLY if it does not belong to the deleted user
			if (!p.getUsername().equals(username))
			{
				remainingPurchases.add(p);
			}
			else
			{
				changed = true;
			}
		}
		
		if (changed)
		{
			// Overwrite the file with the remaining purchases
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(databaseFile)))
			{
				// Format: username|productId|productName|price|timestamp|isShopPurchase
				for (models.Purchase p : remainingPurchases)
				{
					writer.write(p.toFileString());
					writer.newLine();
				}
			}
			catch (IOException e)
			{
				System.err.println("CRITICAL ERROR: Could not update purchase history after user deletion: " + e.getMessage());
			}
			
			System.out.println("Removed purchase history for user: " + username);
		}
	}
}