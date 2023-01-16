package com.adrian.items.equipments;

import com.adrian.base.Shield;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.user_interfaces.GameState;

public class BlueShield extends Shield{
	
	public BlueShield(GamePanel gp) {
		super(gp);
		this.name = "Blue Shield";
		this.description = "[" + name + "]\nA colored blue-\nwielded iron shield.";
		this.defenseValue = 2;
		this.getSprite();
	}

	@Override
	protected void getSprite() {
		this.image = this.loadSprite("objects\\shield_blue.png");
	}
	
	public void setDialogue (String text) {
		gp.ui.currentDialogue = text;
		gp.playSoundEffect(9);
		gp.gameState = GameState.Dialogue.state;
	}

}