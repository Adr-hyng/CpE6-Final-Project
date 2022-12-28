package com.adrian.objects;

import java.awt.image.BufferedImage;

import com.adrian.entity.Entity;
import com.adrian.user_interface.GamePanel;

public class Heart extends Entity {
	public BufferedImage emptyHeartImage, halfHeartImage;
	
	public Heart (GamePanel gp) { 
		super(gp);
		this.name = "Heart";
		this.getSprite();
	}

	@Override
	protected void getSprite() {
		emptyHeartImage = this.loadSprite("objects\\heart_blank.png", gp.tileSize, gp.tileSize);
		halfHeartImage = this.loadSprite("objects\\heart_half.png", gp.tileSize, gp.tileSize);
		image = this.loadSprite("objects\\heart_full.png", gp.tileSize, gp.tileSize);
	}

	@Override
	public void trigger() {
		// TODO Auto-generated method stub
		
	}
}
