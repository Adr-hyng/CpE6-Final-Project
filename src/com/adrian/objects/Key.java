package com.adrian.objects;

import com.adrian.entity.Entity;
import com.adrian.user_interface.GamePanel;

public class Key extends Entity {
	public Key(GamePanel gp) {
		super(gp);
		this.name = "Key";
		this.getSprite();
	}

	@Override
	protected void getSprite() {
		// TODO Auto-generated method stub
		image = loadSprite("objects\\key.png");
	}
}
