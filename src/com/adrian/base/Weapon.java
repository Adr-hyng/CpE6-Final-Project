package com.adrian.base;

import com.adrian.user_interfaces.GamePanel;

public abstract class Weapon extends Item{
	public int attackValue;
	public Class<?> weaponType;
	
	public Weapon(GamePanel gp) {
		super(gp);
		this.type = ItemTypes.Weapon.class;
	}

}
