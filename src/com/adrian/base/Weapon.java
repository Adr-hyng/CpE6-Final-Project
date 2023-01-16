package com.adrian.base;

import com.adrian.types.ItemType;
import com.adrian.types.WeaponType;
import com.adrian.user_interfaces.GamePanel;

public abstract class Weapon extends Item{
	public int attackValue;
	public WeaponType weaponType;
	
	public Weapon(GamePanel gp) {
		super(gp);
		this.type = ItemType.Weapon;
	}

}
