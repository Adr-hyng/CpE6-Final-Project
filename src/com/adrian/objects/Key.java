package com.adrian.objects;

import com.adrian.user_interface.GamePanel;

public class Key extends ItemObject {
	public Key(GamePanel gp) {
		super(gp);
		this.name = "Key";
		this.getSprite();
	}

	protected void getSprite() {
		image = loadSprite("objects\\key.png");
	}
}
