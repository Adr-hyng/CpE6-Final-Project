package com.adrian.items.equipments;

import com.adrian.base.ItemTypes;
import com.adrian.base.Weapon;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.user_interfaces.GameState;

public class RustyAxe extends Weapon{
	public RustyAxe(GamePanel gp) {
		super(gp);
		this.name = "Old Axe";
		this.weaponType = ItemTypes.Weapon.Axe.class;
		this.description = "[" + name + "]\nAn old rusty axe.";
		this.attackValue = 5;
		this.attackArea.width = 25;
		this.attackArea.height = 25;
		this.getSprite();
	}

	@Override
	protected void getSprite() {
		this.image = this.loadSprite("objects\\axe.png");
	}
	
	public void setDialogue (String text) {
		gp.ui.currentDialogue = text;
		gp.playSoundEffect(9);
		gp.gameState = GameState.Dialogue.state;
	}
}
