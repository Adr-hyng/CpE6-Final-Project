package com.adrian.objects;

import com.adrian.user_interface.GamePanel;
import com.adrian.user_interface.GameState;

public class Key extends ItemObject {
	
	public Key(GamePanel gp) {
		super(gp);
		this.name = "Key";
		this.getSprite();
	}

	protected void getSprite() {
		image = loadSprite("objects\\key.png");
	}
	
	public void setDialogue() {
		gp.ui.currentDialogue = "You obtained a " + this.name;
		gp.playSoundEffect(7);
		gp.gameState = GameState.Dialogue.state;
	}
}
