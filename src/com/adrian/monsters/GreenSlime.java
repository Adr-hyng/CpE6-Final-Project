package com.adrian.monsters;

import java.awt.Rectangle;
import java.util.Random;

import com.adrian.base.Entity;
import com.adrian.user_interfaces.GamePanel;

public class GreenSlime extends Entity{
	public GreenSlime(GamePanel gp) {
		super(gp);
		
		name = "Green Slime";
		movementSpeed = 1;
		maxLife = 5;
		currentLife = maxLife;
		type =  2;
		attack = 2;
		defense = 0;
		exp = 5;
		
		solidArea = new Rectangle(3, 18, 42, 30);
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
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
	}
	
	@Override
	protected void damageReaction() {
		actionLockCounter = 0;
		direction = gp.player.direction;
		isMoving = true;
	}
}
