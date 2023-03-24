package com.adrian.items.equipments;

import com.adrian.base.GameState;
import com.adrian.items.base.ItemTypes;
import com.adrian.items.base.Weapon;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.utils.Sound;

public class CommonSword extends Weapon{
	public CommonSword(GamePanel gp) {
		super(gp);
		this.name = "Normal Sword";
		this.weaponType = ItemTypes.Weapon.Sword.class;
		this.description = "[" + name + "]\nAn old rusty sword.";
		this.attackValue = 2;
		this.attackArea.width = 36;
		this.attackArea.height = 36;
		this.getSprite();
	}

	@Override
	protected void getSprite() {
		this.image = this.loadSprite("objects\\sword_normal.png");
	}
	
	public void setDialogue (String text) {
		gp.ui.currentDialogue = text;
		Sound.ACHIEVE.playSE();
		gp.gameState = GameState.Dialogue.state;
	}
}
