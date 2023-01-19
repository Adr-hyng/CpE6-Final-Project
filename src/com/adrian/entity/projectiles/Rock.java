package com.adrian.entity.projectiles;

import java.awt.Color;

import com.adrian.entity.base.Projectile;
import com.adrian.user_interfaces.GamePanel;

public class Rock extends Projectile{
	
	GamePanel gp;
	public int manaCost;

	public Rock(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		this.name = "Rock";
		this.movementSpeed = 7;
		this.maxLife = 80;
		this.currentLife = this.maxLife;
		this.attack = 2;
		this.manaCost = 0;
		this.isAlive = false;
		this.getSprite();
	}
	
	@Override
	protected void getSprite() {
		up1 = loadSprite("projectile\\rock_down_1.png", gp.tileSize, gp.tileSize);
		up2 = loadSprite("projectile\\rock_down_1.png", gp.tileSize, gp.tileSize);
		down1 = loadSprite("projectile\\rock_down_1.png", gp.tileSize, gp.tileSize);
		down2 = loadSprite("projectile\\rock_down_1.png", gp.tileSize, gp.tileSize);
		right1 = loadSprite("projectile\\rock_down_1.png", gp.tileSize, gp.tileSize);
		right2 = loadSprite("projectile\\rock_down_1.png", gp.tileSize, gp.tileSize);
		left1 = loadSprite("projectile\\rock_down_1.png", gp.tileSize, gp.tileSize);
		left2 = loadSprite("projectile\\rock_down_1.png", gp.tileSize, gp.tileSize);
	}
	
	@Override
	public Color getParticleColor() {
		Color color = new Color(40, 50, 0);
		return color;
	}
	
	@Override
	public int getParticleSize() {
		int size = 10;
		return size;
	}
	
	@Override
	public int getParticleSpeed() {
		int speed = 1;
		return speed;
	}
	
	@Override
	public int getParticleMaxLife() {
		int maxLife = 15;
		return maxLife;
	}
}
