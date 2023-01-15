package com.adrian.entity;

import java.awt.Rectangle;
import java.util.Random;

import com.adrian.user_interface.GamePanel;
import com.adrian.user_interface.GameState;

public class NPC extends Entity{
	public NPC(GamePanel gp, String[] dialogues) {
		super(gp);
		name = "NPC";
		direction = "down";
		movementSpeed = 1;
		isMoving = true;
		this.dialogues = dialogues;
		this.solidArea = new Rectangle(8, 16, 48, 48);
		this.getSprite();
	}
	
	@Override
	protected void getSprite() {
		up1 = loadSprite("npc\\oldman_up_1.png", gp.tileSize, gp.tileSize);
		up2 = loadSprite("npc\\oldman_up_2.png", gp.tileSize, gp.tileSize);
		down1 = loadSprite("npc\\oldman_down_1.png", gp.tileSize, gp.tileSize);
		down2 = loadSprite("npc\\oldman_down_2.png", gp.tileSize, gp.tileSize);
		right1 = loadSprite("npc\\oldman_right_1.png", gp.tileSize, gp.tileSize);
		right2 = loadSprite("npc\\oldman_right_2.png", gp.tileSize, gp.tileSize);
		left1 = loadSprite("npc\\oldman_left_1.png", gp.tileSize, gp.tileSize);
		left2 = loadSprite("npc\\oldman_left_2.png", gp.tileSize, gp.tileSize);
	}
	
	public void trigger() {
		if(dialogues[dialogueIndex] == null) dialogueIndex = 0;
		String text = dialogues[dialogueIndex];
		if(text.length() >= 29) text = gp.ui.getAdjustableText(dialogues[dialogueIndex]);
		gp.ui.currentDialogue = text;
		dialogueIndex = (dialogueIndex + 1 ) % dialogues.length;
		switch(gp.player.direction) {
		case "up":
			direction = "down";
			break;
		case "down":
			direction = "up";
			break;
		case "left":
			direction = "right";
			break;
		case "right":
			direction = "left";
			break;
		}
	}
	
	public void setDialogue() {
		gp.gameState = GameState.Dialogue.state;
		gp.playSoundEffect(6);
	}
	
	protected void startMove() {
		actionLockCounter++;
		if(actionLockCounter > 120) {
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
}
