package com.adrian.items;

import com.adrian.base.Item;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.user_interfaces.GameState;

public class Key extends Item {
	
	public Key(GamePanel gp) {
		super(gp);
		this.name = "Key";
		this.description = "[" + name + "]\nIt can be used to open \ndoors.";
		this.getSprite();
	}

	protected void getSprite() {
		image = loadSprite("objects\\key.png");
	}
	
	public void setDialogue() {
		gp.ui.currentDialogue = "You obtained a " + this.name;
		gp.playSoundEffect(9);
		gp.gameState = GameState.Dialogue.state;
	}
}
