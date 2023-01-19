package com.adrian.entity.base;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.adrian.base.Global;
import com.adrian.items.base.Item;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.utils.Sound;
import com.adrian.utils.Vector2DUtil;

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
	protected Projectile projectile;
	
	public String name;
	public Vector2DUtil worldPosition;
	
	public int maxLife;
	public int currentLife = 0;
	public int maxMana;
	public int currentMana = 0;
	public double movementSpeed;
	public String direction = "down";
	public int type;
	
	// Attributes
	public String id;
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
	protected int projectileCooldown;
	protected final int projectileMaxCooldown = 30;
	
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
		this.id = UUID.randomUUID().toString();
	}
	
	protected abstract void getSprite();
	protected BufferedImage loadSprite(final String imagePath, int width, int height) {
		image = null;
		try {
			image = ImageIO.read(new File(Global.assets + imagePath));
			image = Global.util.scaleImage(image, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	public Color getParticleColor() {
		return null;
	}
	
	public int getParticleSize() {
		return 0;
	}
	
	public int getParticleSpeed() {
		return 0;
	}
	
	public int getParticleMaxLife() {
		return 0;
	}
	
	public void generateParticle(Entity generator, Entity target) {
		Color color = generator.getParticleColor();
		int size = generator.getParticleSize();
		int speed = generator.getParticleSpeed();
		int maxLife = generator.getParticleMaxLife();
		
		gp.particleList.add(new Particle(gp, target, color, size, speed, maxLife, -2, -1));
		gp.particleList.add(new Particle(gp, target, color, size, speed, maxLife, 2, -1));
		gp.particleList.add(new Particle(gp, target, color, size, speed, maxLife, -2, 1));
		gp.particleList.add(new Particle(gp, target, color, size, speed, maxLife, 2, 1));
	}
	
	// Set Dialogue Speech to NPC / Obstacles
	protected void setDialogue() {}
	protected void setDialogue(String text) {}
	
	protected void trigger() {}
	protected void damageReaction() {}
	protected void startMove() {}
	
	protected void contactPlayer() {
		boolean contactPlayer = gp.collisionHandler.collidePlayer(this);
		if (this instanceof Monster && contactPlayer == true) {
			this.damagePlayer(this.attack);
		}
	}
	
	protected void damagePlayer(int damageTaken) {
		if(!gp.player.invincible) {
			gp.player.takeDamage(damageTaken);
		}
	}
	
	public void takeDamage(int damageTaken) {
		if(!this.invincible) {
			damageTaken = damageTaken - this.defense;
			damageTaken = (damageTaken < 0) ? 0 : damageTaken;
			gp.ui.addMessage(damageTaken + " damage!");
			this.currentLife -= damageTaken;
			Sound.PLAYER_HURT.playSE();
			this.invincible = true;
		}
	}
	
	public void update() {
		double currentX = this.worldPosition.x;
		double currentY = this.worldPosition.y;
		startMove();
		collisionOn = false;
		try {
			gp.collisionHandler.collideTile(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!(this instanceof Player)) {
			this.contactPlayer();
			gp.collisionHandler.collideEntity(this, gp.npcs);
			gp.collisionHandler.collideEntity(this, gp.monsters);
			gp.collisionHandler.collideEntity(this, gp.obstacles);
			gp.collisionHandler.collideEntity(this, gp.interactableTiles);
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
		
		if(projectileCooldown < this.projectileMaxCooldown) {
			projectileCooldown++;
		}
		
		if(this.currentLife > this.maxLife) this.currentLife = this.maxLife;
		if(this.currentMana > this.maxMana) this.currentMana = this.maxMana;
	}
	
	public void draw(Graphics2D g2) {
		Vector2DUtil screenView = new Vector2DUtil(worldPosition.x - gp.player.worldPosition.x + gp.player.screen.x, worldPosition.y - gp.player.worldPosition.y + gp.player.screen.y);
		// To remove black spots when chunk loading.
		final int screenOffset = 2;
		
		// Only load tiles to the visible tiles in the player's screen.
		if(worldPosition.x + (gp.tileSize * screenOffset) > gp.player.worldPosition.x - gp.player.screen.x && 
		   worldPosition.x - (gp.tileSize * screenOffset) < gp.player.worldPosition.x + gp.player.screen.x &&
		   worldPosition.y + (gp.tileSize * screenOffset) > gp.player.worldPosition.y - gp.player.screen.y &&
		   worldPosition.y - (gp.tileSize * screenOffset) < gp.player.worldPosition.y + gp.player.screen.y) {
			Vector2DUtil tempScreen = new Vector2DUtil(screenView.x, screenView.y);
			
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
			
			if (this instanceof Monster && showHealthBar) {
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
				Global.util.changeAlpha(g2, 0.3f);
			}
			if (isDying) {
				dyingAnimation(g2);
			}
			g2.drawImage(image, (int) tempScreen.x, (int) tempScreen.y, null);
			Global.util.changeAlpha(g2, 1f);
		}
	}
	
	protected <T extends Item> void dropItem(T droppedItem) {
		for(int i = 0; i < gp.itemObjects.length; i++) {
			if(gp.itemObjects[i] == null) {
				gp.itemObjects[i] = droppedItem;
				gp.itemObjects[i].worldPosition = this.worldPosition;
				break;
			}
		}
	}
	
	protected void checkDrop() {}
	
	public void dyingAnimation(Graphics2D g2) {
		int startInterval = 5;
		dyingCounter++;
		if (dyingCounter <= startInterval) Global.util.changeAlpha(g2, 0f);
		if (dyingCounter > startInterval * 1 && dyingCounter <= startInterval * 2) Global.util.changeAlpha(g2, 1f);
		if (dyingCounter > startInterval * 2 && dyingCounter <= startInterval * 3) Global.util.changeAlpha(g2, 0f);
		if (dyingCounter > startInterval * 3 && dyingCounter <= startInterval * 4) Global.util.changeAlpha(g2, 1f);
		if (dyingCounter > startInterval * 4 && dyingCounter <= startInterval * 5) Global.util.changeAlpha(g2, 0f);
		if (dyingCounter > startInterval * 5 && dyingCounter <= startInterval * 6) Global.util.changeAlpha(g2, 1f);
		if (dyingCounter > startInterval * 6 && dyingCounter <= startInterval * 7) Global.util.changeAlpha(g2, 0f);
		if (dyingCounter > startInterval * 7 && dyingCounter <= startInterval * 8) Global.util.changeAlpha(g2, 1f);
		
		if (dyingCounter > startInterval * 8) {
			isDying = false;
			isAlive = false;
			this.checkDrop();;
		}
	}
}
