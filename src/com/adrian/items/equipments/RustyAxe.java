package com.adrian.items.equipments;

import com.adrian.items.base.ItemTypes;
import com.adrian.items.base.Weapon;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.user_interfaces.GameState;
import com.adrian.utils.Sound;

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
		Sound.ACHIEVE.playSE();
		gp.gameState = GameState.Dialogue.state;
	}
}
