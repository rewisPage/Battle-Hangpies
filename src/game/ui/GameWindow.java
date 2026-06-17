package game.ui;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.Random;

import game.GameConstants;
import main.Main;
import models.Hangpie;
import models.User;
import utils.AssetLoader;
import utils.SoundManager;

// This is the main window and controls the entire game flow
public class GameWindow extends Frame implements Runnable { // This creates the window and runs the game code in a loop
	private User currentUser;
	private boolean isRunning;
	private Canvas gameCanvas;
	private Thread gameThread;

	// State Management
	public enum GameState { // This tells the program what screen to show now
		MENU, INVENTORY, PLAYING
	}

	private GameState currentState;

	// Views
	private InventoryView inventoryView; // The inventory screen object
	private BattleView battleView; // The battle screen object

	// Game Data
	private Hangpie equippedHangpie = null; // The pet the player is using for battle

	// Inventory Confirmation State
	private boolean isInventoryConfirmationOpen = false;
	private Hangpie confirmPetCandidate = null;
	private Rectangle modalConfirmYesBounds;
	private Rectangle modalConfirmNoBounds;
	private int selectedConfirmOption = -1;

	// Assets
	private Image background;
	private Image titleImage;
	private Image titleCoverImage;

	// UI Assets
	private Image modalImg;
	private Image frameImg;
	private Image nameFrameImg;

	// Dimensions
	private final int COVER_WIDTH = 980;
	private final int COVER_HEIGHT = 140;
	private final int TITLE_WIDTH = 900;
	private final int TITLE_HEIGHT = 100;

	private final int MENU_UI_BUTTON_SIZE = 50;
	private final int MODAL_WIDTH = 400;
	private final int MODAL_HEIGHT = 500;

	// Menu State
	private String[] options = { "Play Game", "Inventory", "Exit Game" };
	private volatile int selectedOption = -1;
	private Rectangle[] menuBounds; // This array holds the clickable areas for the menu buttons

	// Menu Controls & State
	private Rectangle instructionBtnBounds;
	private boolean isInstructionOpen = false;

	// Equipped Error State
	private long errorFlashStartTime = 0;
	private final int ERROR_DURATION = 1500;
	private final int SHAKE_MAGNITUDE = 5;
	private int shakeX = 0;
	private int shakeY = 0;
	private long lastShakeTime = 0;
	private Random random = new Random(); // This helps create random effects like shaking

	// This is where the game window starts
	public GameWindow(User user) {
		this.currentUser = user;
		this.menuBounds = new Rectangle[options.length];
		this.currentState = GameState.MENU;

		this.inventoryView = new InventoryView(user);

		// If the user has pets, equip the first one by default if none is set
		if (user.getInventory() != null && !user.getInventory().isEmpty()) {
			this.equippedHangpie = user.getInventory().get(0);
			this.equippedHangpie.setCurrentHealth(this.equippedHangpie.getMaxHealth()); // Sets pet health to max
			this.inventoryView.setSelection(this.equippedHangpie);
		}

		// Start Menu Music (Loops)
		SoundManager.playMenuMusic();

		setupWindow(); // Sets up the frame size and settings
		loadAssets(); // Loads pictures

		// Menu UI boundary
		instructionBtnBounds = new Rectangle(GameConstants.WINDOW_WIDTH - 80, 20, MENU_UI_BUTTON_SIZE,
				MENU_UI_BUTTON_SIZE);

		setVisible(true);
		start(); // Starts the main game loop thread
	}

