package com.adrian.items.equipments;

import com.adrian.base.GameState;
import com.adrian.items.base.Shield;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.utils.Sound;

public class WoodenShield extends Shield{
	
	public WoodenShield(GamePanel gp) {
		super(gp);
		this.name = "Wooden Shield";
		this.description = "[" + name + "]\nA shield that is made \nby wood.";
		this.defenseValue = 1;
		this.getSprite();
	}

	@Override
	protected void getSprite() {
		this.image = this.loadSprite("objects\\shield_wood.png");
	}
	
	public void setDialogue (String text) {
		gp.ui.currentDialogue = text;
		Sound.ACHIEVE.playSE();
		gp.gameState = GameState.Dialogue.state;
	}

}
