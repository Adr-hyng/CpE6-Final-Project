package com.adrian.items;

import com.adrian.entity.base.Entity;
import com.adrian.items.base.Consumable;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.user_interfaces.GameState;
import com.adrian.utils.Sound;

public class RedPotion extends Consumable {
	public int restoreValue;
	
	public RedPotion(GamePanel gp) {
		super(gp);
		this.name = "Red Potion";
		this.restoreValue = 2;
		this.description = "[" + name + "]\nRestores 2 HP.";
		this.getSprite();
	}

	protected void getSprite() {
		image = loadSprite("objects\\potion_red.png");
	}
	
	public void setDialogue(String text) {
		gp.ui.currentDialogue = text;
		Sound.ACHIEVE.playSE();
		gp.gameState = GameState.Dialogue.state;
	}
	
	@Override
	public <T extends Entity> void useItem(T user) {
		user.currentLife += restoreValue;
		Sound.POWERUP.playSE();
	}
}