	// This sets up the visual part of the window
	private void setupWindow() {
		setTitle(GameConstants.GAME_TITLE);
		setSize(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
		setResizable(false);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		setBackground(Color.BLACK);

		gameCanvas = new Canvas() { // This is the area where the game draws its graphics
			private static final long serialVersionUID = 1L;

			@Override
			public void update(Graphics g) {
			}

			@Override
			public void paint(Graphics g) {
			}
		};

		gameCanvas.setPreferredSize(new Dimension(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT));
		gameCanvas.setFocusable(true);
		gameCanvas.setBackground(Color.BLACK);
		gameCanvas.setIgnoreRepaint(true);

		// This handles all the mouse moving and clicking events
		MouseAdapter mouseHandler = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (currentState == GameState.MENU) {
					checkMenuHover(e.getX(), e.getY()); // Checks if the mouse is over a menu option
				} else if (currentState == GameState.INVENTORY) {
					if (isInventoryConfirmationOpen) {
						checkInventoryConfirmationHover(e.getX(), e.getY());
						return;
					}
					inventoryView.handleMouseMove(e.getX(), e.getY());
				} else if (currentState == GameState.PLAYING && battleView != null) {
					battleView.handleMouseMove(e.getX(), e.getY());
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (currentState == GameState.MENU) {
					if (isInstructionOpen) {
						isInstructionOpen = false;
						return;
					}

					if (instructionBtnBounds != null && instructionBtnBounds.contains(e.getX(), e.getY())) {
						isInstructionOpen = true;
						SoundManager.playSFX("sfx/select.wav");
						return;
					}

					if (selectedOption != -1) {
						SoundManager.playSFX("sfx/select.wav");
						handleMenuClick(selectedOption); // Runs the action for the clicked menu item
					}
				} else if (currentState == GameState.INVENTORY) {

					if (isInventoryConfirmationOpen) {
						handleInventoryConfirmationClick(e.getX(), e.getY());
						return;
					}

					String action = inventoryView.handleMouseClick(e.getX(), e.getY());

					// Play SFX if a meaningful action occurred
					if (!action.equals("NONE")) {
						SoundManager.playSFX("sfx/select.wav");
					}

					if (action.equals("BACK")) {
						currentState = GameState.MENU; // Go back to the main menu
					} else if (action.equals("SELECT")) {
						Hangpie selectedPet = inventoryView.getSelectedPet();

						if (selectedPet == equippedHangpie) {
							currentState = GameState.MENU;
							return;
						}

						// Check if a battle save exists before changing the pet
						if (Main.saveManager.hasSave(currentUser.getUsername())) {
							confirmPetCandidate = selectedPet;
							isInventoryConfirmationOpen = true; // Show the warning modal
							selectedConfirmOption = -1;
						} else {
							// No save found, so equip the pet right away
							equippedHangpie = selectedPet;
							equippedHangpie.setCurrentHealth(equippedHangpie.getMaxHealth());
							inventoryView.setSelection(equippedHangpie);
							currentState = GameState.MENU;
						}
					}
				} else if (currentState == GameState.PLAYING && battleView != null) {
					// BattleView handles its own SFX inside its method
					String action = battleView.handleMouseClick(e.getX(), e.getY());

					if (action.equals("EXIT")) {
						stop(); // Close the game program
					} else if (action.equals("MENU")) {
						// Return to menu logic: Stop battle music, restart menu music
						SoundManager.stopBattleMusic();
						SoundManager.stopEndGameMusic(); // Ensure victory music stops if active
						SoundManager.playMenuMusic();

						currentState = GameState.MENU; // Go back to the main menu
						battleView = null;
					}
				}
			}

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (currentState == GameState.INVENTORY && !isInventoryConfirmationOpen) {
					inventoryView.handleMouseScroll(e.getWheelRotation()); // Scrolls the inventory screen
				}
			}
		};

