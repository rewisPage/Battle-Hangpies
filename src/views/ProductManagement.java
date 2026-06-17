package views;

import static main.Main.productManager;
import static main.Main.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import controllers.AlertManager;
import game.GameConstants;
import interfaces.Colorable;
import main.Main;
import models.Hangpie;

public class ProductManagement implements Colorable
{
	public static void showProductManagement()
	{
		while (true)
		{
			Main.clearScreen();

			System.out.println("\n\n"
							 + "   _________________________________________________________________________________________________________________________________________________________________ \n"
							 + "  |                                                                                                                                                                 |");
			System.out.println("  |  " + Colorable.YELLOW + "░█▀█░█▀▄░█▀█░█▀▄░█░█░█▀▀░▀█▀░░░█▄█░█▀█░█▀█░█▀█░█▀▀░█▀▀░█▄█░█▀▀░█▀█░▀█▀" + Colorable.RESET + "                                                                                         |\n"
						 	 + "  |  " + Colorable.YELLOW + "░█▀▀░█▀▄░█░█░█░█░█░█░█░░░░█░░░░█░█░█▀█░█░█░█▀█░█░█░█▀▀░█░█░█▀▀░█░█░░█░" + Colorable.RESET + "                                                                                         |\n"
						 	 + "  |  " + Colorable.YELLOW + "░▀░░░▀░▀░▀▀▀░▀▀░░▀▀▀░▀▀▀░░▀░░░░▀░▀░▀░▀░▀░▀░▀░▀░▀▀▀░▀▀▀░▀░▀░▀▀▀░▀░▀░░▀░" + Colorable.RESET + "                                                                                         |\n"
						 	 + "  |_________________________________________________________________________________________________________________________________________________________________|\n");

			System.out.println("  [NO.]\t[PODUCT ID]\t[NAME]\t\t\t[LEVEL]\t\t\t[STATS]\t\t\t[PRICE]\t\t\t[DESCRIPTION]");
			System.out.println("  ------------------------------------------------------------------------------------------------------------------------------------------------------------------");

			List<Hangpie> productList = new ArrayList<>(productManager.getAllProducts());

			Collections.sort(productList);
			
			// Display Products
			int count = 1;
			for (Hangpie product : productList)
			{
				System.out.println("  [" + count++ + "]\t" + product.toString());
			}

			Main.fillUpList(18, productList.size(), "");

			System.out.println("\n  " + AlertManager.getAndClearAlert());

			System.out.println("\n  Options:");
			System.out.println("  [1] - Add New Product");
			System.out.println("  [2] - Update Product");
			System.out.println("  [3] - Delete Product");
			System.out.println("  [4] - Back to Admin Dashboard");
			System.out.print(Colorable.BLUE + "  [Enter your choice]: " + Colorable.RESET);

			String choice = scanner.nextLine();

			if (choice.equals("1"))
			{
				doAddProduct();
			}

			else if (choice.equals("2"))
			{
				doUpdateProduct();
			}

			else if (choice.equals("3"))
			{
				doDeleteProduct();
			}

			else if (choice.equals("4"))
			{
				break;
			}

			else
			{
				AlertManager.setError("Invalid choice. Please try again.");
			}
		}
	}
	
	private static String selectImageFolder()
	{
		File folder = new File(GameConstants.HANGPIE_DIR);

		// Safety check: create directory if missing
		if (!folder.exists())
		{
			folder.mkdirs();
		}

		File[] listOfFiles = folder.listFiles();
		List<String> folderNames = new ArrayList<>();

		// Filter to only get directories (folders)
		if (listOfFiles != null)
		{
			for (File file : listOfFiles)
			{
				if (file.isDirectory())
				{
					folderNames.add(file.getName());
				}
			}
		}

		// Sort alphabetically for easier reading
		Collections.sort(folderNames);

		if (folderNames.isEmpty())
		{
			System.out.println(Colorable.RED + "Error: No image folders found in " + GameConstants.HANGPIE_DIR + Colorable.RESET);
			return "default"; // Fallback
		}

		// Display loop
		while (true)
		{
			System.out.println(Colorable.YELLOW + "\n  [Select Image Folder]" + Colorable.RESET);
			int count = 1;
			for (String name : folderNames)
			{
				System.out.println("  [" + count++ + "] " + name);
			}

			System.out.print("  Enter the number of the image folder: ");
			String input = scanner.nextLine().trim();
			
			if (input.equalsIgnoreCase("exit"))
			{
				AlertManager.setError("Add Product Cancelled.");
				return null;
			}

			try
			{
				int index = Integer.parseInt(input);
				if (index >= 1 && index <= folderNames.size())
				{
					// Return the chosen folder name
					return folderNames.get(index - 1);
				}
				
				else
				{
					System.out.println(Colorable.RED + "  [Error]: Invalid number. Please try again." + Colorable.RESET);
				}
			}
			
			catch (NumberFormatException e)
			{
				System.out.println(Colorable.RED + "  [Error]: Invalid input. Please enter a number." + Colorable.RESET);
			}
		}
	}

