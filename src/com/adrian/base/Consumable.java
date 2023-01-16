package com.adrian.base;

import com.adrian.types.ItemType;
import com.adrian.user_interfaces.GamePanel;

public abstract class Consumable extends Item{
	
	
	public Consumable(GamePanel gp) {
		super(gp);
		this.type = ItemType.Consumable;
	}
	
	public abstract <T extends Entity> void useItem(T user);
}
