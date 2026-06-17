package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import interfaces.Colorable;

public class Purchase implements Colorable
{
	private String username;
	private String productID;
	private String productName;
	private double pricePaid;
	private LocalDateTime timestamp;
	private boolean isShopPurchase;

	private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	public Purchase(String username, String productID, String productName, double pricePaid, boolean isShopPurchase)
	{
		this.username = username;
		this.productID = productID;
		this.productName = productName;
		this.pricePaid = pricePaid;
		this.isShopPurchase = isShopPurchase;
		this.timestamp = LocalDateTime.now(); 
	}
	
	// This constructor is for loading from the file
	public Purchase(String username, String productID, String productName, double pricePaid, String timestamp, boolean isShopPurchase)
	{
		this.username = username;
		this.productID = productID;
		this.productName = productName;
		this.pricePaid = pricePaid;
		this.timestamp = LocalDateTime.parse(timestamp, formatter);
		this.isShopPurchase = isShopPurchase;
	}

	public String getUsername()
	{
		return username;
	}

	public String getProductId()
	{
		return productID;
	}

	// Converts the purchase to a simple line for the text file
	public String toFileString()
	{
		String line = String.join("|", username, productID, productName, String.valueOf(pricePaid),timestamp.format(formatter), String.valueOf(isShopPurchase));
		return line;
	}

	@Override
	public String toString()
	{
		// Formatter for a nice, human-readable display
		DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm");
		
		// Ternary Operator for display
		String source = isShopPurchase ? "Shop" : "Marketplace";

		String toString = String.format(Colorable.YELLOW + "[%s]" + " [%s]" + Colorable.RESET + " Purchased: " + Colorable.GREEN
				+ "%s" + Colorable.RESET + " (ID:" + Colorable.GREEN + " %s" + Colorable.RESET + ") for "
				+ Colorable.YELLOW + "%.2f G" + Colorable.RESET,
				timestamp.format(displayFormat),
				source,
				productName,
				productID,
				pricePaid);

		return toString;
	}

	public boolean isShopPurchase()
	{
		return isShopPurchase;
	}

	public void setShopPurchase(boolean isShopPurchase)
	{
		this.isShopPurchase = isShopPurchase;
	}
}