	private static void doAddProduct()
	{
		System.out.println(Colorable.YELLOW + "\n  [Add New Product]" + Colorable.RESET);
		try
		{
			System.out.println(Colorable.YELLOW + "  [NOTE]: Enter 'exit' to cancel operation." + Colorable.RESET);
			String id = Main.validateInput("  Enter Product ID (e.g., HP-004): ", "  ");
			if (id == null) return;

			String name = Main.validateInput("  Enter Name: ", "  ");
			if (name == null) return;

			String description = Main.validateInput("  Enter Description: ", "  ");
			if (description == null) return;
			
			String imageName = selectImageFolder();
			if (imageName == null) return;
			System.out.println(Colorable.GREEN + "  [Selected Image]: " + imageName + Colorable.RESET);

			String inputPrice = Main.validateInput("  Enter Price (e.g., 300): ", "  ");
			if (inputPrice == null) return;
			double price = Double.parseDouble(inputPrice);

			String inputMaxHealth = Main.validateInput("  Enter Max Health (e.g., 60): ", "  ");
			if (inputMaxHealth == null) return;
			int maxHealth = Integer.parseInt(inputMaxHealth);

			String inputLevel = Main.validateInput("  Enter Starting Level (e.g., 1): ", "  ");
			if (inputLevel == null) return;
			int level = Integer.parseInt(inputLevel);
			
			String inputAtk = Main.validateInput("  Enter Attack Power (e.g., 10): ", "  ");
			if (inputAtk == null) return;
			int attackPower = Integer.parseInt(inputAtk);

			Hangpie newProduct = new Hangpie(id, name, description, price, maxHealth, level, attackPower, imageName);

			if (productManager.addProduct(newProduct))
			{
				AlertManager.setSuccess("Product added successfully!");
			}
			
			else
			{
				AlertManager.setError("A product with ID '" + id + "' already exists.");
			}
		}
		
		catch (NumberFormatException e)
		{
			AlertManager.setError("Invalid number. Price, Health, Level, and Attack must be numbers.");
		}
	}

