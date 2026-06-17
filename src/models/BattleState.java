package models;

public class BattleState {
	private String username;
	private String secretWord;
	private String clue;
	private String guessedLetters;

	// Enemy Data
	private String enemyName;
	private int enemyHp;
	private int enemyMaxHp;
	private int enemyAtk;
	private String enemyImageFolder;
	private int enemyLevel;

	// Player Pet Data
	private String playerPetId;
	private int playerPetHp;

	public BattleState(String username, String secretWord, String clue, String guessedLetters, String enemyName,
			int enemyHp, int enemyMaxHp, int enemyAtk, String enemyImageFolder, int enemyLevel, String playerPetId,
			int playerPetHp) {
		this.username = username;
		this.secretWord = secretWord;
		this.clue = clue;
		this.guessedLetters = guessedLetters;
		this.enemyName = enemyName;
		this.enemyHp = enemyHp;
		this.enemyMaxHp = enemyMaxHp;
		this.enemyAtk = enemyAtk;
		this.enemyImageFolder = enemyImageFolder;
		this.enemyLevel = enemyLevel;
		this.playerPetId = playerPetId;
		this.playerPetHp = playerPetHp;
	}

	// Getters
	public String getUsername() {
		return username;
	}

	public String getSecretWord() {
		return secretWord;
	}

	public String getClue() {
		return clue;
	}

	public String getGuessedLetters() {
		return guessedLetters;
	}

	public String getEnemyName() {
		return enemyName;
	}

	public int getEnemyHp() {
		return enemyHp;
	}

	public int getEnemyMaxHp() {
		return enemyMaxHp;
	}

	public int getEnemyAtk() {
		return enemyAtk;
	}

	public String getEnemyImageFolder() {
		return enemyImageFolder;
	}

	public int getEnemyLevel() {
		return enemyLevel;
	}

	public String getPlayerPetId() {
		return playerPetId;
	}

	public int getPlayerPetHp() {
		return playerPetHp;
	}

	public String toFileString() {
		return String.join("|", username, secretWord, clue, guessedLetters, enemyName, String.valueOf(enemyHp),
				String.valueOf(enemyMaxHp), String.valueOf(enemyAtk), enemyImageFolder, String.valueOf(enemyLevel),
				playerPetId, String.valueOf(playerPetHp));
	}
}