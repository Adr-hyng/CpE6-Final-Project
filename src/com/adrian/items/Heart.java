package com.adrian.items;

import java.awt.image.BufferedImage;

import com.adrian.entity.base.Entity;
import com.adrian.user_interfaces.GamePanel;

public class Heart extends Entity {
	public BufferedImage emptyImage, halfImage;
	
	public Heart (GamePanel gp) { 
		super(gp);
		this.name = "Heart";
		this.getSprite();
	}

	@Override
	protected void getSprite() {
		emptyImage = this.loadSprite("objects\\heart_blank.png", gp.tileSize, gp.tileSize);
		halfImage = this.loadSprite("objects\\heart_half.png", gp.tileSize, gp.tileSize);
		image = this.loadSprite("objects\\heart_full.png", gp.tileSize, gp.tileSize);
	}
}
