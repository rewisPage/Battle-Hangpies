package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import models.BattleState;

public class SaveManager {
	private Map<String, BattleState> saveMap;
	private String databaseFile;

	public SaveManager() {
		this.databaseFile = "saves.txt";
		this.saveMap = new HashMap<>();
		loadSaves();
	}

	private void loadSaves() {
		try (BufferedReader reader = new BufferedReader(new FileReader(databaseFile))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("//") || line.trim().isEmpty()) {
					continue;
				}

				String[] parts = line.split("\\|", -1);

				if (parts.length < 12) {
					continue;
				}

				try {
					String username = parts[0];
					String secretWord = parts[1];
					String clue = parts[2];
					String guessedLetters = parts[3];
					String enemyName = parts[4];
					int enemyHp = Integer.parseInt(parts[5]);
					int enemyMaxHp = Integer.parseInt(parts[6]);
					int enemyAtk = Integer.parseInt(parts[7]);
					String enemyImageFolder = parts[8];
					int enemyLevel = Integer.parseInt(parts[9]);
					String playerPetId = parts[10];
					int playerPetHp = Integer.parseInt(parts[11]);

					BattleState state = new BattleState(username, secretWord, clue, guessedLetters, enemyName, enemyHp,
							enemyMaxHp, enemyAtk, enemyImageFolder, enemyLevel, playerPetId, playerPetHp);

					saveMap.put(username, state);

				} catch (NumberFormatException e) {
					System.err.println("[Warning]: Bad data in saves.txt: " + e.getMessage());
				}
			}
		} catch (IOException e) {
			// This is not a critical error, just indicates the file doesn't exist yet
			// (normal on first run)
		}
	}

	private void writeSaves() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(databaseFile))) {
			writer.write(
					"// FORMAT: username|secretWord|clue|guessedLetters|enemyName|enemyHp|enemyMaxHp|enemyAtk|enemyImageFolder|enemyLevel|playerPetId|playerPetHp");
			writer.newLine();

			for (BattleState state : saveMap.values()) {
				writer.write(state.toFileString());
				writer.newLine();
			}
		} catch (IOException e) {
			System.err.println("CRITICAL ERROR: Could not write to saves.txt: " + e.getMessage());
		}
	}

	public void saveBattle(BattleState state) {
		saveMap.put(state.getUsername(), state);
		writeSaves();
		// Removed console log for saved state
	}

	public BattleState loadBattle(String username) {
		return saveMap.get(username);
	}

	public void deleteSave(String username) {
		if (saveMap.containsKey(username)) {
			saveMap.remove(username);
			writeSaves();
			// Removed console log for deleted save
		}
	}

	public boolean hasSave(String username) {
		return saveMap.containsKey(username);
	}
}