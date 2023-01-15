package com.adrian.items.equipments;

import com.adrian.base.Weapon;
import com.adrian.user_interfaces.GamePanel;

public class CommonSword extends Weapon{
	public CommonSword(GamePanel gp) {
		super(gp);
		this.name = "Normal Sword";
		this.description = "[" + name + "]\nAn old rusty sword.";
		this.attackValue = 5;
		this.getSprite();
	}

	@Override
	protected void getSprite() {
		this.image = this.loadSprite("objects\\sword_normal.png");
	}
}
