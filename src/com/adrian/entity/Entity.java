package com.adrian.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.adrian.GlobalTool;
import com.adrian.user_interface.GamePanel;
import com.adrian.utils.Vector2D;

public abstract class Entity {
	// DEBUG
	protected boolean walkthroughWalls = false;
	
	public GamePanel gp;
	
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public String direction;
	
	public double movementSpeed;
	public boolean isMoving = false;
	public Vector2D worldPosition;
	
	public int spriteCounter = 0;
	public int spriteNum = 1;
	public int actionLockCounter = 0;
	public int moveLockCounter = 0;
	public BufferedImage image;
	
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collisionOn = false;
	
	public Entity(GamePanel gp) {
		this.gp = gp;
	}
	
	protected abstract void getSprite();
	
	protected BufferedImage loadSprite(final String imagePath) {
		image = null;
		try {
			image = ImageIO.read(new File(GlobalTool.assetsDirectory + imagePath));
			image = GlobalTool.utilityTool.scaleImage(image, gp.tileSize, gp.tileSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	protected void startMove() {}
	
	public void update() {
		double currentX = this.worldPosition.x;
		double currentY = this.worldPosition.y;
		startMove();
		collisionOn = false;
		if(!walkthroughWalls) gp.collisionHandler.collideTile(this);
		if(!(this instanceof Player)) {
			gp.collisionHandler.collidePlayer(this);
			gp.collisionHandler.collideObject(this, false);
		}
		if(!this.collisionOn && isMoving) {
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
		
		// Sprite Animation
		if( ((this.worldPosition.x - currentX) == 0) && ((this.worldPosition.y - currentY) == 0)) {
			spriteNum = 1;
		} else {
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
	
	public void draw(Graphics2D g2) {
		Vector2D screenView = new Vector2D(worldPosition.x - gp.player.worldPosition.x + gp.player.screen.x, worldPosition.y - gp.player.worldPosition.y + gp.player.screen.y);
		BufferedImage image = null;
		// To remove black spots when chunk loading.
		final int screenOffset = 2;
		
		// Only load tiles to the visible tiles in the player's screen.
		if(worldPosition.x + (gp.tileSize * screenOffset) > gp.player.worldPosition.x - gp.player.screen.x && 
		   worldPosition.x - (gp.tileSize * screenOffset) < gp.player.worldPosition.x + gp.player.screen.x &&
		   worldPosition.y + (gp.tileSize * screenOffset) > gp.player.worldPosition.y - gp.player.screen.y &&
		   worldPosition.y - (gp.tileSize * screenOffset) < gp.player.worldPosition.y + gp.player.screen.y) {
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
			g2.drawImage(image, (int) screenView.x, (int) screenView.y, null);
		}
	}
}
