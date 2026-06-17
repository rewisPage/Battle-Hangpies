package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import models.Hangpie;
import models.Listing;
import models.User;

public class ListingManager
{
	private Map<String, Listing> listMap;
	private String databaseFile = "listings.txt";

	public ListingManager()
	{
		this.listMap = new HashMap<>();
		loadListings();
	}

	private void loadListings()
	{
		try (BufferedReader reader = new BufferedReader(new FileReader(databaseFile)))
		{
			String line;

			while ((line = reader.readLine()) != null)
			{
				if (line.startsWith("//") || line.trim().isEmpty())
				{
					continue;
				}

				// Use limit -1 to ensure empty trailing strings are included
				String[] parts = line.split("\\|", -1);

				if (parts.length < 9)
				{
					// Use System.err.println for unrecoverable data corruption warning during loading
					System.err.println("[Warning]: Skipping corrupt listing: " + line);
					continue;
				}

				try
				{
					String uniquePetId = parts[0];
					String sellerUsername = parts[1];
					Double sellerPrice = Double.parseDouble(parts[2]);
					String productId = parts[3];
					String petName = parts[4];
					int petLevel = Integer.parseInt(parts[5]);
					int petHealth = Integer.parseInt(parts[6]);
					int petAttack = Integer.parseInt(parts[7]);
					String description = parts[8];

					// Parse EXP (Index 9), default to 0 if missing (backward compatibility)
					int petExp = 0;
					if (parts.length > 9) {
						petExp = Integer.parseInt(parts[9]);
					}

					Listing listing = new Listing(uniquePetId, sellerUsername, sellerPrice, productId, petName, petLevel, petHealth, petAttack, description, petExp);
					listMap.put(listing.getUniqueId(), listing);
				}

				catch (NumberFormatException e)
				{
					// Use System.err.println for unrecoverable data corruption warning during loading
					System.err.println("Error loading listings: " + e.getMessage());
				}
			}
		}
		catch (IOException e)
		{
			// Inform user of error loading
			System.err.println("Error loading listings: " + e.getMessage());
		}
	}

	private void saveListing()
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(databaseFile)))
		{
			writer.write("// FORMAT: uniquePetId|sellerUsername|price|productId|petName|petLevel|petHealth|petAttack|description|petExp");
			writer.newLine();

			for (Listing listing: listMap.values())
			{
				writer.write(listing.toFileString());
				writer.newLine();
			}
		}

		catch (IOException e)
		{
			// Use System.err.println for critical save failure
			System.err.println("CRITICAL ERROR: Could not save listings: " + e.getMessage());
		}
	}

	// Gets all current P2P listings
	public Collection<Listing> getAllListings()
	{
		return listMap.values();
	}

	// Finds a single listing by the pet's unique ID
	public Listing getListing(String uniqueId)
	{
		return listMap.get(uniqueId);
	}

	// Creates a new listing and saves it
	public void createListing(User seller, Hangpie pet, double price)
	{
		Listing newListing = new Listing(seller.getUsername(), pet, price);
		listMap.put(pet.getUniqueId(), newListing);
		saveListing();
	}

	public void removeListing(String uniquePetId)
	{
		listMap.remove(uniquePetId);
		saveListing();
	}

	public void deleteAllListingsByUser(String username)
	{
		// We use an iterator to safely remove items while looping
		Iterator<Map.Entry<String, Listing>> iterator = listMap.entrySet().iterator();

		boolean changed = false;

		while (iterator.hasNext())
		{
			Map.Entry<String, Listing> entry = iterator.next();
			Listing listing = entry.getValue();

			if (listing.getSellerUsername().equals(username))
			{
				iterator.remove();
				changed = true;
			}
		}

		if (changed)
		{
			saveListing();
			System.out.println("Removed all marketplace listings for user: " + username);
		}
	}

	public int getListingCount()
	{
		return listMap.size();
	}
}