package game.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.ImageObserver;
import java.util.List;

import game.GameConstants;
import models.Hangpie;
import models.User;
import utils.AssetLoader;

// This class shows the player's pet inventory screen
public class InventoryView {
	private User user; // This is the player whose pets we are showing
	private Image background;
	private Rectangle backButtonBounds;

	// New UI Assets
	private Image nameFrameImg;
	private Image frameImg;

	// Icon Assets 
	private Image heartImg;
	private Image attackImg;

	// Scrolling & Selection
	private int scrollY = 0; // This controls how far down the list the player has scrolled
	private Hangpie selectedPet = null;
	private boolean isBackHovered = false;

	// Layout Constants
	private final int CARD_WIDTH = 220;
	private final int CARD_HEIGHT = 320;
	private final int GAP_X = 70;
	private final int GAP_Y = 160;
	private final int START_X = 85;
	private final int START_Y = 160;

	// This function sets up the inventory screen
	public InventoryView(User user) {
		this.user = user;
		loadAssets();
	}

	// This function loads all the necessary pictures
	private void loadAssets() {
		String path = GameConstants.BG_DIR + GameConstants.INVENTORY_BG;
		background = AssetLoader.loadImage(path, GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);

		// Load UI Elements
		nameFrameImg = AssetLoader.loadImage(GameConstants.NAME_FRAME_IMG, 200, 50);
		frameImg = AssetLoader.loadImage(GameConstants.FRAME_IMG, 220, 60);

		// Load Icons
		heartImg = AssetLoader.loadImage(GameConstants.HEART_IMG, 15, 15);
		attackImg = AssetLoader.loadImage(GameConstants.ATTACK_IMG, 15, 15);
	}

	// Gets the pet that is currently selected
	public Hangpie getSelectedPet() {
		return selectedPet;
	}

	// Sets which pet is currently selected
	public void setSelection(Hangpie pet) {
		this.selectedPet = pet;
	}

	public void clearSelectedPet() {
		this.selectedPet = null;
	}

	// This function draws the entire inventory screen
	public void render(Graphics2D g, int width, int height, ImageObserver observer) {
		if (background != null) {
			g.drawImage(background, 0, 0, width, height, observer);
		} else {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, width, height);
		}

		// Gets the list of pets from the user model
		Shape originalClip = g.getClip();
		// This sets the boundary so that pets outside this box are cut off when scrolling
		g.setClip(0, 90, width, height - 190);

		List<Hangpie> inventory = user.getInventory();

		if (inventory.isEmpty()) {
			g.setFont(GameConstants.UI_FONT);
			g.setColor(Color.WHITE);
			g.drawString("Your inventory is empty.", (width / 2) - 150, height / 2);
		} else {

			int x = START_X;
			int y = START_Y - scrollY; // Applies the current scroll position

			// This loop goes through every pet and draws its card
			for (Hangpie pet : inventory) {
				// Only draws the card if it is visible on the screen
				if (y + CARD_HEIGHT + GAP_Y > 0 && y - 100 < height) {
					drawPetCard(g, pet, x, y, CARD_WIDTH, CARD_HEIGHT, observer);
				}

				x += CARD_WIDTH + GAP_X;

				// Moves to the next row when the current row is full
				if (x + CARD_WIDTH > width - 20) {
					x = START_X;
					y += CARD_HEIGHT + GAP_Y;
				}
			}
		}

		g.setClip(originalClip); // Stops the clipping

