package com.adrian.weapon;

import com.adrian.entity.Entity;
import com.adrian.user_interface.GamePanel;

public abstract class Weapon extends Entity{
	public int attackValue;
	
	public Weapon(GamePanel gp) {
		super(gp);
	}

}
