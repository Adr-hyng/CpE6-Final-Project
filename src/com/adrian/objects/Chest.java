package com.adrian.objects;

import com.adrian.entity.Entity;
import com.adrian.user_interface.GamePanel;

public class Chest extends Entity {
	public Chest(GamePanel gp) {
		super(gp);
		this.name = "Chest";
		this.getSprite();
	}

	@Override
	protected void getSprite() {
		image = this.loadSprite("objects\\chest.png");
	}
}
