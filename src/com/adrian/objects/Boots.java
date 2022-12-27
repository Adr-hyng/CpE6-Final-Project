package com.adrian.objects;

import com.adrian.entity.Entity;
import com.adrian.user_interface.GamePanel;

public class Boots extends Entity {
	public String name;

	public Boots(GamePanel gp) {
		super(gp);
		this.name = "Boots";
		this.getSprite();
	}

	@Override
	protected void getSprite() {
		image = this.loadSprite("objects\\boots.png");
	}
}
