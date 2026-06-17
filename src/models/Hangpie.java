package models;

import java.awt.Image;
import java.util.UUID;
import utils.AssetLoader;
import game.GameConstants;

// This class define the Attributes and Characteristics of a Hangpie
// This class implements the public interface Comparable<T> used for sorting objects
public class Hangpie extends Character implements Comparable<Hangpie>
{
	// Animation States
	public enum AnimState
	{
		IDLE, ATTACK, DAMAGE, DEATH
	}

	// UUID used for Marketplace
	private String uniqueId;
	
	// Product ID used exclusively in Shop
	private String productId;
	
	// Hangpie's description
	private String description;
	
	// For the marketplace
	private double price;
	
	// The folder name of the image (e.g., "wizard") (from original code)
	private String imageName; 

	// Current EXP (from original code)
	private int currentExp; 
	
	// Current Animation State (from original code)
	private AnimState currentAnimState = AnimState.IDLE;
	
	// Hangpie constructor to be used in defining a particular Hangpie (Updated to match original codebase structure)
	public Hangpie(String id, String name, String description, double price, int maxHealth, int level, int attackPower, String imageName)
	{
		super(name, maxHealth, level, attackPower);	// Calls the parent (Character) constructor using "super" keyword
		this.productId = id;
		this.description = description;
		this.price = price;
		this.imageName = imageName; // Added
		this.currentExp = 0; // Added
	}

	/* This is a Copy constructor.
	 * It serves as the local copy of products of the user.
	 * Its purpose is to store owned hangpie/s to the Inventory.
	 * Having a local copy of products can allow the user to modify their own Hangpie's data without affecting the Shop Product data. 
	 * This is used to prevent data manipulation to the product list in shop.
	 */
	public Hangpie (Hangpie localCopy)
	{
		// super keyword for the Character constructor
		super(localCopy.getName(), localCopy.getMaxHealth(), localCopy.getLevel(), localCopy.getAttackPower());
		
		// Generate random UUID. Used UUID Class
		this.uniqueId = UUID.randomUUID().toString();
		
		this.productId = localCopy.productId;
		this.description = localCopy.description;
		this.price = localCopy.price;
		this.imageName = localCopy.imageName; // Added
		this.currentExp = localCopy.currentExp; // Added
	}
	
	// Level Progression Methods
	// Used for creating EXP needed to level up per level
	public int getMaxExpForCurrentLevel()
	{
		// EXAMPLE: Level 1 = 10 EXP needed to level up, Level 2 = 20 needed to level up, ...
		return this.level * 10;
	}

	/**
	 * Adds EXP to the Hangpie.
	 * Handles level ups and caps based on World Level.
	 * The amount of EXP to gain.
	 * worldLevelLimit The player's current World Level (Hard Cap for Pet Level).
	 * return true if the pet leveled up, false otherwise.
	 */
	public boolean gainExp(int amount, int worldLevelLimit)
	{
		// Progression Gate: If already at level cap and max exp, do nothing
		if (this.level >= worldLevelLimit && this.currentExp >= getMaxExpForCurrentLevel())
		{
			this.currentExp = getMaxExpForCurrentLevel(); // Ensure clamped
			return false;
		}

		this.currentExp += amount;
		boolean leveledUp = false;

		int requiredExp = getMaxExpForCurrentLevel();

		// Level Up Loop
		while (this.currentExp >= requiredExp && this.level < worldLevelLimit)
		{
			this.currentExp -= requiredExp;
			levelUp();
			leveledUp = true;
			requiredExp = getMaxExpForCurrentLevel(); // Update for next level
		}

		// Hard Cap Enforcement: If we hit the world limit, cap EXP at max
		if (this.level >= worldLevelLimit)
		{
			if (this.currentExp > requiredExp)
			{
				this.currentExp = requiredExp;
			}
		}

		return leveledUp;
	}
	
	private void levelUp()
	{
		this.level++;
		
		// Stats Growth: +1 HP / +1 ATK per level
		this.maxHealth += 1;
		this.attackPower += 1;
		
		// Heal on level up
		this.currentHealth = this.maxHealth;
	}
	
	// Animation Methods
	public void setAnimationState(AnimState state)
	{
		if (this.currentAnimState != state)
		{
			this.currentAnimState = state;

			if (state == AnimState.ATTACK || state == AnimState.DAMAGE || state == AnimState.DEATH)
			{
				Image img = getCurrentImage();
				if (img != null)
				{
					img.flush();
				}
			}
		}
	}

	public Image getCurrentImage()
	{
		return getImageForState(this.currentAnimState);
	}

	private Image getImageForState(AnimState state)
	{
		String fileName = "idle.gif";
		switch (state)
		{
		case ATTACK:
			fileName = "attack.gif";
			break;
		case DAMAGE:
			fileName = "damage.gif";
			break;
		case DEATH:
			fileName = "death.gif";
			break;
		default:
			fileName = "idle.gif";
			break;
		}

		String fullPath = GameConstants.HANGPIE_DIR + imageName + "/" + fileName;
		return AssetLoader.loadImage(fullPath, -1, -1);
	}

	public void preloadAssets()
	{
		for (AnimState state : AnimState.values())
		{
			getImageForState(state);
		}
	}
	
	@Override
	// This method is used to display this object to the Shop easily
	public String toString()
	{
		return String.format("[%s]\t%s\t\tLvl:%d (EXP:%d/%d)\t(HP:%d, Atk:%d)\t\t[Price: %.2fG]\t%s",
				productId,
				name,
				level,
				currentExp,
				getMaxExpForCurrentLevel(),
				maxHealth,
				attackPower,
				price,
				description);
		
	}
	
	@Override
	// This is a sorting method used to sort out Products by its Product ID
	// It can be used to sort the Product list to ascending or descending
	public int compareTo(Hangpie other)
	{
		return this.productId.compareTo(other.getId());
	}

	// Getter Methods (Updated to include new fields)
	public String getUniqueId()
	{
		return uniqueId;
	}
	
	public String getId()
	{
		return productId;
	}

	public double getPrice()
	{
		return price;
	}

	public String getDescription()
	{
		return description;
	}
	
	public String getImageName()
	{
		return imageName;
	}

	public int getCurrentExp()
	{
		return currentExp;
	}


	// Setter Method (Updated to include new fields)
	public void setUniqueId(String uniqueId)
	{
		this.uniqueId = uniqueId;
	}
	
	public void setPrice(double price)
	{
		this.price = price;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setImageName(String imageName)
	{
		this.imageName = imageName;
	}

	public void setCurrentExp(int currentExp)
	{
		this.currentExp = currentExp;
	}
}