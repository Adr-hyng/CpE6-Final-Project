package com.adrian.items.equipments;

import com.adrian.base.Shield;
import com.adrian.user_interfaces.GamePanel;

public class WoodenShield extends Shield{
	
	public WoodenShield(GamePanel gp) {
		super(gp);
		this.name = "Wooden Shield";
		this.description = "[" + name + "]\nA shield that is made by wood.";
		this.defenseValue = 1;
		this.getSprite();
	}

	@Override
	protected void getSprite() {
		this.image = this.loadSprite("objects\\shield_wood.png");
	}

}
