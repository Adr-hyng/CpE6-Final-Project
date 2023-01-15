package com.adrian.obstacles;

import com.adrian.base.Entity;
import com.adrian.user_interfaces.GamePanel;

public class Chest extends Entity {
	public Chest(GamePanel gp) {
		super(gp);
		this.name = "Chest";
		this.getSprite();
	}

	@Override
	protected void getSprite() {
		image = this.loadSprite("objects\\chest.png", gp.tileSize, gp.tileSize);
	}

}
