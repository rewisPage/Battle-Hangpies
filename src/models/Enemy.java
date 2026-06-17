package models;

import java.awt.Image;
import utils.AssetLoader;

public class Enemy extends Character {

// Animation States - This definition is required for BattleView
	public enum AnimState {
		IDLE, ATTACK, DAMAGE, DEATH
	}

	private String assetFolder;
	private String folderName; // Store the raw folder name for saving
	private AnimState currentState;

	public Enemy(String name, int maxHealth, int level, int attackPower, String folderName) {
		super(name, maxHealth, level, attackPower);
		this.folderName = folderName;
		this.assetFolder = "images/" + folderName;
		this.currentState = AnimState.IDLE;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setAnimationState(AnimState state) {
		if (this.currentState != state) {
			this.currentState = state;

			if (state == AnimState.ATTACK || state == AnimState.DAMAGE || state == AnimState.DEATH) {
				Image img = getCurrentImage();
				if (img != null) {
					img.flush();
				}
			}
		}
	}

	public Image getCurrentImage() {
		return getImageForState(this.currentState);
	}

	private Image getImageForState(AnimState state) {
		String fileName = "idle.gif";
		switch (state) {
		case ATTACK:
			fileName = "attack.gif";
			break;
		case DAMAGE:
			fileName = "damage.gif";
			break;
		case DEATH:
			fileName = "death.gif";
			break;
		default:
			fileName = "idle.gif";
			break;
		}

		String fullPath = assetFolder + "/" + fileName;
		return AssetLoader.loadImage(fullPath, -1, -1);
	}

	public void preloadAssets() {
		for (AnimState state : AnimState.values()) {
			getImageForState(state);
		}
	}
}