		// Draws the top bar
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, 0, width, 90);

		g.setFont(GameConstants.HEADER_FONT);
		g.setColor(Color.WHITE);
		String title = "MY INVENTORY - Select to Equip";
		FontMetrics fm = g.getFontMetrics();
		int titleX = (width - fm.stringWidth(title)) / 2;
		g.drawString(title, titleX, 70);

		// Draws the bottom bar
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, height - 100, width, 100);

		// Draws the Back button
		String backText = isBackHovered ? "> Back <" : "Back";
		g.setFont(GameConstants.BUTTON_FONT);
		fm = g.getFontMetrics();

		int baseWidth = fm.stringWidth("Back");
		int buttonCenterX = (width - 150) - (baseWidth / 2);

		int backW = fm.stringWidth(backText);
		int backH = fm.getHeight();

		int backX = buttonCenterX - (backW / 2);
		int backY = height - 50;

		// This sets the clickable area for the Back button
		backButtonBounds = new Rectangle(backX - 10, backY - fm.getAscent() - 10, backW + 20, backH + 20);

		if (isBackHovered) {
			g.setColor(GameConstants.SELECTION_COLOR);
		} else {
			g.setColor(Color.WHITE);
		}

		g.drawString(backText, backX, backY);
	}

	// This draws the picture and info for one pet
	private void drawPetCard(Graphics2D g, Hangpie pet, int x, int y, int w, int h, ImageObserver observer) {
		// Loads the big tarot card image for the pet
		String tarotPath = GameConstants.HANGPIE_DIR + pet.getImageName() + "/tarot.png";
		Image tarotCard = AssetLoader.loadImage(tarotPath, w, h);

		if (tarotCard != null) {
			g.drawImage(tarotCard, x, y, w, h, null);
		} else {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(x, y, w, h);
			g.setColor(Color.RED);
			g.drawString("No Tarot", x + 80, y + 150);
		}

		// Draws a green border if the pet is currently selected
		if (pet == selectedPet) {
			g.setColor(Color.GREEN);
			g.setStroke(new BasicStroke(4));
			g.drawRect(x, y, w, h);
			g.setStroke(new BasicStroke(1));

			g.setColor(Color.GREEN);
			g.setFont(new Font("Monospaced", Font.BOLD, 14));
			g.drawString("EQUIPPED", x + 10, y + 30);
		} else {
			g.setColor(new Color(255, 255, 255, 50)); 
			g.drawRect(x, y, w, h);
		}

		int nameFrameY = y - 55;
		int nameFrameH = 40;

		if (nameFrameImg != null) {
			g.drawImage(nameFrameImg, x + 10, nameFrameY, w - 20, nameFrameH, observer);
		}

		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.BOLD, 14));
		FontMetrics fm = g.getFontMetrics();
		String name = pet.getName();
		if (name.length() > 15)
			name = name.substring(0, 12) + "...";

		int nameX = x + (w - fm.stringWidth(name)) / 2;
		int nameY = nameFrameY + ((nameFrameH - fm.getHeight()) / 2) + fm.getAscent();
		g.drawString(name, nameX, nameY);

		int statsY = y + h + 10;
		int statsH = 75;

		if (nameFrameImg != null) {
			g.drawImage(nameFrameImg, x + 10, statsY, w - 20, statsH, observer);
		}

		g.setColor(Color.WHITE);
		Font statFont = new Font("Monospaced", Font.BOLD, 16);
		g.setFont(statFont);

		String lvlTxt = "Lvl: " + pet.getLevel();
		fm = g.getFontMetrics();
		int lvlWidth = fm.stringWidth(lvlTxt);

		g.drawString(lvlTxt, x + (w - lvlWidth) / 2, statsY + 28);

		drawModernStats(g, pet, x + 10, statsY, w - 20, statsH, statFont, observer); // Draws the HP and ATK numbers
	}

	// Helper method to draw the specific [Heart] HP | [Sword] ATK
	private void drawModernStats(Graphics2D g, Hangpie pet, int x, int y, int w, int h, Font font, ImageObserver obs) {
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();

		String hpTxt = pet.getMaxHealth() + "";
		String atkTxt = pet.getAttackPower() + "";
		String sep = " | ";

		int iconSize = 15; 
		int gap = 5; 
		int PADDING_X = 40;
		int targetY = y + 45;
		int iconTopY = targetY - 12;
		int hpTextWidth = fm.stringWidth(hpTxt);
		int atkTextWidth = fm.stringWidth(atkTxt);
		int separatorWidth = fm.stringWidth(sep);
		int hpIconX = x + PADDING_X;
		int currentX = hpIconX;

		if (heartImg != null) {
			g.drawImage(heartImg, currentX, iconTopY, iconSize, iconSize, obs);
		}
		currentX += iconSize + gap;

		g.setColor(Color.WHITE);
		g.drawString(hpTxt, currentX, targetY);
		currentX += hpTextWidth;

		int centerPoint = x + (w / 2);
		int sepX = centerPoint - (separatorWidth / 2);

		g.setColor(Color.GRAY);
		g.drawString(sep, sepX, targetY);

		int atkTextEnd = x + w - PADDING_X;
		int atkTextX = atkTextEnd - atkTextWidth;
		int atkIconX = atkTextX - gap - iconSize;

		if (attackImg != null) {
			g.drawImage(attackImg, atkIconX, iconTopY, iconSize, iconSize, obs);
		}

		g.setColor(Color.WHITE);
		g.drawString(atkTxt, atkTextX, targetY);
	}

	// Handles when the player clicks on a pet card
	public String handleMouseClick(int mx, int my) {
		if (backButtonBounds != null && backButtonBounds.contains(mx, my)) {
			return "BACK"; // Return a signal to go back
		}

		int x = START_X;
		int y = START_Y - scrollY;
		int width = GameConstants.WINDOW_WIDTH;

		// Checks every pet card to see if it was clicked
		for (Hangpie pet : user.getInventory()) {
			Rectangle cardBounds = new Rectangle(x, y, CARD_WIDTH, CARD_HEIGHT);
			boolean isVisible = (y + CARD_HEIGHT > 100) && (y < GameConstants.WINDOW_HEIGHT - 100);

			if (isVisible && cardBounds.contains(mx, my)) {
				this.selectedPet = pet;
				System.out.println("[Inventory] Equipped candidate: " + pet.getName());
				return "SELECT"; // Return a signal that a pet was chosen
			}

			x += CARD_WIDTH + GAP_X;
			if (x + CARD_WIDTH > width - 20) {
				x = START_X;
				y += CARD_HEIGHT + GAP_Y;
			}
		}

		return "NONE";
	}

	// Checks if the mouse is over the back button
	public void handleMouseMove(int mx, int my) {
		if (backButtonBounds != null) {
			isBackHovered = backButtonBounds.contains(mx, my);
		}
	}

	// Changes the scroll position when the mouse wheel is moved
	public void handleMouseScroll(int units) {
		int scrollSpeed = 30;
		scrollY += units * scrollSpeed;

		// Stops the scroll position from going too high or too low
		int totalRows = (int) Math.ceil((double) user.getInventory().size() / 4.0);
		int totalContentHeight = (totalRows * (CARD_HEIGHT + GAP_Y));
		int maxScroll = Math.max(0, totalContentHeight - (GameConstants.WINDOW_HEIGHT - 200));

		if (scrollY < 0)
			scrollY = 0;
		if (scrollY > maxScroll + 100)
			scrollY = maxScroll + 100;
	}
}