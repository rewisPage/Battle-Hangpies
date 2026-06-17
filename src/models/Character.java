package models;

// This abstract class is used to define the common attributes and behaviors between various Characters used in the game.
public abstract class Character
{
	// Attributes
	protected String name;				// Character's name
	protected int maxHealth;			// Character's MAX HP
	protected int currentHealth;		// Character's current HP
	protected int level;				// Character's Level
	protected int attackPower;			// Character's Attack Power

	// Parent Constructor. Sub classes can access this constructor using "super" keyword
	public Character(String name, int maxHealth, int level, int attackPower)
	{
		this.name = name;
		this.maxHealth = maxHealth;
		this.currentHealth = maxHealth;	// At the start of the battle, Characters start at full health
		this.level = level;
		this.attackPower  = attackPower;
	}
	
	// Used to apply damage 
	public void takeDamage(int damage)
	{
		// For every Character that took damage, the currentHealth will be reduced by the damage
		this.currentHealth -= damage;
		
		// The value of currentHealth will always be 0 after having negative value subtracted by the damage
		if (this.currentHealth < 0)
		{
			this.currentHealth = 0;
		}
	}
	
	// Character's Alive Status
	public boolean isAlive()
	{
		// IF the Character's Health reaches 0, THEN isAlive() return false
		return this.currentHealth > 0;
	}


	// Getter Methods
	public String getName()
	{
		return name;
	}

	public int getMaxHealth()
	{
		return maxHealth;
	}

	public int getCurrentHealth()
	{
		return currentHealth;
	}

	public int getLevel()
	{
		return level;
	}
	
	public int getAttackPower()
	{
		return attackPower;
	}

	//Setter Methods
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setLevel(int level)
	{
		this.level = level;
	}
	
	public void setMaxHealth(int maxHealth)
	{
		this.maxHealth = maxHealth;
	}
	
	public void setAttackPower(int attackPower)
	{
		this.attackPower = attackPower;
	}
	
	// --- ADDED METHOD FOR HEALTH RESTORATION/LOADING ---
	public void setCurrentHealth(int health) 
	{
		this.currentHealth = health;
		if (this.currentHealth > this.maxHealth) {
			this.currentHealth = this.maxHealth;
		}
	}

}