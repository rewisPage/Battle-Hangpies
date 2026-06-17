package models;

public class Listing
{
	private String uniquePetId;
	private String sellerUsername;
	private double sellingPrice;
	
	private String productId;
	private String petName;
	private int petLevel;
	private int petExp;
	private int petHealth;
	private int petAttack;
	private String description;
	
	// Constructor for creating a new listing (Updated to include EXP)
	public Listing(String sellerUsername, Hangpie pet, double sellingPrice)
	{
		this.uniquePetId = pet.getUniqueId();
		this.sellerUsername = sellerUsername;
		this.sellingPrice = sellingPrice;
		this.productId = pet.getId();
		this.petName = pet.getName();
		this.petLevel = pet.getLevel();
		this.petExp = pet.getCurrentExp();
		this.petHealth = pet.getMaxHealth();
		this.petAttack = pet.getAttackPower();
		this.description = pet.getDescription();
	}
	
	// Constructor for loading a listing from the file
	public Listing(String uniquePetId, String sellerUsername, double sellingPrice, String productId, String petName, int petLevel, int petHealth, int petAttack, String description, int petExp)
	{
		this.uniquePetId = uniquePetId;
		this.sellerUsername = sellerUsername;
		this.sellingPrice = sellingPrice;
		this.productId = productId;
		this.petName = petName;
		this.petLevel = petLevel;
		this.petHealth = petHealth;
		this.petAttack = petAttack;
		this.description = description;
		this.petExp = petExp; // NEW FIELD
	}
	
	public String getUniqueId()
	{
		return uniquePetId;
	}
	
	public String getProductId()
	{
		return productId;
	}
	
	public String getSellerUsername()
	{
		return sellerUsername;
	}
	
	public double getPrice()
	{
		return sellingPrice;
	}
	
	public String getPetName()
	{
		return petName;
	}
	
	public int getPetLevel()
	{
		return petLevel;
	}
	
	public int getPetExp()
	{
		return petExp;
	}
	
	public int getPetHealth()
	{
		return petHealth;
	}

	public int getPetAttack() {
		return petAttack;
	}
	
    public String getDescription()
    {
    	return description;
    }
	
	@Override
	public String toString()
	{
		String uniqueId = uniquePetId.substring(0, 8).toUpperCase();
		int maxExp = petLevel * 10; 
		
		String format = String.format("[FOR SALE] [UUID: %s] [%s] [STATS:(Lvl:%d, EXP:%d/%d, HP:%d, Atk:%d)] [Price: %.2fG] (Seller: %s)",
				uniqueId, petName, petLevel, petExp, maxExp, petHealth, petAttack, sellingPrice, sellerUsername);
		
		return format;
	}
	
	
	public String toFileString()
	{
		String toFileString = String.join("|",
                uniquePetId,
                sellerUsername,
                String.valueOf(sellingPrice),
                productId,
                petName,
                String.valueOf(petLevel),
                String.valueOf(petHealth),
                String.valueOf(petAttack),
                description,
                String.valueOf(petExp));
		
		return toFileString;
	}
}