package utils;

import javax.sound.sampled.*;
import java.io.File;
import java.util.Random;

public class SoundManager {

	private static Clip menuMusic;
	private static Clip battleMusic;
	private static Clip endGameMusic; // Victory or Defeat music

	private static long menuPausePosition = 0;
	private static long battlePausePosition = 0;
	private static boolean currentTrackIsBoss = false;

	private static final Random random = new Random();

	// Root path for sounds (Project Root/sounds/)
	private static final String SOUND_ROOT = "sounds/";

	// --- MENU MUSIC (LOOPING) ---

	public static void playMenuMusic() {
		if (menuMusic != null && menuMusic.isRunning()) {
			return; // Already playing
		}

		try {
			if (menuMusic == null) {
				File file = new File(SOUND_ROOT + "menu/menu_bg.wav");
				if (!file.exists()) {
					System.err.println("[SoundManager] Missing: " + file.getAbsolutePath());
					return;
				}
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
				menuMusic = AudioSystem.getClip();
				menuMusic.open(audioIn);
			}

			// Resume from pause position if available
			menuMusic.setMicrosecondPosition(menuPausePosition);
			menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
			menuMusic.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void pauseMenuMusic() {
		if (menuMusic != null && menuMusic.isRunning()) {
			menuPausePosition = menuMusic.getMicrosecondPosition();
			menuMusic.stop();
		}
	}

	public static void stopMenuMusic() {
		if (menuMusic != null) {
			menuMusic.stop();
			menuMusic.close();
			menuMusic = null;
			menuPausePosition = 0;
		}
	}

	// --- BATTLE MUSIC (LOOPING) ---

	public static void playBattleMusic(boolean isBoss) {
		// If music is already loaded and we are resuming the same type (Boss vs
		// Regular)
		if (battleMusic != null) {
			if (currentTrackIsBoss == isBoss) {
				if (!battleMusic.isRunning()) {
					// Resume from where it was paused (e.g., after Victory/Defeat screen)
					battleMusic.setMicrosecondPosition(battlePausePosition);
					battleMusic.loop(Clip.LOOP_CONTINUOUSLY);
					battleMusic.start();
				}
				return; // Already playing or successfully resumed
			}
		}

		// If we are switching modes (Normal <-> Boss) or starting fresh, stop the old
		// track
		stopBattleMusic();
		currentTrackIsBoss = isBoss;

		String folderPath = isBoss ? "battle/boss/" : "battle/battle/";
		File folder = new File(SOUND_ROOT + folderPath);

		if (folder.exists() && folder.isDirectory()) {
			File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".wav"));
			if (files != null && files.length > 0) {
				File randomTrack = files[random.nextInt(files.length)];
				playBattleClip(randomTrack);
			} else {
				System.err.println("[SoundManager] No music found in: " + folderPath);
			}
		} else {
			System.err.println("[SoundManager] Directory missing: " + folderPath);
		}
	}

	private static void playBattleClip(File file) {
		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
			battleMusic = AudioSystem.getClip();
			battleMusic.open(audioIn);
			battleMusic.loop(Clip.LOOP_CONTINUOUSLY);
			battleMusic.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void pauseBattleMusic() {
		if (battleMusic != null && battleMusic.isRunning()) {
			battlePausePosition = battleMusic.getMicrosecondPosition();
			battleMusic.stop();
		}
	}

	public static void stopBattleMusic() {
		if (battleMusic != null) {
			battleMusic.stop();
			battleMusic.close();
			battleMusic = null;
			battlePausePosition = 0;
		}
	}

	// --- END GAME MUSIC (Victory/Defeat) ---

	public static void playEndGameMusic(String relativePath) {
		// Stop menu music if playing, but do NOT stop battle music (we want to resume
		// it later)
		stopMenuMusic();
		stopEndGameMusic(); // Stop any previous end game track

		try {
			File file = new File(SOUND_ROOT + relativePath);
			if (!file.exists()) {
				System.err.println("[SoundManager] Missing: " + file.getAbsolutePath());
				return;
			}
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
			endGameMusic = AudioSystem.getClip();
			endGameMusic.open(audioIn);
			endGameMusic.loop(Clip.LOOP_CONTINUOUSLY);
			endGameMusic.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void stopEndGameMusic() {
		if (endGameMusic != null) {
			endGameMusic.stop();
			endGameMusic.close();
			endGameMusic = null;
		}
	}

	// --- GLOBAL STOP ---

	public static void stopAll() {
		stopMenuMusic();
		stopBattleMusic();
		stopEndGameMusic();
	}

	// --- SFX (ONESHOT) ---

	public static void playSFX(String relativePath) {
		// We run SFX in a new thread to avoid UI lag
		new Thread(() -> {
			try {
				File file = new File(SOUND_ROOT + relativePath);
				if (!file.exists()) {
					System.err.println("[SoundManager] SFX Missing: " + relativePath);
					return;
				}

				AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
				Clip clip = AudioSystem.getClip();
				clip.open(audioIn);
				clip.start();

				// Close resources when done
				clip.addLineListener(event -> {
					if (event.getType() == LineEvent.Type.STOP) {
						clip.close();
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}
}