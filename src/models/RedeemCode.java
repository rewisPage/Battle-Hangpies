package models;

public class RedeemCode
{
	private String codeString;
	private double goldValue;
	private boolean isUsed;
	private String redeemedBy;

	// RedeemCode Constructor used to define the code details
	public RedeemCode(String codeString, double goldValue)
	{
		this.codeString = codeString;
		this.goldValue = goldValue;
		
		// Code default value
		this.isUsed = false;
		this.redeemedBy = "N/A";
	}

	// Getters
	public String getCodeString()
	{
		return codeString;
	}

	public double getGoldValue()
	{
		return goldValue;
	}

	public boolean isUsed()
	{
		return isUsed;
	}
	
	public String getRedeemedBy()
	{
		return redeemedBy;
	}

	// Setter
	public void setUsed(boolean isUsed)
	{
		this.isUsed = isUsed;
	}

	public void setRedeemedBy(String redeemedBy)
	{
		this.redeemedBy = redeemedBy;
	}
}