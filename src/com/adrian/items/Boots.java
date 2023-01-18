package com.adrian.items;

import com.adrian.items.base.Item;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.user_interfaces.GameState;
import com.adrian.utils.Sound;

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
		Sound.ACHIEVE.playSE();
		gp.gameState = GameState.Dialogue.state;
	}
}
