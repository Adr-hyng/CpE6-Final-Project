package com.adrian.items;

import com.adrian.base.Item;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.user_interfaces.GameState;

public class Boots extends Item {
	public Boots(GamePanel gp) {
		super(gp);
		this.name = "Boots";
		this.description = "[" + name + "]\nAn old man's boot.";
		this.getSprite();
	}

	protected void getSprite() {
		this.image = this.loadSprite("objects\\boots.png");
	}
	
	public void setDialogue (String text) {
		gp.ui.currentDialogue = text;
		gp.playSoundEffect(9);
		gp.gameState = GameState.Dialogue.state;
	}
}
