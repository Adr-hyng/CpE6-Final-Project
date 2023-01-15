package com.adrian.entity;

import java.awt.Color;
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
	
	protected BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	protected BufferedImage attack_up1, attack_up2, attack_down1, attack_down2, attack_left1, attack_left2, attack_right1, attack_right2;
	protected int spriteNum = 1;
	public BufferedImage image;
	
	protected String dialogues[] = new String[20];
	protected int dialogueIndex = 0; 
	
	public String name;
	public Vector2D worldPosition;
	public int maxLife;
	public int currentLife = 0;
	public double movementSpeed;
	public String direction = "down";
	public int type;
	
	// Attributes
	public int level;
	public int strength;
	public int dexterity;
	public int attack;
	public int defense;
	public int exp;
	public int nextLevelExp;
	public int coin;
	
	// Counter
	public int spriteCounter = 0;
	public int actionLockCounter = 0;
	public int moveLockCounter = 0;
	public int invincibleCount = 0;
	int dyingCounter = 0;
	int healthBarCounter = 0;
	
	// States
	public boolean invincible = false;
	public boolean isAlive = true;
	public boolean isDying = false;
	public boolean collision = false;
	public boolean isMoving = false;
	public boolean attacking = false;
	public boolean collisionOn = false;
	public boolean showHealthBar = false;
	
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
	
	// Collision Rect
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public int solidAreaDefaultX, solidAreaDefaultY;
	
	public Entity(GamePanel gp) {
		this.gp = gp;
	}
	
	protected abstract void getSprite();
	protected BufferedImage loadSprite(final String imagePath, int width, int height) {
		image = null;
		try {
			image = ImageIO.read(new File(GlobalTool.assetsDirectory + imagePath));
			image = GlobalTool.utilityTool.scaleImage(image, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	// STAY
	// Set Dialogue Speech to NPC / Obstacles
	protected void setDialogue() {}
	protected void setDialogue(String text) {}
	
	// STAY
	protected void trigger() {}
	protected void damageReaction() {}
	protected void startMove() {}
	
	protected void contactPlayer() {
		boolean contactPlayer = gp.collisionHandler.collidePlayer(this);
		if (this.type == 2 && contactPlayer == true) {
			if(!gp.player.invincible) {
				gp.player.currentLife --;
				gp.player.invincible = true;
			}
		}
	}
	
	public void takeDamage(int damageTaken) {
		if(!this.invincible) {
			damageTaken = damageTaken - this.defense;
			damageTaken = (damageTaken < 0) ? 0 : damageTaken;
			gp.ui.addMessage(damageTaken + " damage!");
			this.currentLife -= damageTaken;
			gp.playSoundEffect(4);
			this.invincible = true;
		}
	}
	
	public void update() {
		double currentX = this.worldPosition.x;
		double currentY = this.worldPosition.y;
		startMove();
		collisionOn = false;
		try {
			if(!walkthroughWalls) gp.collisionHandler.collideTile(this);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Tile ERrror. " + this.getClass().getName());
		}
		if(!(this instanceof Player)) {
			this.contactPlayer();
			gp.collisionHandler.collideEntity(this, gp.npcs);
			gp.collisionHandler.collideEntity(this, gp.monsters);
			gp.collisionHandler.collideEntity(this, gp.obstacles);
			gp.collisionHandler.collideObject(this, false);
		}
		if(!this.collisionOn && isMoving) {
			switch(direction) {
			case "up": this.worldPosition.y -= this.movementSpeed; break;
			case "down": this.worldPosition.y += this.movementSpeed; break;
			case "left": this.worldPosition.x -= this.movementSpeed; break;
			case "right": this.worldPosition.x += this.movementSpeed; break;
			}
		}
		
		// Sprite Animation Moving
		if( ((this.worldPosition.x - currentX) == 0) && ((this.worldPosition.y - currentY) == 0)) {
		}
		else {
			spriteCounter++;
			if(spriteCounter > 10) {
				if(spriteNum == 1) spriteNum = 2;
				else if(spriteNum == 2) spriteNum = 1;
				spriteCounter = 0;
			}
		}
		
		if (invincible) {
			invincibleCount++;
			if (invincibleCount > 40) {
				invincible = false;
				invincibleCount = 0;
			}
		}
	}
	
	public void draw(Graphics2D g2) {
		Vector2D screenView = new Vector2D(worldPosition.x - gp.player.worldPosition.x + gp.player.screen.x, worldPosition.y - gp.player.worldPosition.y + gp.player.screen.y);
		// To remove black spots when chunk loading.
		final int screenOffset = 2;
		
		// Only load tiles to the visible tiles in the player's screen.
		if(worldPosition.x + (gp.tileSize * screenOffset) > gp.player.worldPosition.x - gp.player.screen.x && 
		   worldPosition.x - (gp.tileSize * screenOffset) < gp.player.worldPosition.x + gp.player.screen.x &&
		   worldPosition.y + (gp.tileSize * screenOffset) > gp.player.worldPosition.y - gp.player.screen.y &&
		   worldPosition.y - (gp.tileSize * screenOffset) < gp.player.worldPosition.y + gp.player.screen.y) {
			Vector2D tempScreen = new Vector2D(screenView.x, screenView.y);
			
			switch(direction) {
			case "up":
				if (!attacking) {
					if (spriteNum == 1) image = up1;
					if (spriteNum == 2) image = up2;
				}
				if (attacking) {
					tempScreen.y = screenView.y - gp.tileSize;
					if (spriteNum == 1) image = attack_up1;
					if (spriteNum == 2) image = attack_up2;
				}
				break;
			case "down":
				if (!attacking) {
					if (spriteNum == 1) image = down1;
					if (spriteNum == 2) image = down2;
				}
				if (attacking) {
					if (spriteNum == 1) image = attack_down1;
					if (spriteNum == 2) image = attack_down2;
				}
				break;
			case "right":
				if (!attacking) {
					if (spriteNum == 1) image = right1;
					if (spriteNum == 2) image = right2;
				}
				if (attacking) {
					if (spriteNum == 1) image = attack_right1;
					if (spriteNum == 2) image = attack_right2;
				}
				break;
			case "left":
				if (!attacking) {
					if (spriteNum == 1) image = left1;
					if (spriteNum == 2) image = left2;
				}
				if (attacking) {
					tempScreen.x = screenView.x - gp.tileSize;
					if (spriteNum == 1) image = attack_left1;
					if (spriteNum == 2) image = attack_left2;
				}
				break;
			default:
				break;
			}
			
			if (type == 2 && showHealthBar) {
				double oneScale = (double) gp.tileSize / maxLife;
				double healthBar = oneScale * currentLife;
				int showSeconds = 4;
				g2.setColor(new Color(35, 35, 35));
				g2.fillRect((int) screenView.x - 1, (int) screenView.y - 16, gp.tileSize + 2, 12);
				
				g2.setColor(new Color(255, 0, 30));
				g2.fillRect((int) screenView.x, (int) screenView.y - 15, (int) healthBar, 10);
				
				healthBarCounter++;
				if (healthBarCounter > showSeconds * 60) {
					healthBarCounter = 0;
					showHealthBar = false;
				}
			}
			if (invincible && invincibleCount % 5 == 0) {
				showHealthBar = true;
				healthBarCounter = 0;
				GlobalTool.utilityTool.changeAlpha(g2, 0.3f);
			}
			if (isDying) {
				dyingAnimation(g2);
			}
			g2.drawImage(image, (int) tempScreen.x, (int) tempScreen.y, null);
			GlobalTool.utilityTool.changeAlpha(g2, 1f);
		}
	}
	
	public void dyingAnimation(Graphics2D g2) {
		int startInterval = 5;
		dyingCounter++;
		if (dyingCounter <= startInterval) GlobalTool.utilityTool.changeAlpha(g2, 0f);
		if (dyingCounter > startInterval * 1 && dyingCounter <= startInterval * 2) GlobalTool.utilityTool.changeAlpha(g2, 1f);
		if (dyingCounter > startInterval * 2 && dyingCounter <= startInterval * 3) GlobalTool.utilityTool.changeAlpha(g2, 0f);
		if (dyingCounter > startInterval * 3 && dyingCounter <= startInterval * 4) GlobalTool.utilityTool.changeAlpha(g2, 1f);
		if (dyingCounter > startInterval * 4 && dyingCounter <= startInterval * 5) GlobalTool.utilityTool.changeAlpha(g2, 0f);
		if (dyingCounter > startInterval * 5 && dyingCounter <= startInterval * 6) GlobalTool.utilityTool.changeAlpha(g2, 1f);
		if (dyingCounter > startInterval * 6 && dyingCounter <= startInterval * 7) GlobalTool.utilityTool.changeAlpha(g2, 0f);
		if (dyingCounter > startInterval * 7 && dyingCounter <= startInterval * 8) GlobalTool.utilityTool.changeAlpha(g2, 1f);
		
		if (dyingCounter > startInterval * 8) {
			isDying = false;
			isAlive = false;
		}
	}
}
