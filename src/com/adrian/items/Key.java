package com.adrian.items;

import com.adrian.base.GameState;
import com.adrian.items.base.Item;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.utils.Sound;

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
	
	public void setDialogue(String text) {
		gp.ui.currentDialogue = text;
		Sound.ACHIEVE.playSE();
		gp.gameState = GameState.Dialogue.state;
	}
}
