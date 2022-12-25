package com.adrian.entity;

import java.awt.Rectangle;
import java.util.Random;

import com.adrian.user_interface.GamePanel;

public class NPC extends Entity{
	public NPC(GamePanel gp) {
		super(gp);
		
		direction = "down";
		movementSpeed = 1;
		isMoving = true;
		this.solidArea = new Rectangle(8, 16, 36, 36);
		this.getSprite();
	}
	
	@Override
	protected void getSprite() {
		up1 = loadSprite("npc\\oldman_up_1.png");
		up2 = loadSprite("npc\\oldman_up_2.png");
		down1 = loadSprite("npc\\oldman_down_1.png");
		down2 = loadSprite("npc\\oldman_down_2.png");
		right1 = loadSprite("npc\\oldman_right_1.png");
		right2 = loadSprite("npc\\oldman_right_2.png");
		left1 = loadSprite("npc\\oldman_left_1.png");
		left2 = loadSprite("npc\\oldman_left_2.png");
	}
	
	protected void startMove() {
		actionLockCounter++;
//		gp.collisionHandler.collidePlayer(this);
		if(actionLockCounter > 120) {
			Random random = new Random();
			int i = random.nextInt(125) + 1; // Range 1-100
			
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
}