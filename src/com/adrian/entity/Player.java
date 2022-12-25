package com.adrian.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.adrian.GlobalTool;
import com.adrian.inputs.KeyHandler;
import com.adrian.user_interface.GamePanel;
import com.adrian.utils.Vector2D;

public class Player extends Entity {
	
	GamePanel gp;
	KeyHandler keyInput;
	
	public final Vector2D screen;
	
	public int haveKeyCount = 0;
	
	public Player(GamePanel gp, KeyHandler keyInput, Vector2D position) {
		this.screen = new Vector2D(
				gp.screenWidth / 2 - (gp.tileSize), 
				gp.screenHeight / 2 - (gp.tileSize));
		this.gp = gp;
		this.keyInput = keyInput;
		this.worldPosition = position;
		this.movementSpeed = 4;
		this.direction = "down";
		this.solidArea = new Rectangle(8, 16, 28, 28); // IMPROVEMENT: Use percentage to calculate the rect for responsiveness.
		solidAreaDefaultX = this.solidArea.x;
		solidAreaDefaultY = this.solidArea.y;
		this.getSprite();
	}
	
	private void plugController() {
		double currentX = this.worldPosition.x;
		double currentY = this.worldPosition.y;
		
		// Get Directional Boolean Movement
		if(keyInput.upPressed) {
			direction = "up";
			isMoving = true;
		}
		else if(keyInput.downPressed) {
			direction = "down";
			isMoving = true;
		}
		else if(keyInput.rightPressed) {
			direction = "right";
			isMoving = true;
		}
		else if(keyInput.leftPressed) {
			direction = "left";
			isMoving = true;
		}
		
		
		
		// Move Through Collision Detection using Improvised AABB 2D Collision
		this.collisionOn = false;
		gp.collisionHandler.collideTile(this);
		int objectIndex = gp.collisionHandler.collideObject(this, true);
		this.pickUpObject(objectIndex);
		
		if(! this.collisionOn && isMoving) {
			switch(direction) {
			case "up":
				this.worldPosition.y -= this.movementSpeed;
				break;
			case "down":
				this.worldPosition.y += this.movementSpeed;
				break;
			case "left":
				this.worldPosition.x -= this.movementSpeed;
				break;
			case "right":
				this.worldPosition.x += this.movementSpeed;
				break;
			}
		}
		
		isMoving = false;
		
		// Sprite Animation
		if( ((this.worldPosition.x - currentX) == 0) && ((this.worldPosition.y - currentY) == 0)) {
			// Idle
			spriteNum = 1;
		} else {
			// Moving
			spriteCounter++;
			if(spriteCounter > 10) {
				if(spriteNum == 1) {
					spriteNum = 2;
				}
				else if(spriteNum == 2) {
					spriteNum = 1;
				}
				spriteCounter = 0;
			}
		}
	}
	
	public void pickUpObject(int index) {
		// Just check if it's in the index of the array objects, then it will be deleted.
		if(index == 999) return;
		String objectName = gp.objects[index].name;
		
		switch(objectName) {
		case "Key":
			gp.playSoundEffect(1);
			haveKeyCount++;
			gp.objects[index] = null;
			gp.ui.showMessage("You got a key!");
			break;
		case "Door":
			if(haveKeyCount > 0) {
				gp.playSoundEffect(3);
				gp.objects[index] = null;
				haveKeyCount--;
				gp.ui.showMessage("Door unlocked");
			}
			else {
				gp.ui.showMessage("You don't have enough key!");
			}
			break;
		case "Boots":
			gp.playSoundEffect(2);
			this.movementSpeed += 2;
			gp.objects[index] = null;
			break;
		case "Chest":
			gp.ui.gameFinished = true;
			gp.stopMusic();
			gp.playSoundEffect(4);
			break;
		}
	}
	
	public void getSprite() {
		up1 = loadSprite("player\\boy_up_1.png");
		up2 = loadSprite("player\\boy_up_2.png");
		down1 = loadSprite("player\\boy_down_1.png");
		down2 = loadSprite("player\\boy_down_2.png");
		right1 = loadSprite("player\\boy_right_1.png");
		right2 = loadSprite("player\\boy_right_2.png");
		left1 = loadSprite("player\\boy_left_1.png");
		left2 = loadSprite("player\\boy_left_2.png");
	}
	
	private BufferedImage loadSprite(final String imagePath) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(GlobalTool.assetsDirectory + imagePath));
			image = GlobalTool.utilityTool.scaleImage(image, gp.tileSize, gp.tileSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	public void update() {
		plugController();
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
}
