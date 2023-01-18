package com.adrian.items.base;

import com.adrian.entity.base.Entity;
import com.adrian.user_interfaces.GamePanel;

public abstract class Consumable extends Item{
	
	
	public Consumable(GamePanel gp) {
		super(gp);
		this.type = ItemTypes.Consumable.class;
	}
	
	public abstract <T extends Entity> void useItem(T user);
}
