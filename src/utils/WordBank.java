package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordBank {

	public static class WordData {
		public String word;
		public String clue;

		public WordData(String w, String c) {
			this.word = w;
			this.clue = c;
		}
	}

	// Separate word lists by difficulty/length
	// World 1 - Stages 1-4
	private static List<WordData> world1Words = new ArrayList<>();
	// World 2 - Stages 1-4
	private static List<WordData> world2Words = new ArrayList<>();
	// World 3+ - Stages 1-4
	private static List<WordData> world3Words = new ArrayList<>();
	// Boss Words - Stage 5 of any World
	private static List<WordData> bossWords = new ArrayList<>();

	static {
		// World 1
		world1Words.add(new WordData("BANANA", "A long curved yellow fruit"));
		world1Words.add(new WordData("OCEAN", "Large body of salt water"));
		world1Words.add(new WordData("SOCCER", "World's most popular sport"));
		world1Words.add(new WordData("SUMMER", "The hottest season"));
		world1Words.add(new WordData("PLANET", "Earth is one of these"));
		world1Words.add(new WordData("DOCTOR", "Treats sick people"));
		world1Words.add(new WordData("EAGLE", "Large bird of prey"));
		world1Words.add(new WordData("FROZEN", "Turned into ice"));
		world1Words.add(new WordData("GLOW", "To shine brightly"));
		world1Words.add(new WordData("TIGER", "Large striped cat"));
		world1Words.add(new WordData("HEAVEN", "Home of God and angels"));
		world1Words.add(new WordData("LIGHT", "Opposite of darkness"));
		world1Words.add(new WordData("MUSIC", "Rhythm, harmony, melody"));
		world1Words.add(new WordData("SMART", "Intelligent or clever"));
		world1Words.add(new WordData("PHONE", "Used to make calls"));
		world1Words.add(new WordData("CHAIR", "Furniture to sit on"));
		world1Words.add(new WordData("TABLE", "Furniture to eat on"));
		world1Words.add(new WordData("HAPPY", "Feeling or showing pleasure"));
		world1Words.add(new WordData("COOKIE", "A sweet baked snack"));
		world1Words.add(new WordData("FLOWER", "The colorful part of a plant"));
		world1Words.add(new WordData("CASTLE", "A large fortified building"));
		world1Words.add(new WordData("BRIGHT", "Full of light"));
		world1Words.add(new WordData("VIBES", "A person's emotional state or aura"));
		world1Words.add(new WordData("AWESOME", "Extremely impressive or daunting"));
		world1Words.add(new WordData("MEMES", "Funny internet images or videos"));

		// World 2
		world2Words.add(new WordData("GIRAFFE", "The tallest land animal"));
		world2Words.add(new WordData("GUITAR", "Stringed musical instrument"));
		world2Words.add(new WordData("PENGUIN", "Flightless bird living in ice"));
		world2Words.add(new WordData("LIBRARY", "A place full of books"));
		world2Words.add(new WordData("MOUNTAIN", "Large natural earth elevation"));
		world2Words.add(new WordData("ELEPHANT", "World's largest land animal"));
		world2Words.add(new WordData("KEYBOARD", "Computer input device"));
		world2Words.add(new WordData("DIAMOND", "Precious stone, pure carbon"));
		world2Words.add(new WordData("ABSTRACT", "Existing as an idea"));
		world2Words.add(new WordData("ADVENTURE", "An exciting experience"));
		world2Words.add(new WordData("FLAMINGO", "A pink wading bird"));
		world2Words.add(new WordData("ASTRONAUT", "Space traveler"));
		world2Words.add(new WordData("CALENDAR", "System for organizing the year"));
		world2Words.add(new WordData("TREASURE", "A quantity of valuable things"));
		world2Words.add(new WordData("DISCOVER", "To find or find out"));
		world2Words.add(new WordData("VOLCANO", "A mountain that erupts lava"));
		world2Words.add(new WordData("LOLLIPOP", "A hard candy on a stick"));
		world2Words.add(new WordData("FANTASY", "Genre of magic and myths"));
		world2Words.add(new WordData("JOURNEY", "An act of traveling"));
		world2Words.add(new WordData("HARVEST", "Gathering of a ripened crop"));
		world2Words.add(new WordData("BOP", "A term for a really great song"));
		world2Words.add(new WordData("FLOP", "To fail badly; opposite of a hit"));
		world2Words.add(new WordData("STREAK", "Daily activity record on social media"));
		world2Words.add(new WordData("CHALLENGE", "A viral dare on platforms like TikTok"));

		// --- World 3+
		world3Words.add(new WordData("BUTTERFLY", "Insect with colorful wings"));
		world3Words.add(new WordData("RAINBOW", "Colorful arc in the sky"));
		world3Words.add(new WordData("COMMUNITY", "Group of people living together"));
		world3Words.add(new WordData("TEMPERATURE", "Intensity of heat"));
		world3Words.add(new WordData("TECHNOLOGY", "Application of scientific knowledge"));
		world3Words.add(new WordData("UNIVERSAL", "Relating to all people or things"));
		world3Words.add(new WordData("PHOTOGRAPH", "Picture made with a camera"));
		world3Words.add(new WordData("GOVERNMENT", "Political body that governs"));
		world3Words.add(new WordData("CHAMPIONSHIP", "Competition for a title"));
		world3Words.add(new WordData("ACCOMPLISH", "Achieve or complete"));
		world3Words.add(new WordData("EXPERIENCE", "Practical contact with facts"));
		world3Words.add(new WordData("INTERNATIONAL", "Occurring between two or more nations"));
		world3Words.add(new WordData("IMAGINATION", "The ability to form new ideas"));
		world3Words.add(new WordData("CELEBRATION", "A social gathering for a happy event"));
		world3Words.add(new WordData("ENVIRONMENT", "Surroundings or conditions"));
		world3Words.add(new WordData("STATISTICS", "Numerical data analysis"));
		world3Words.add(new WordData("RESPONSIBLE", "Having a duty to deal with something"));
		world3Words.add(new WordData("STREAMING", "Watching media over the internet"));
		world3Words.add(new WordData("INFLUENCER", "Social media personality"));
		world3Words.add(new WordData("ALGORITHM", "A process used by computers and apps"));
		world3Words.add(new WordData("BANANA", "A long curved yellow fruit"));
		world3Words.add(new WordData("OCEAN", "Large body of salt water"));
		world3Words.add(new WordData("SOCCER", "World's most popular sport"));
		world3Words.add(new WordData("SUMMER", "The hottest season"));
		world3Words.add(new WordData("PLANET", "Earth is one of these"));
		world3Words.add(new WordData("DOCTOR", "Treats sick people"));
		world3Words.add(new WordData("EAGLE", "Large bird of prey"));
		world3Words.add(new WordData("FROZEN", "Turned into ice"));
		world3Words.add(new WordData("GLOW", "To shine brightly"));
		world3Words.add(new WordData("TIGER", "Large striped cat"));
		world3Words.add(new WordData("HEAVEN", "Home of God and angels"));
		world3Words.add(new WordData("LIGHT", "Opposite of darkness"));
		world3Words.add(new WordData("MUSIC", "Rhythm, harmony, melody"));
		world3Words.add(new WordData("SMART", "Intelligent or clever"));
		world3Words.add(new WordData("PHONE", "Used to make calls"));
		world3Words.add(new WordData("CHAIR", "Furniture to sit on"));
		world3Words.add(new WordData("TABLE", "Furniture to eat on"));
		world3Words.add(new WordData("HAPPY", "Feeling or showing pleasure"));
		world3Words.add(new WordData("COOKIE", "A sweet baked snack"));
		world3Words.add(new WordData("FLOWER", "The colorful part of a plant"));
		world3Words.add(new WordData("CASTLE", "A large fortified building"));
		world3Words.add(new WordData("BRIGHT", "Full of light"));
		world3Words.add(new WordData("VIBES", "A person's emotional state or aura"));
		world3Words.add(new WordData("AWESOME", "Extremely impressive or daunting"));
		world3Words.add(new WordData("MEMES", "Funny internet images or videos"));
		world3Words.add(new WordData("GIRAFFE", "The tallest land animal"));
		world3Words.add(new WordData("GUITAR", "Stringed musical instrument"));
		world3Words.add(new WordData("PENGUIN", "Flightless bird living in ice"));
		world3Words.add(new WordData("LIBRARY", "A place full of books"));
		world3Words.add(new WordData("MOUNTAIN", "Large natural earth elevation"));
		world3Words.add(new WordData("ELEPHANT", "World's largest land animal"));
		world3Words.add(new WordData("KEYBOARD", "Computer input device"));
		world3Words.add(new WordData("DIAMOND", "Precious stone, pure carbon"));
		world3Words.add(new WordData("ABSTRACT", "Existing as an idea"));
		world3Words.add(new WordData("ADVENTURE", "An exciting experience"));
		world3Words.add(new WordData("FLAMINGO", "A pink wading bird"));
		world3Words.add(new WordData("ASTRONAUT", "Space traveler"));
		world3Words.add(new WordData("CALENDAR", "System for organizing the year"));
		world3Words.add(new WordData("TREASURE", "A quantity of valuable things"));
		world3Words.add(new WordData("DISCOVER", "To find or find out"));
		world3Words.add(new WordData("VOLCANO", "A mountain that erupts lava"));
		world3Words.add(new WordData("LOLLIPOP", "A hard candy on a stick"));
		world3Words.add(new WordData("FANTASY", "Genre of magic and myths"));
		world3Words.add(new WordData("JOURNEY", "An act of traveling"));
		world3Words.add(new WordData("HARVEST", "Gathering of a ripened crop"));
		world3Words.add(new WordData("BOP", "A term for a really great song"));
		world3Words.add(new WordData("FLOP", "To fail badly; opposite of a hit"));
		world3Words.add(new WordData("STREAK", "Daily activity record on social media"));
		world3Words.add(new WordData("CHALLENGE", "A viral dare on platforms like TikTok"));

		// --- Boss Words (Phrases/Sentences) ---
		bossWords.add(new WordData("PIECE OF CAKE", "Idiom: Something very easy"));
		bossWords.add(new WordData("BREAK A LEG", "Idiom: Good luck performance"));
		bossWords.add(new WordData("HARRY POTTER", "The Boy Who Lived"));
		bossWords.add(new WordData("STAR WARS", "May the Force be with you"));
		bossWords.add(new WordData("THE LION KING", "Disney movie with Simba"));
		bossWords.add(new WordData("UP IN THE AIR", "Idiom: Uncertain or unresolved"));
		bossWords.add(new WordData("JURASSIC PARK", "Movie with dinosaurs"));
		bossWords.add(new WordData("NEW YORK CITY", "The Big Apple"));
		bossWords.add(new WordData("ICE CREAM CAKE", "A frozen birthday treat"));
		bossWords.add(new WordData("UNDER THE WEATHER", "Idiom: Feeling sick"));
		bossWords.add(new WordData("SPIDER MAN", "Hero bitten by an arachnid"));
		bossWords.add(new WordData("THE MATRIX", "Blue pill or red pill?"));
		bossWords.add(new WordData("GLOBAL WARMING", "Earth's rising average temperature"));
		bossWords.add(new WordData("THE SILENT HILL", "Horror game with fog"));
		bossWords.add(new WordData("GAME OF THRONES", "A series about the Iron Throne"));
		bossWords.add(new WordData("APPLE OF MY EYE", "Idiom: Cherished person"));
		bossWords.add(new WordData("SIX FLAGS", "A popular amusement park chain"));
		bossWords.add(new WordData("PULP FICTION", "Tarantino movie with a briefcase"));
		bossWords.add(new WordData("AVENGERS ENDGAME", "The ultimate Marvel movie"));
		bossWords.add(new WordData("NO CAP", "Slang: To tell the truth or be serious"));
		bossWords.add(new WordData("CATCH THESE HANDS", "Slang: To start a fight"));
		bossWords.add(new WordData("SQUID GAME", "Korean survival drama series"));
		bossWords.add(new WordData("THE NOTEBOOK", "Classic tear-jerker romance film"));
		bossWords.add(new WordData("TAYLOR SWIFT", "The 'Eras Tour' artist"));
		bossWords.add(new WordData("GOOD VIBES ONLY", "A popular positive phrase"));
		bossWords.add(new WordData("THE MANDALORIAN", "This is the way"));
		bossWords.add(new WordData("IT IS WHAT IT IS", "Idiom: Acceptance of a situation"));
	}

	public static WordData getRandomWord(int worldLevel, int progressLevel) {
		Random rand = new Random();
		List<WordData> source;

		if (progressLevel == 5) {
			source = bossWords;
		} else if (worldLevel == 1) {
			source = world1Words;
		} else if (worldLevel == 2) {
			source = world2Words;
		} else {
			source = world3Words;
		}

		if (source.isEmpty()) {
			System.err.println(
					"Warning: Word list for World Level " + worldLevel + " is empty. Falling back to World 1 list.");
			source = world1Words.isEmpty() ? bossWords : world1Words;
		}

		return source.get(rand.nextInt(source.size()));
	}
}