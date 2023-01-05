package com.adrian.weapon;

import com.adrian.user_interface.GamePanel;

public class WoodenShield extends Shield{
	
	public WoodenShield(GamePanel gp) {
		super(gp);
		this.name = "Wooden Shield";
		this.defenseValue = 1;
		this.getSprite();
	}

	@Override
	protected void getSprite() {
		this.image = this.loadSprite("objects\\shield_wood.png", gp.tileSize, gp.tileSize);
	}

}
