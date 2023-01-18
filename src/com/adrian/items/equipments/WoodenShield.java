package com.adrian.items.equipments;

import com.adrian.items.base.Shield;
import com.adrian.user_interfaces.GamePanel;

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

}
