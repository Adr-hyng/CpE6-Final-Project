package com.adrian.items;

import java.awt.image.BufferedImage;

import com.adrian.entity.base.Entity;
import com.adrian.user_interfaces.GamePanel;

public class ManaCrystal extends Entity {
	public BufferedImage emptyImage;
	
	public ManaCrystal (GamePanel gp) { 
		super(gp);
		this.name = "Heart";
		this.getSprite();
	}

	@Override
	protected void getSprite() {
		emptyImage = this.loadSprite("objects\\manacrystal_blank.png", gp.tileSize, gp.tileSize);
		image = this.loadSprite("objects\\manacrystal_full.png", gp.tileSize, gp.tileSize);
	}
}
