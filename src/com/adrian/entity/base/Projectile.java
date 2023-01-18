package com.adrian.entity.base;

import com.adrian.user_interfaces.GamePanel;
import com.adrian.utils.Vector2D;

public class Projectile extends Entity{
	
	Entity caster;
	protected int manaCost;

	public Projectile(GamePanel gp) {
		super(gp);
	}

	@Override
	protected void getSprite() {
		
	}
	
	public void set(Double initialX, Double initialY, String direction, boolean isAlive, Entity caster) {
		this.worldPosition = new Vector2D(initialX, initialY);
		this.direction = direction;
		this.isAlive = isAlive;
		this.caster = caster;
		this.currentLife = this.maxLife;
	}
	
	public void destroySelf() {
		gp.projectileList.remove(this);
	}
	
	public boolean haveResource(Entity user) {
		return false;
	}
	
	public void substractResource(Entity user) {
		
	}
	
	public void update() {
		if(this.caster instanceof Player) {
			int entityIndex = gp.collisionHandler.collideEntity(this, gp.monsters);
			if(entityIndex != 999) {
				gp.player.getDamageFromMonster(entityIndex, this.attack);
				this.isAlive = false;
			}
		}
		if(!(this.caster instanceof Player)) {
			boolean contactPlayer = gp.collisionHandler.collidePlayer(this);
			int allyIndex = gp.collisionHandler.collideEntity(this, gp.monsters);
			if(!gp.player.invincible && contactPlayer) {
				gp.player.damagePlayer(this.attack);
				this.isAlive = false;
			} 
			if(allyIndex != 999) {
				if(gp.monsters[allyIndex] != this.caster) this.isAlive = false;
			}
		}
		if(!this.isAlive) this.destroySelf();
		
		switch (this.direction) { 
		case "up": this.worldPosition.y -= this.movementSpeed; break;
		case "down": this.worldPosition.y += this.movementSpeed; break;
		case "left": this.worldPosition.x -= this.movementSpeed; break;
		case "right": this.worldPosition.x += this.movementSpeed; break;
		}
		
		this.currentLife--;
		if(this.currentLife <= 0) {
			this.isAlive = false;
			gp.projectileList.remove(this);
		}
		
		spriteCounter++;
		if(spriteCounter > 12) {
			if(spriteNum == 1) {
				spriteNum = 2;
			}
			else if(spriteNum ==2 ) {
				spriteNum = 1;
			}
			spriteCounter = 0;
		}
	}
}
