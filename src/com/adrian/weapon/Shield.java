package com.adrian.weapon;

import com.adrian.entity.Entity;
import com.adrian.user_interface.GamePanel;

public abstract class Shield extends Entity{
	public int defenseValue;
	
	public Shield(GamePanel gp) {
		super(gp);
	}

}