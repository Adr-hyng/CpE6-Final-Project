package com.adrian.weapon;

import com.adrian.user_interface.GamePanel;

public class CommonSword extends Weapon{
	public CommonSword(GamePanel gp) {
		super(gp);
		this.name = "Normal Sword";
		this.attackValue = 1;
		this.getSprite();
	}

	@Override
	protected void getSprite() {
		this.down1 = this.loadSprite("objects\\sword_normal.png", gp.tileSize, gp.tileSize);
	}

}
