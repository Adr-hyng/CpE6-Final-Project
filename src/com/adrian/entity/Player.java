package com.adrian.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.adrian.inputs.KeyHandler;
import com.adrian.user_interface.GamePanel;
import com.adrian.user_interface.GameState;
import com.adrian.utils.Vector2D;

public class Player extends Entity {
	
	GamePanel gp;
	KeyHandler keyInput;
	
	public final Vector2D screen;
	
	public Player(GamePanel gp, KeyHandler keyInput, Vector2D position) {
		super(gp);
		this.screen = new Vector2D(
				gp.screenWidth / 2 - (gp.tileSize), 
				gp.screenHeight / 2 - (gp.tileSize));
		
		this.gp = gp;
		this.keyInput = keyInput;
		
		// Default Values
		this.worldPosition = position;
		this.movementSpeed = 4;
		this.maxLife = 6;
		this.currentLife = maxLife;
		this.direction = "down";
		
		this.solidArea = new Rectangle(8, 16, 28, 28); // IMPROVEMENT: Use percentage to calculate the rect for responsiveness.
		solidAreaDefaultX = this.solidArea.x;
		solidAreaDefaultY = this.solidArea.y;
		this.getSprite();
	}
	
	private void plugController() {
		if(keyInput.haveKeyPressed.get("W")) {
			direction = "up";
			isMoving = true;
		}
		else if(keyInput.haveKeyPressed.get("S")) {
			direction = "down";
			isMoving = true;
		}
		else if(keyInput.haveKeyPressed.get("D")) {
			direction = "right";
			isMoving = true;
		}
		else if(keyInput.haveKeyPressed.get("A")) {
			direction = "left";
			isMoving = true;
		}
		else if(!keyInput.haveKeyPressed.get("W") &&
				!keyInput.haveKeyPressed.get("S") &&
				!keyInput.haveKeyPressed.get("A") &&
				!keyInput.haveKeyPressed.get("D")) {
			isMoving = false;
		}
	}
	
	@Override
	protected void startMove() {
		plugController();
		int objectIndex = gp.collisionHandler.collideObject(this, true);
		int npcIndex = gp.collisionHandler.collideEntity(this, gp.npcs);
		int obstacleIndex = gp.collisionHandler.collideEntity(this, gp.obstacles);
		gp.eventHandler.checkEvent();
		pickUpObject(objectIndex);
		interactObstacle(obstacleIndex);
		return;
	}
	
	public void pickUpObject(int index) {
		if(index == 999) return;
		// Should
		int findIndex = gp.itemObjects[index].getClass().getName().lastIndexOf(".") + 1;
		String objectCode = gp.itemObjects[index].getClass().getName();
		objectCode = objectCode.substring(findIndex, objectCode.length() - 1);
		switch(objectCode) {
		case "Key":
			break;
		case "Door":
			break;
		case "Boot":
			System.out.println("Boots");
			gp.itemObjects[index] = null;
			break;
		case "Chest":
			break;
		}
	}
	
	public void interactEntity(int index) {
		if(index == 999) {
			return;
		};
		if(index != 999  && (keyInput.haveKeyPressed.get("ENTER"))) {
			gp.gameState = GameState.Dialogue.state;
			gp.npcs[index].trigger();
		}
		keyInput.haveKeyPressed.replace("ENTER", false);	
	}
	
	public void interactObstacle(int index) {
		if(index == 999) {
			return;
		};
		if(index != 999  && (keyInput.haveKeyPressed.get("ENTER"))) {
			gp.gameState = GameState.Dialogue.state;
			gp.obstacles[index].trigger();
			gp.npcs[index] = null;
		}
		keyInput.haveKeyPressed.replace("ENTER", false);
		
	}
	
	
	@Override
	protected void getSprite() {
		up1 = loadSprite("player\\boy_up_1.png");
		up2 = loadSprite("player\\boy_up_2.png");
		down1 = loadSprite("player\\boy_down_1.png");
		down2 = loadSprite("player\\boy_down_2.png");
		right1 = loadSprite("player\\boy_right_1.png");
		right2 = loadSprite("player\\boy_right_2.png");
		left1 = loadSprite("player\\boy_left_1.png");
		left2 = loadSprite("player\\boy_left_2.png");
	}
	
	public void draw(Graphics2D g) {
		BufferedImage image = null;
		
		switch(direction) {
			case "up":
				if (spriteNum == 1) {
					image = up1;
				}
				if (spriteNum == 2) {
					image = up2;
				}
				break;
			case "down":
				if (spriteNum == 1) {
					image = down1;
				}
				if (spriteNum == 2) {
					image = down2;
				}
				break;
			case "right":
				if (spriteNum == 1) {
					image = right1;
				}
				if (spriteNum == 2) {
					image = right2;
				}
				break;
			case "left":
				if (spriteNum == 1) {
					image = left1;
				}
				if (spriteNum == 2) {
					image = left2;
				}
				break;
		}
		g.drawImage(image, (int) screen.x, (int) screen.y, null);
	}

	@Override
	public void trigger() {
		// TODO Auto-generated method stub
		
	}
}
