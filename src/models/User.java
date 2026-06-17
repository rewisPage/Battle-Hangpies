package models;

import java.util.ArrayList;
import java.util.List;

public class User implements Comparable<User>
{
	// User's username
	private String username;

	// User's password
	private String password;

	// Used to define if the user is Admin
	private boolean isAdmin;

	// Used to define if the user is banned
	private boolean isBanned;

	// User's First Name
	private String firstName;

	// User's Last Name
	private String lastName;

	// User's Contact Number
	private String contactNum;

	// User's balance
	private double goldBalance;

	// Used to store owned Hangpies in a list
	private List<Hangpie> inventory;

	// Track game progress, Serves as Game Data
	private int worldLevel;
	private int progressLevel;

	// User Constructor. This is used to define a User
	public User(String username, String pasword, boolean isAdmin, String firstName, String lastName, String contactNum, boolean isBanned)
	{
		this.username = username;
		this.password = pasword;
		this.isAdmin = isAdmin;
		this.isBanned = isBanned;

		this.firstName = firstName;
		this.lastName = lastName;
		this.contactNum = contactNum;

		this.goldBalance = 0;
		this.inventory = new ArrayList<>();
		this.worldLevel = 1;
		this.progressLevel = 1;
	}

	// Used to add Gold to the User
	public void addGold(double amount)
	{
		this.goldBalance += amount;
	}

	// Used to subtract Gold of the User
	public boolean subtractGold(double amount)
	{
		// IF user has more gold than the amount to be subtract:
		if (goldBalance >= amount)
		{
			goldBalance -= amount;
			return true;
		}

		// IF user has less than the amount
		return false;
	}

	// Used to add Hangpie to the Inventory
	public void addToInventory(Hangpie hangpie)
	{
		// Add the Hangpie to the User's inventory
		inventory.add(hangpie);
	}

	public void removeFromInventory(Hangpie hangpie)
	{
		// Remove the Hangpie from the User's inventory
		inventory.remove(hangpie);
	}

	// Getter Methods
	public String getUsername()
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}

	public boolean isAdmin()
	{
		return isAdmin;
	}

	public boolean isBanned()
	{
		return isBanned;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public String getContactNum()
	{
		return contactNum;
	}

	public double getGoldBalance()
	{
		return goldBalance;
	}

	public List<Hangpie> getInventory()
	{
		return inventory;
	}

	public int getWorldLevel()
	{
		return worldLevel;
	}

	public int getProgressLevel()
	{
		return progressLevel;
	}


	// Setter Methods
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public void setContactNum(String contactNum)
	{
		this.contactNum = contactNum;
	}

	public void setWorldLevel(int worldLevel)
	{
		this.worldLevel = worldLevel;
	}

	public void setProgressLevel(int progressLevel)
	{
		this.progressLevel = progressLevel;
	}

	public void setAdmin(boolean isAdmin)
	{
		this.isAdmin = isAdmin;
	}

	public void setBanned(boolean isBanned)
	{
		this.isBanned = isBanned;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}


	@Override
	public int compareTo(User other)
	{
		// Sort alphabetically by username (Case Insensitive)
		return this.username.compareToIgnoreCase(other.getUsername());
	}
}