package com.adrian.base;

import com.adrian.user_interfaces.GamePanel;

public abstract class Shield extends Item{
	public int defenseValue;
	
	public Shield(GamePanel gp) {
		super(gp);
		this.type = ItemTypes.Shield.class;
	}

}