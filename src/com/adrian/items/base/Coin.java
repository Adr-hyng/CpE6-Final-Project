package com.adrian.items.base;

import com.adrian.user_interfaces.GamePanel;

public class Coin extends Item {
	public int value;
	
	public Coin(GamePanel gp) {
		super(gp);
		this.type = ItemTypes.NotObtainable.class;
		this.getSprite();
	}

	protected void getSprite() {
	}
}
