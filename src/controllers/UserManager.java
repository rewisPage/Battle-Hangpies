package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import models.Hangpie;
import models.User;
import static main.Main.listingManager;
import static main.Main.purchaseManager;

public class UserManager
{
	private Map<String, User> userMap;
	private String databaseFile = "users.txt";

	public UserManager(ProductManager productManager)
	{
		this.userMap = new HashMap<>();
		loadUsers(productManager);
	}

	public User login(String username, String password)
	{
		User user = userMap.get(username);

		if (user != null && user.getPassword().equals(password))
		{
			return user;
		}

		return null;
	}

	private void loadUsers(ProductManager productManager)
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

				String[] parts = line.split("\\|");

				if (parts.length < 10)
				{
					continue;
				}

				String username = parts[0];
				String password = parts[1];
				boolean isAdmin = Boolean.parseBoolean(parts[2]);
				String firstName = parts[3];
				String lastName = parts[4];
				String contactNum = parts[5];
				boolean isBanned = Boolean.parseBoolean(parts[6]);

				User user = new User(username, password, isAdmin, firstName, lastName, contactNum, isBanned);

				try
				{
					user.addGold(Double.parseDouble(parts[7]));
					user.setWorldLevel(Integer.parseInt(parts[8]));
					user.setProgressLevel(Integer.parseInt(parts[9]));
				}
				catch (NumberFormatException e)
				{
					System.err.println("[Warning]: Bad data for user " + username);
				}

				userMap.put(user.getUsername(), user);

				loadInventory(user, productManager);
			}
		}
		catch (IOException e)
		{
			System.err.println("Error loading user database: " + e.getMessage());
			System.out.println("Creating default admin account and new users.txt...");
			createDefaultAdmin();
		}

		if (userMap.isEmpty())
		{
			createDefaultAdmin();
		}
	}

	// GETTER
	private void saveUsers()
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(databaseFile)))
		{
			writer.write("// FORMAT: username|password|isAdmin|firstName|lastName|contactNum|isBanned|goldBalance|worldLvl|progressLvl");
			writer.newLine();

			for (User user : userMap.values())
			{
				String line = String.join("|",
						user.getUsername(),
						user.getPassword(),
						String.valueOf(user.isAdmin()),
						user.getFirstName(),
						user.getLastName(),
						user.getContactNum(),
						String.valueOf(user.isBanned()),
						String.valueOf(user.getGoldBalance()),
						String.valueOf(user.getWorldLevel()),
						String.valueOf(user.getProgressLevel()));

				writer.write(line); // Write the user's data
				writer.newLine();   // Add a new line
			}
		}
		catch (IOException e)
		{
			System.err.println("CRITICAL ERROR: Could not save user database: " + e.getMessage());
		}

		saveInventory();
	}

	// SETTER
	private void loadInventory(User user, ProductManager productManager)
	{
		try (BufferedReader reader = new BufferedReader(new FileReader("inventories.txt")))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				if ((line.startsWith("//") || line.trim().isEmpty()))
				{
					continue; // Skip lines that aren't for this user
				}

				String[] parts = line.split("\\|");

				if (parts.length < 5 || !parts[1].equals(user.getUsername()))
				{
					continue;
				}

				try
				{
					String uniqueId = parts[0];
					// parts[1] is username
					String productId = parts[2];
					String customName = parts[3];
					int level = Integer.parseInt(parts[4]);

					// Find the base product from the product manager
					Hangpie product = productManager.getProductById(productId);
					if (product != null)
					{
						// Create a new copy for the inventory
						Hangpie pet = new Hangpie(product);

						// Apply the saved customizations
						pet.setUniqueId(uniqueId);
						pet.setName(customName);
						pet.setLevel(level);

						// Add the loaded pet to the user's inventory list
						user.addToInventory(pet);
					}
				}
				catch (NumberFormatException e)
				{
					System.err.println("Error loading inventory for user " + user.getUsername() + ": " + e.getMessage());
				}
			}
		}
		catch (IOException e)
		{
			System.err.println("Error loading inventory for user " + user.getUsername() + ": " + e.getMessage());
		}
	}

	private void saveInventory()
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("inventories.txt")))
		{
			writer.write("// uniqueId|ownerUsername|productId|customName|level");
			writer.newLine();

			// Loop through every user in our map
			for (User user : userMap.values())
			{

				// Loop through every pet in THAT user's inventory
				for (Hangpie pet : user.getInventory())
				{

					// Create the save string
					String line = String.join("|",
							pet.getUniqueId(), 	// UUID
							user.getUsername(),
							pet.getId(),       // The Product ID (e.g., HP-001)
							pet.getName(),     // The custom name
							String.valueOf(pet.getLevel()));

					writer.write(line);
					writer.newLine();
				}
			}
		}

		catch (IOException e)
		{
			System.err.println("CRITICAL ERROR: Could not save inventories: " + e.getMessage());
		}
	}

	private void createDefaultAdmin()
	{
		User admin = new User("admin", "admin123", true, "Admin", "User", "00000000000", false);
		userMap.put(admin.getUsername(), admin);
		saveUsers();
	}

	// Create Account Validator - checks if the created account is already existing
	public boolean createAccount(User user)
	{
		if (userMap.containsKey(user.getUsername()))
		{
			return false;
		}

		userMap.put(user.getUsername(), user);
		saveUsers();
		return true;
	}

	// Read all registered users
	public Collection<User> getAllUsers()
	{
		return userMap.values();
	}

	// Update user information
	public void updateUser(User user)
	{
		// 'put' will simply overwrite the old entry with the new one
		userMap.put(user.getUsername(), user);
		saveUsers();
	}

	// Delete User
	public int deleteUser(String username, User adminMakingRequest)
	{
		if (username.equals(adminMakingRequest.getUsername()))
		{
			return -1;
		}

		User userToRemove = userMap.remove(username);

		if (userToRemove != null)
		{
			// 1. Remove all their listings from the Marketplace
			listingManager.deleteAllListingsByUser(username);

			// 2. Remove their purchase history
			purchaseManager.deletePurchaseHistoryByUser(username);

			saveUsers();
			saveInventory();
			return 1;
		}

		return 0;
	}

	// Used to search a user
	public User getUserByUsername(String username)
	{
		return userMap.get(username); // O(1) search
	}

	// Get the total count of all registered users
	public int getUserCount()
	{
		return userMap.size();
	}
}