package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import models.RedeemCode;

public class CodeManager
{
	private Map<String, RedeemCode> codeMap;
	private String databaseFile = "codes.txt";
	private Random random;

	public CodeManager()
	{
		this.codeMap = new HashMap<>();
		this.random = new Random();
		loadCodes();
	}

	private void loadCodes()
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

				if (parts.length < 4)
				{
					continue;
				}

				try
				{
					String codeString = parts[0];
					double goldValue = Double.parseDouble(parts[1]);
					boolean isUsed = Boolean.parseBoolean(parts[2]);
					String redeemedBy = parts[3];

					RedeemCode code = new RedeemCode(codeString, goldValue);
					code.setUsed(isUsed);
					code.setRedeemedBy(redeemedBy);

					codeMap.put(codeString, code);

				}
				
				catch (NumberFormatException e)
				{
					// Use System.err.println for unrecoverable data corruption warning during loading
					System.err.println("[Warning]: Bad data in codes.txt for line: " + line);
				}
			}
		}
		
		catch (IOException e)
		{
			// Inform user of error loading
			System.err.println("Error loading code database: " + e.getMessage());
		}
	}

	private void saveCodes()
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(databaseFile)))
		{
			writer.write("// FORMAT: codeString|goldValue|isUsed|redeemedBy");
			writer.newLine();

			for (RedeemCode code : codeMap.values())
			{
				String line = String.join("|",
						code.getCodeString(),
						String.valueOf(code.getGoldValue()),
						String.valueOf(code.isUsed()),
						code.getRedeemedBy());

				writer.write(line);
				writer.newLine();
			}
		}
		
		catch (IOException e)
		{
			// Use System.err.println for critical save failure
			System.err.println("CRITICAL ERROR: Could not save code database: " + e.getMessage());
		}
	}

	public int generateCodes(int quantity, double goldValue)
	{
		int codeCreated = 0;

		for (int i = 0; i < quantity; i++)
		{
			String newCodeString;

			do
			{
				newCodeString = generateRandomCodeString();
			}
			
			while (codeMap.containsKey(newCodeString));

			RedeemCode newCode = new RedeemCode(newCodeString, goldValue);
			codeMap.put(newCodeString, newCode);
			codeCreated++;
		}

		saveCodes();
		return codeCreated;
	}

	public String generateRandomCodeString()
	{
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 12; i++)
		{
			if (i == 4 || i == 8)
			{
				sb.append('-');
			}
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}

	public double redeemCode(String codeString, String username)
	{
		RedeemCode code = codeMap.get(codeString);

		// IF the codeString inputted by the user is not found from the codes.txt, then code is null
		// IF code is null, then redeemCode method will return -1
		if (code == null)
		{
			return -1;
		}

		// IF code is used, the return -1
		if (code.isUsed())
		{
			return -1;
		}

		// IF the code is unused, set its status to isUsed
		code.setUsed(true);
		
		// Record the user 
		code.setRedeemedBy(username);

		// Update database
		saveCodes();

		// redeemCode method will return with the code's goldValue
		return code.getGoldValue();
	}

	public boolean deleteCode(String codeString)
	{
		RedeemCode removedCode = codeMap.remove(codeString);

		if (removedCode != null)
		{
			saveCodes();
			return true;
		}

		return false;
	}

	public Collection<RedeemCode> getAllCodes()
	{
		return codeMap.values();
	}

	public int getActiveCodeCount()
	{
		int count = 0;
		for (RedeemCode code : codeMap.values())
		{
			if (!code.isUsed())
			{
				count++;
			}
		}
		return count;
	}
}