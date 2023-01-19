package com.adrian.entity.monsters;

import java.awt.Rectangle;
import java.util.Random;

import com.adrian.entity.base.Monster;
import com.adrian.entity.projectiles.Rock;
import com.adrian.items.base.Item;
import com.adrian.items.base.ItemTypes;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.utils.WeightedRandom;

public class GreenSlime extends Monster {
	public GreenSlime(GamePanel gp) {
		super(gp);
		
		this.name = "Green Slime";
		this.movementSpeed = 1;
		this.maxLife = 5;
		this.currentLife = maxLife;
		this.attack = 2;
		this.defense = 0;
		this.exp = 1;
		this.projectile = new Rock(gp);
		
		this.solidArea = new Rectangle(3, 18, 42, 30);
		this.solidAreaDefaultX = solidArea.x;
		this.solidAreaDefaultY = solidArea.y;
		this.getSprite();
	}

	@Override
	protected void getSprite() {
		up1 = this.loadSprite("monster\\greenslime_down_1.png", gp.tileSize, gp.tileSize);
		up2 = this.loadSprite("monster\\greenslime_down_2.png", gp.tileSize, gp.tileSize);
		down1 = this.loadSprite("monster\\greenslime_down_1.png", gp.tileSize, gp.tileSize);
		down2 = this.loadSprite("monster\\greenslime_down_2.png", gp.tileSize, gp.tileSize);
		left1 = this.loadSprite("monster\\greenslime_down_1.png", gp.tileSize, gp.tileSize);
		left2 = this.loadSprite("monster\\greenslime_down_2.png", gp.tileSize, gp.tileSize);
		right1 = this.loadSprite("monster\\greenslime_down_1.png", gp.tileSize, gp.tileSize);
		right2 = this.loadSprite("monster\\greenslime_down_2.png", gp.tileSize, gp.tileSize);
	}
	@Override
	protected void startMove() {
		actionLockCounter++;
		if(actionLockCounter > 60) {
			Random random = new Random();
			int i = random.nextInt(100) + 1; // Range 1-100
			
			if(i <= 25) {
				direction = "up";
				isMoving = true;
			}
			else if(i > 25 && i <= 50) {
				direction = "up";
				isMoving = true;
			}
			else if(i > 50 && i <= 75) {
				direction = "left";
				isMoving = true;
			}
			else if(i > 75 && i <= 100) {
				direction = "right";
				isMoving = true;
			}
			moveLockCounter++;
			if(moveLockCounter > 2) {
				isMoving = false;
				moveLockCounter = 0;
			}
			actionLockCounter = 0;
		}
		int i = new Random().nextInt(100) + 1;
		if(i > 99 && !this.projectile.isAlive && this.projectileCooldown == this.projectileMaxCooldown) {
			this.projectile.set(this.worldPosition.x, this.worldPosition.y, this.direction, this.isAlive, this);
			gp.projectileList.add(projectile);
			this.projectileCooldown = 0;
		}
	}
	
	@Override
	protected void checkDrop() {
		WeightedRandom<Item> itemDrops = new WeightedRandom<>() {{
			addEntry(ItemTypes.NotObtainable.Coin.getBronzeCoin(gp), 90);
			addEntry(ItemTypes.Weapon.Sword.getCommonSword(gp), 10);
		}};
		this.dropItem(itemDrops.getRandom());
	}
	
	@Override
	protected void damageReaction() {
		actionLockCounter = 0;
		direction = gp.player.direction;
		isMoving = true;
	}
}
