package com.adrian.entity.projectiles;

import com.adrian.entity.base.Entity;
import com.adrian.entity.base.Projectile;
import com.adrian.user_interfaces.GamePanel;

public class Fireball extends Projectile{
	
	GamePanel gp;

	public Fireball(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		this.name = "Fireball";
		this.movementSpeed = 10;
		this.maxLife = 80;
		this.currentLife = this.maxLife;
		this.attack = 10;
		this.manaCost = 1;
		this.isAlive = false;
		this.getSprite();
	}
	
	@Override
	protected void getSprite() {
		up1 = loadSprite("projectile\\fireball_up_1.png", gp.tileSize, gp.tileSize);
		up2 = loadSprite("projectile\\fireball_up_2.png", gp.tileSize, gp.tileSize);
		down1 = loadSprite("projectile\\fireball_down_1.png", gp.tileSize, gp.tileSize);
		down2 = loadSprite("projectile\\fireball_down_2.png", gp.tileSize, gp.tileSize);
		right1 = loadSprite("projectile\\fireball_right_1.png", gp.tileSize, gp.tileSize);
		right2 = loadSprite("projectile\\fireball_right_2.png", gp.tileSize, gp.tileSize);
		left1 = loadSprite("projectile\\fireball_left_1.png", gp.tileSize, gp.tileSize);
		left2 = loadSprite("projectile\\fireball_left_2.png", gp.tileSize, gp.tileSize);
	}
	
	@Override
	public boolean haveResource(Entity user) {
		return (user.currentMana >= this.manaCost);
	}
	
	@Override
	public void substractResource(Entity user) {
		user.currentMana -= this.manaCost;
	}
}
