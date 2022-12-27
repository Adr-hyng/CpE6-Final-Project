package com.adrian.objects;

import com.adrian.user_interface.GamePanel;

public class Boots extends ItemObject {
	public String name;

	public Boots(GamePanel gp) {
		super(gp);
		this.name = "Boots";
		this.getSprite();
	}

	protected void getSprite() {
		this.image = this.loadSprite("objects\\boots.png");
	}
}