		// This handles all the keyboard input events
		gameCanvas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (currentState == GameState.MENU && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					isInstructionOpen = false;
				} else if (currentState == GameState.INVENTORY && isInventoryConfirmationOpen
						&& e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					isInventoryConfirmationOpen = false;
					confirmPetCandidate = null;
					inventoryView.setSelection(equippedHangpie);
				} else if (currentState == GameState.PLAYING && battleView != null) {
					battleView.handleKeyPress(e.getKeyCode(), e.getKeyChar()); // Sends key press to the battle screen

					if (battleView.isExitRequested()) {
						// Return to menu logic: Stop battle music, restart menu music
						SoundManager.stopBattleMusic();
						SoundManager.stopEndGameMusic(); // Ensure victory music stops if active
						SoundManager.playMenuMusic();

						currentState = GameState.MENU;
						battleView = null;
					}
				}
			}
		});

		gameCanvas.addMouseListener(mouseHandler);
		gameCanvas.addMouseMotionListener(mouseHandler);
		gameCanvas.addMouseWheelListener(mouseHandler);

		add(gameCanvas, BorderLayout.CENTER);

		// Closes the game cleanly when the window close button is pressed
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				stop();
			}
		});
	}

	// Logic for handling the confirmation modal on equip
	private void checkInventoryConfirmationHover(int mx, int my) {
		selectedConfirmOption = -1;

		if (modalConfirmYesBounds != null && modalConfirmYesBounds.contains(mx, my)) {
			selectedConfirmOption = 1; // YES button hovered
		} else if (modalConfirmNoBounds != null && modalConfirmNoBounds.contains(mx, my)) {
			selectedConfirmOption = 0; // NO button hovered
		}
	}

	private void handleInventoryConfirmationClick(int mx, int my) {
		if (modalConfirmYesBounds != null && modalConfirmYesBounds.contains(mx, my)) {
			SoundManager.playSFX("sfx/select.wav");
			equippedHangpie = confirmPetCandidate;
			equippedHangpie.setCurrentHealth(equippedHangpie.getMaxHealth());
			Main.saveManager.deleteSave(currentUser.getUsername()); // Deletes the save file
			inventoryView.setSelection(equippedHangpie);
			isInventoryConfirmationOpen = false;
			confirmPetCandidate = null;
			currentState = GameState.MENU;
			System.out.println("[Game] Equipped new Hangpie and deleted active save.");
		} else if (modalConfirmNoBounds != null && modalConfirmNoBounds.contains(mx, my)) {
			SoundManager.playSFX("sfx/select.wav");
			isInventoryConfirmationOpen = false;
			confirmPetCandidate = null;
			inventoryView.setSelection(equippedHangpie); // Cancels and keeps the old pet selected
		}
	}

	private void checkMenuHover(int mx, int my) {
		boolean found = false;
		for (int i = 0; i < options.length; i++) {
			if (menuBounds[i] != null && menuBounds[i].contains(mx, my)) {
				selectedOption = i;
				found = true;
				break;
			}
		}
		if (!found) {
			selectedOption = -1;
		}
	}

	private void handleMenuClick(int option) {
		if (option == 0) {
			// PLAY GAME
			if (equippedHangpie == null) {
				System.out.println("[Game] Cannot start: No Hangpie equipped.");
				errorFlashStartTime = System.currentTimeMillis(); // Starts the shaking animation
			} else {
				SoundManager.stopMenuMusic(); // Stop menu music before battle
				startBattle(); // Starts the game fight
			}

		} else if (option == 1) {
			// INVENTORY
			currentState = GameState.INVENTORY; // Changes to the inventory screen
			inventoryView.setSelection(equippedHangpie);
			isInventoryConfirmationOpen = false;
			confirmPetCandidate = null;
			selectedConfirmOption = -1;

		} else if (option == 2) {
			// EXIT
			stop(); // Closes the program
		}
	}

	// Creates the battle screen object and starts the game
	private void startBattle() {
		battleView = new BattleView(currentUser, equippedHangpie);
		currentState = GameState.PLAYING; // Changes to the playing screen
	}

	// Loads all the main images
	private void loadAssets() {
		String bgPath = GameConstants.BG_DIR + GameConstants.MAIN_BG;
		background = AssetLoader.loadImage(bgPath, GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);

		String titlePath = GameConstants.BG_DIR + GameConstants.TITLE_IMG;
		titleImage = AssetLoader.loadImage(titlePath, TITLE_WIDTH, TITLE_HEIGHT);

		String titleCoverPath = GameConstants.BG_DIR + GameConstants.TITLE_COVER_IMG;
		titleCoverImage = AssetLoader.loadImage(titleCoverPath, COVER_WIDTH, COVER_HEIGHT);

		// Load UI
		modalImg = AssetLoader.loadImage(GameConstants.MODAL_IMG, MODAL_WIDTH, MODAL_HEIGHT);
		frameImg = AssetLoader.loadImage(GameConstants.FRAME_IMG, 600, 60);
		nameFrameImg = AssetLoader.loadImage(GameConstants.NAME_FRAME_IMG, 250, 50);
	}

	// Starts the separate game thread
	private synchronized void start() {
		if (isRunning)
			return;
		isRunning = true;
		gameThread = new Thread(this);
		gameThread.start();
	}

	// Stops the game thread and closes the program
	private synchronized void stop() {
		if (!isRunning)
			return;
		isRunning = false;

		// STOP ALL MUSIC ON EXIT
		SoundManager.stopAll();

		try {
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dispose();
	}

	@Override
	public void run() {
		gameCanvas.createBufferStrategy(3); // Uses triple buffering to stop screen flicker
		BufferStrategy bs = gameCanvas.getBufferStrategy();

		final double FPS = 60.0;
		final double TIME_PER_TICK = 1000000000 / FPS; // Time needed for 60 updates per second
		long lastTime = System.nanoTime();
		double delta = 0;

		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / TIME_PER_TICK;
			lastTime = now;

			if (delta >= 1) {
				if (currentState == GameState.PLAYING && battleView != null) {
					battleView.update(); // Updates the battle logic
				}
				updateMenuShake(); // Updates the error shake animation
				delta--;
			}
			render(bs); // Draws the screen
		}
	}

	// Logic for the menu shake effect when there is no pet equipped
	private void updateMenuShake() {
		if (errorFlashStartTime > 0) {
			long timeElapsed = System.currentTimeMillis() - errorFlashStartTime;
			if (timeElapsed < ERROR_DURATION) {
				if (System.currentTimeMillis() - lastShakeTime > 50) {
					shakeX = random.nextInt(SHAKE_MAGNITUDE * 2 + 1) - SHAKE_MAGNITUDE;
					shakeY = random.nextInt(SHAKE_MAGNITUDE * 2 + 1) - SHAKE_MAGNITUDE;
					lastShakeTime = System.currentTimeMillis();
				}
			} else {
				errorFlashStartTime = 0;
				shakeX = 0;
				shakeY = 0;
			}
		}
	}

	// This tells the correct screen object to draw itself
	private void render(BufferStrategy bs) {
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

		switch (currentState) {
		case MENU:
			renderMenu(g);
			if (isInstructionOpen) {
				drawInstructionModal(g, gameCanvas.getWidth(), gameCanvas.getHeight());
			}
			break;
		case INVENTORY:
			inventoryView.render(g, gameCanvas.getWidth(), gameCanvas.getHeight(), gameCanvas);
			if (isInventoryConfirmationOpen) {
				renderInventoryConfirmation(g, gameCanvas.getWidth(), gameCanvas.getHeight());
			}
			break;
		case PLAYING:
			if (battleView != null) {
				battleView.render(g, gameCanvas.getWidth(), gameCanvas.getHeight(), gameCanvas);
			}
			break;
		}

		g.dispose();
		bs.show();
		Toolkit.getDefaultToolkit().sync();
	}

	// Draws the warning modal for pet switching
	private void renderInventoryConfirmation(Graphics2D g, int width, int height) {
		if (!isInventoryConfirmationOpen)
			return;

		g.setColor(new Color(0, 0, 0, 180)); // Fills the background with a dark, transparent color
		g.fillRect(0, 0, width, height);

		int mW = 600;
		int mH = 250;
		int mX = (width - mW) / 2;
		int mY = (height - mH) / 2;

		// Draw Modal Background
		if (modalImg != null) {
			g.drawImage(modalImg, mX, mY, mW, mH, null);
		} else {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(mX, mY, mW, mH);
		}

		// Title
		g.setColor(Color.BLACK);
		Font titleFont = new Font("Monospaced", Font.BOLD, 30);
		g.setFont(titleFont);
		String title = "WARNING: Active Save Detected";
		FontMetrics fmTitle = g.getFontMetrics();
		g.drawString(title, mX + (mW - fmTitle.stringWidth(title)) / 2, mY + 50);
		g.setFont(GameConstants.INSTRUCTION_FONT);
		FontMetrics fmMessage = g.getFontMetrics();

		// Message 1 (Red)
		g.setColor(Color.RED);
		String msg = "Switching pets will DELETE your current battle save.";
		g.drawString(msg, mX + (mW - fmMessage.stringWidth(msg)) / 2, mY + 100);

		// Message 2 (Black)
		g.setColor(Color.BLACK);
		String petName = (confirmPetCandidate != null) ? confirmPetCandidate.getName() : "this pet";
		String question = "Are you sure you want to equip " + petName + " and proceed?";
		g.drawString(question, mX + (mW - fmMessage.stringWidth(question)) / 2, mY + 125);

		g.setFont(GameConstants.BUTTON_FONT);
		FontMetrics fmButton = g.getFontMetrics();
		int btnH = 40;

		int buttonSpacing = 30;
		int btnAreaY = mY + 175;

		String yesTxtContent = "YES";
		String noTxtContent = "NO";

		String yesTxtHover = "> " + yesTxtContent + " <";
		String noTxtHover = "> " + noTxtContent + " <";

		int yesTxtWMax = fmButton.stringWidth(yesTxtHover);
		int noTxtWMax = fmButton.stringWidth(noTxtHover);

		int totalBtnWidth = yesTxtWMax + noTxtWMax + buttonSpacing;
		int startX = mX + (mW - totalBtnWidth) / 2;

		int currentX = startX;

		// YES Button
		String yesTxt = (selectedConfirmOption == 1) ? yesTxtHover : yesTxtContent;
		int yesTxtWidthNormal = fmButton.stringWidth(yesTxtContent);

		int yesDrawOffsetX = (yesTxtWMax - yesTxtWidthNormal) / 2;

		modalConfirmYesBounds = new Rectangle(currentX, btnAreaY, yesTxtWMax, btnH); // Sets the YES button clickable
																						// area

		g.setColor((selectedConfirmOption == 1) ? GameConstants.SELECTION_COLOR : Color.BLACK);

		if (selectedConfirmOption == 1) {
			g.drawString(yesTxt, currentX, btnAreaY + fmButton.getAscent());
		} else {
			g.drawString(yesTxt, currentX + yesDrawOffsetX, btnAreaY + fmButton.getAscent());
		}

		currentX += yesTxtWMax + buttonSpacing;

		// NO Button
		String noTxt = (selectedConfirmOption == 0) ? noTxtHover : noTxtContent;
		int noTxtWidthNormal = fmButton.stringWidth(noTxtContent);
		int noDrawOffsetX = (noTxtWMax - noTxtWidthNormal) / 2;

		modalConfirmNoBounds = new Rectangle(currentX, btnAreaY, noTxtWMax, btnH); // Sets the NO button clickable area

		g.setColor((selectedConfirmOption == 0) ? GameConstants.SELECTION_COLOR : Color.BLACK);

		if (selectedConfirmOption == 0) {
			g.drawString(noTxt, currentX, btnAreaY + fmButton.getAscent());
		} else {
			g.drawString(noTxt, currentX + noDrawOffsetX, btnAreaY + fmButton.getAscent());
		}
	}

	// Renders the main menu screen elements
	private void renderMenu(Graphics2D g) {
		// Applies shake only if there's no equipped pet and an error flash is active
		int equippedOffsetX = (equippedHangpie == null && errorFlashStartTime > 0) ? shakeX : 0;
		int equippedOffsetY = (equippedHangpie == null && errorFlashStartTime > 0) ? shakeY : 0;

		int menuOptionsOffsetX = 0;
		int menuOptionsOffsetY = 0;

		// Background
		if (background != null) {
			g.drawImage(background, 0, 0, gameCanvas.getWidth(), gameCanvas.getHeight(), gameCanvas);
		}

		if (instructionBtnBounds != null && nameFrameImg != null) {
			int btnX = instructionBtnBounds.x;
			int btnY = instructionBtnBounds.y;
			int btnW = instructionBtnBounds.width;
			int btnH = instructionBtnBounds.height;

			g.drawImage(nameFrameImg, btnX, btnY, btnW, btnH, null);

			g.setFont(GameConstants.HEADER_FONT);
			g.setColor(Color.BLACK);
			FontMetrics fm = g.getFontMetrics();
			String qText = "?";
			int textX = btnX + (btnW - fm.stringWidth(qText)) / 2;
			int textY = btnY + (btnH - fm.getAscent()) / 2 + fm.getAscent() - 10;
			g.drawString(qText, textX, textY);
		}

		// Title Banner
		int bannerY = 100;
		if (titleCoverImage != null) {
			int coverX = (gameCanvas.getWidth() - COVER_WIDTH) / 2;
			g.drawImage(titleCoverImage, coverX, bannerY, COVER_WIDTH, COVER_HEIGHT, null);
		}

		if (titleImage != null) {
			int textX = (gameCanvas.getWidth() - TITLE_WIDTH) / 2;
			int textY = bannerY + (COVER_HEIGHT - TITLE_HEIGHT) / 2;
			g.drawImage(titleImage, textX, textY, TITLE_WIDTH, TITLE_HEIGHT, null);
		}

		// World Level/Stage Level
		int levelFrameCenterY = 260;
		int levelFrameW = 350;
		int levelFrameH = 50;
		int levelFrameX = (gameCanvas.getWidth() - levelFrameW) / 2;

		if (nameFrameImg != null) {
			g.drawImage(nameFrameImg, levelFrameX, levelFrameCenterY, levelFrameW, levelFrameH, null);

			g.setFont(GameConstants.SUBTITLE_FONT);
			g.setColor(GameConstants.TEXT_COLOR);
			FontMetrics fm = g.getFontMetrics();

			String levelText = String.format("WORLD: %d - STAGE: %d", currentUser.getWorldLevel(),
					currentUser.getProgressLevel());
			int levelWidth = fm.stringWidth(levelText);
			int levelTextX = (gameCanvas.getWidth() - levelWidth) / 2;
			int levelTextY = levelFrameCenterY + ((levelFrameH - fm.getHeight()) / 2) + fm.getAscent();
			g.drawString(levelText, levelTextX, levelTextY);
		}

		// Equipped Status
		int equipY = levelFrameCenterY + 70;
		int nfW = 350;
		int nfH = 50;
		int nfX = (gameCanvas.getWidth() - nfW) / 2;
		int nfY = equipY;

		int drawNFX = nfX + equippedOffsetX;
		int drawNFY = nfY + equippedOffsetY;

		if (equippedHangpie == null && errorFlashStartTime > 0) {
			long timeElapsed = System.currentTimeMillis() - errorFlashStartTime;
			int flicker = (int) (timeElapsed / 100) % 2;
			if (flicker == 0) {
				g.setColor(new Color(200, 0, 0, 150));
				g.fillRect(drawNFX, drawNFY, nfW, nfH);
			}
		}

		if (nameFrameImg != null) {
			g.drawImage(nameFrameImg, drawNFX, drawNFY, nfW, nfH, null);
		}

		g.setFont(GameConstants.SMALLER_BUTTON_FONT);
		FontMetrics fmEquip = g.getFontMetrics();
		String nameText = (equippedHangpie != null) ? equippedHangpie.getName() : "No Hangpie Equipped";

		if (equippedHangpie == null) {
			g.setColor(Color.RED);
			if (errorFlashStartTime > 0) {
				g.setColor(Color.WHITE);
			}
		} else {
			g.setColor(Color.WHITE);
		}

		int nameW = fmEquip.stringWidth(nameText);
		int nameX = (gameCanvas.getWidth() - nameW) / 2 + equippedOffsetX;
		int nameY = drawNFY + ((nfH - fmEquip.getHeight()) / 2) + fmEquip.getAscent();
		g.drawString(nameText, nameX, nameY);

		// Menu Options
		drawMenuOptions(g, menuOptionsOffsetX, menuOptionsOffsetY);
	}

	private void drawInstructionModal(Graphics2D g, int width, int height) {
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, 0, width, height);

		int mW = MODAL_WIDTH;
		int mH = MODAL_HEIGHT;
		int mX = (width - mW) / 2;
		int mY = (height - mH) / 2;

		if (modalImg != null) {
			g.drawImage(modalImg, mX, mY, mW, mH, null);
		} else {
			g.setColor(Color.GRAY);
			g.fillRect(mX, mY, mW, mH);
		}

		// Title
		g.setColor(Color.BLACK);
		g.setFont(GameConstants.HEADER_FONT);
		String title = GameConstants.INSTRUCTION_TITLE;
		FontMetrics fm = g.getFontMetrics();
		g.drawString(title, mX + (mW - fm.stringWidth(title)) / 2, mY + 50);

		// Instructions Content
		g.setFont(GameConstants.INSTRUCTION_FONT);
		g.setColor(Color.BLACK);

		String content = GameConstants.INSTRUCTION_TEXT;
		String[] lines = content.split("\n");

		// Word Wrapping parameters
		int wrapWidth = mW - 60;
		int startX = mX + 30;
		int startY = mY + 95;
		FontMetrics fmSmall = g.getFontMetrics();
		int lineHeight = fmSmall.getHeight();
		int indent = 20;

		for (String line : lines) {
			String trimmedLine = line.trim();

			if (trimmedLine.isEmpty()) {
				startY += lineHeight / 3;
				continue;
			}

			if (trimmedLine.endsWith(":") || trimmedLine.startsWith("Objectives")) {
				int lineW = fmSmall.stringWidth(line);
				int textX = startX;
				int textY = startY + fmSmall.getAscent();
				g.drawString(line, textX, textY);
				startY += lineHeight;
			} else if (trimmedLine.startsWith("Type")) {
				String[] words = trimmedLine.split(" ");
				StringBuilder currentLine = new StringBuilder();
				int currentX = mX + (mW - wrapWidth) / 2;

				for (String word : words) {
					if (fmSmall.stringWidth(currentLine.toString() + " " + word) > wrapWidth) {
						int textY = startY + fmSmall.getAscent();
						g.drawString(currentLine.toString().trim(), currentX, textY);

						startY += lineHeight;
						currentLine = new StringBuilder();
					}

					if (currentLine.length() > 0) {
						currentLine.append(" ");
					}
					currentLine.append(word);
				}

				if (currentLine.length() > 0) {
					int textY = startY + fmSmall.getAscent();
					g.drawString(currentLine.toString().trim(), currentX, textY);
					startY += lineHeight;
				}
			} else {
				String[] words = trimmedLine.split(" ");
				StringBuilder currentLine = new StringBuilder();
				int currentX = startX + (trimmedLine.startsWith("Your") || trimmedLine.startsWith("Defeat")
						|| trimmedLine.startsWith("Win") ? indent : 0);

				for (String word : words) {
					if (fmSmall.stringWidth(currentLine.toString() + " " + word) > wrapWidth) {
						int textY = startY + fmSmall.getAscent();
						g.drawString(currentLine.toString().trim(), currentX, textY);

						startY += lineHeight;
						currentLine = new StringBuilder();
						currentX = startX + (trimmedLine.startsWith("Your") || trimmedLine.startsWith("Defeat")
								|| trimmedLine.startsWith("Win") ? indent * 2 : indent);
					}

					if (currentLine.length() > 0) {
						currentLine.append(" ");
					}
					currentLine.append(word);
				}

				if (currentLine.length() > 0) {
					int textY = startY + fmSmall.getAscent();
					g.drawString(currentLine.toString().trim(), currentX, textY);
					startY += lineHeight;
				}
			}
		}

		// Close Instruction
		g.setFont(new Font("Monospaced", Font.BOLD, 16));
		g.setColor(Color.DARK_GRAY);
		String closeText = "[Press ESC or Click to Close]";
		g.drawString(closeText, mX + (mW - g.getFontMetrics().stringWidth(closeText)) / 2, mY + mH - 30);
	}

	private void drawMenuOptions(Graphics g, int offsetX, int offsetY) {
		g.setFont(GameConstants.UI_FONT);
		FontMetrics fm = g.getFontMetrics();

		int startY = 530 + offsetY;
		int spacing = 50;

		for (int i = 0; i < options.length; i++) {
			String text = options[i];
			boolean isSelected = (i == selectedOption);

			if (isSelected) {
				g.setColor(GameConstants.SELECTION_COLOR);
				text = "> " + text + " <";
			} else {
				g.setColor(GameConstants.TEXT_COLOR);
			}

			int textWidth = fm.stringWidth(text);
			int textHeight = fm.getHeight();
			int x = (gameCanvas.getWidth() - textWidth) / 2 + offsetX;
			int y = startY + (i * spacing);

			menuBounds[i] = new Rectangle(x, y - fm.getAscent(), textWidth, textHeight);

			g.drawString(text, x, y);
		}
	}
}