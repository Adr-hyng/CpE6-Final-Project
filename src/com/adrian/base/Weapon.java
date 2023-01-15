package com.adrian.base;

import com.adrian.user_interfaces.GamePanel;

public abstract class Weapon extends Item{
	public int attackValue;
	
	public Weapon(GamePanel gp) {
		super(gp);
	}

}