	private static void doUpdateProduct()
	{
		List<Hangpie> productList = new ArrayList<>(productManager.getAllProducts());
		Collections.sort(productList);
		
		System.out.println(Colorable.YELLOW + "\n  [Update Product]" + Colorable.RESET);
		
		System.out.print("  Enter the number of the product (or 'exit' to cancel): ");
		String input = scanner.nextLine().trim();

		if (input.equalsIgnoreCase("exit"))
		{
			return;
		}
		
		int productNumber;
		
		try
		{
			productNumber = Integer.parseInt(input);
			
			if (productNumber < 1 || productNumber > productList.size())
			{
				AlertManager.setError("Invalid number. Enter a valid number. Canceling operation...");
				return;
			}
		}
		
		catch (NumberFormatException e)
		{
			AlertManager.setError("Invalid input. Please enter a number.");
			return;
		}
		
		Hangpie product = productList.get(productNumber - 1);

		System.out.println(Colorable.YELLOW + "\n  [Updating product]: " + product.getName() + Colorable.RESET);
		System.out.println(Colorable.YELLOW + "  (Press Enter to keep the current value, enter 'exit' to cancel operation)\n" + Colorable.RESET);

		try
		{
			System.out.print("  Enter new Name (current: " + product.getName() + "): ");
			String newName = scanner.nextLine().trim();

			if (!newName.isEmpty())
			{
				if (newName.equalsIgnoreCase("exit"))
				{
					AlertManager.setError("Update Product cancelled.");
					return;
				}
				
				product.setName(newName);
			}

			System.out.print("  Enter new Description (current: " + product.getDescription() + "): ");
			String newDesc = scanner.nextLine().trim();

			if (!newDesc.isEmpty())
			{
				if (newDesc.equalsIgnoreCase("exit"))
				{
					AlertManager.setError("Update Product cancelled.");
					return;
				}
				
				product.setDescription(newDesc);
			}
			
			System.out.println("  Current Image Folder: " + product.getImageName());
			System.out.print("  [Do you want to change the image? [YES] / [NO])]: ");
			String changeImg = scanner.nextLine().trim();
			
			if (changeImg.equalsIgnoreCase("yes"))
			{
				String newImageName = selectImageFolder();
				if (newImageName != null)
				{
					product.setImageName(newImageName);
					System.out.println(Colorable.GREEN + "  [Image updated to]: " + newImageName + Colorable.RESET);
				}
			}
			else if (changeImg.equalsIgnoreCase("no"))
			{
				System.out.println(Colorable.GREEN + "  [Keep Current Image]" + Colorable.RESET);
			}
			else if (changeImg.equalsIgnoreCase("exit"))
			{
				AlertManager.setError("Update Product cancelled.");
				return;
			}

			System.out.print("  Enter new Price (current: " + product.getPrice() + "): ");
			String newPrice = scanner.nextLine().trim();

			if (!newPrice.isEmpty())
			{
				if (newPrice.equalsIgnoreCase("exit"))
				{
					AlertManager.setError("Update Product cancelled.");
					return;
				}
				
				product.setPrice(Double.parseDouble(newPrice));
			}

			System.out.print("  Enter new Max Health (current: " + product.getMaxHealth() + "): ");
			String newMaxHealth = scanner.nextLine().trim();

			if (!newMaxHealth.isEmpty())
			{
				if (newMaxHealth.equalsIgnoreCase("exit"))
				{
					AlertManager.setError("Update Product cancelled.");
					return;
				}
				
				product.setMaxHealth(Integer.parseInt(newMaxHealth));
			}
			
			System.out.print("  Enter new Attack Power (current: " + product.getAttackPower() + "): ");
			String newAtkPower = scanner.nextLine();

			if (!newAtkPower.isEmpty())
			{
				if (newAtkPower.equalsIgnoreCase("exit"))
				{
					AlertManager.setError("Update Product cancelled.");
					return;
				}
				
				product.setAttackPower(Integer.parseInt(newAtkPower));
			}

			productManager.updateProduct(product);
			AlertManager.setSuccess("Product updated successfully!");

		}
		
		catch (NumberFormatException e)
		{
			AlertManager.setError("Invalid number. Price, Health, Level, and Attack must be numbers. Cancel Operation...");
		}

	}

	private static void doDeleteProduct()
	{
		List<Hangpie> productList = new ArrayList<>(productManager.getAllProducts());
		Collections.sort(productList);
		
		System.out.println(Colorable.YELLOW + "\n  [Delete Product]" + Colorable.RESET);
		
		System.out.print("  Enter the number of the product (or 'exit' to cancel): ");
		String input = scanner.nextLine().trim();

		if (input.equalsIgnoreCase("exit"))
		{
			AlertManager.setError("Delete Product cancelled.");
			return;
		}
		
		int productNumber;
		
		try
		{
			productNumber = Integer.parseInt(input);
			
			if (productNumber < 1 || productNumber > productList.size())
			{
				AlertManager.setError("Invalid number. Enter a valid number. Canceling operation...");
				return;
			}
		}
		
		catch (NumberFormatException e)
		{
			AlertManager.setError("Invalid input. Please enter a number.");
			return;
		}
		
		Hangpie product = productList.get(productNumber - 1);
		
		System.out.print(Colorable.YELLOW + "  Delete Product named '" + product.getName() +"'? [YES] / [NO]: " + Colorable.RESET);
		String choice = scanner.nextLine().trim();
		
		if (choice.equalsIgnoreCase("yes"))
		{
			
			if (productManager.deleteProduct(product.getId()))
			{
				AlertManager.setSuccess("Product '" + product.getId() + "' deleted successfully.");
			}
			
			else
			{
				AlertManager.setError("Product ID '" + product.getId() + "' not found.");
			}
		}
		else if (choice.equalsIgnoreCase("no"))
		{
			AlertManager.setError("Cancelling operation...");
		}

	}
}