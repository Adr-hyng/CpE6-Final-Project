package com.adrian.items;

import com.adrian.base.Consumable;
import com.adrian.base.Entity;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.user_interfaces.GameState;

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
		gp.playSoundEffect(9);
		gp.gameState = GameState.Dialogue.state;
	}
	
	@Override
	public <T extends Entity> void useItem(T user) {
		gp.gameState = GameState.Dialogue.state;
		gp.ui.currentDialogue = "You drink the " + this.name + "!\n"
				+ "It restored " + this.restoreValue + " HP.";
		user.currentLife += restoreValue;
		if(gp.player.currentLife > gp.player.maxLife) {
			gp.player.currentLife = gp.player.maxLife;
		}
	}
}
