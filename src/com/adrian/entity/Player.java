package com.adrian.entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.adrian.inputs.KeyHandler;
import com.adrian.user_interface.GamePanel;
import com.adrian.user_interface.GameState;
import com.adrian.utils.Vector2D;

public class Player extends Entity {
	
	GamePanel gp;
	KeyHandler keyInput;
	
	public Vector2D screen;
	public int keyCount = 0;
	
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
		this.attackArea = new Rectangle(0, 0, 36, 36);
		
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
		
		else if(keyInput.haveKeyPressed.get("SPACE")) {
			switch (direction) {
			case "up":
				this.worldPosition.y -= (gp.tileSize * 1);
				break;
			case "down":
				this.worldPosition.y += (gp.tileSize * 1);
				break;
			case "left":
				this.worldPosition.x -= (gp.tileSize * 1);
				break;
			case "right":
				this.worldPosition.x += (gp.tileSize * 1);
				break;
			}
		}
		
		else if(!keyInput.haveKeyPressed.get("W") &&
				!keyInput.haveKeyPressed.get("S") &&
				!keyInput.haveKeyPressed.get("A") &&
				!keyInput.haveKeyPressed.get("D") &&
				!keyInput.haveKeyPressed.get("ENTER")) {
			isMoving = false;
			attacking = false;
		}
	}
	
	private void checkDied() {
		if(this.currentLife <= 0 && gp.gameState == GameState.Continue.state) {
			gp.stopMusic();
			
			gp.gameState = GameState.Menu.state;
			gp.ui.titleScreenState = 0;
			gp.ui.titleScreen = "Game Over";
			gp.playSoundEffect(8);
			gp.canPlay = false;
		}
	}
	
	private void executeAttack() {
		// Good for attack speed
		int min = 5; // 5
		int max = 25; // 25
		if (attacking) {
			isMoving = false;
			spriteCounter++;
			if (spriteCounter <= min) {
				spriteNum = 1;
			}
			if (spriteCounter > min && spriteCounter <= max) {
				spriteNum = 2;
				
				Rectangle attackArea = new Rectangle((int) worldPosition.x, (int) worldPosition.y, solidArea.width, solidArea.height);
				int attackRange = 1;
				
				switch (direction) {
				case "up": worldPosition.y -= (attackArea.height * attackRange); break;
				case "down": worldPosition.y += (attackArea.height * attackRange); break;
				case "left": worldPosition.x -= (attackArea.width * attackRange); break;
				case "right": worldPosition.x += (attackArea.width * attackRange); break;
				}
				
				solidArea.width = attackArea.width;
				solidArea.height = attackArea.height;
				
				int entityIndex = gp.collisionHandler.collideEntity(this, gp.monsters);
				if ((max + min) / 2 == spriteCounter) {
					damageMonster(entityIndex);
				}
				
				worldPosition.x = attackArea.x;
				worldPosition.y = attackArea.y;
				solidArea.width = attackArea.width;
				solidArea.height = attackArea.height;
				
			}
			if (spriteCounter > max) {
				spriteNum = 1;
				spriteCounter = 0;
				attacking = false;
				keyInput.haveKeyPressed.replace("ENTER", false);
			}
		}
	}
	
	@Override
	protected void startMove() {
		checkDied();
		plugController();
		executeAttack();
		gp.eventHandler.checkEvent();
		int objectIndex = gp.collisionHandler.collideObject(this, true);
		try{
			int npcIndex = gp.collisionHandler.collideEntity(this, gp.npcs);
			int obstacleIndex = gp.collisionHandler.collideEntity(this, gp.obstacles);
			int monsterIndex = gp.collisionHandler.collideEntity(this, gp.monsters);
			contactMonster(monsterIndex);
			interactEntity(npcIndex, gp.npcs);
			interactEntity(obstacleIndex, gp.obstacles);
			interactEntity(monsterIndex, gp.monsters);
		} catch (NullPointerException e) {
			System.out.println("Not initialized. " + this.getClass().getName());
		}
		pickUpObject(objectIndex);
		return;
	}
	
	public void pickUpObject(int index) {
		if(index == 999) return;
		// Should
		int findIndex = gp.itemObjects[index].getClass().getName().lastIndexOf(".") + 1;
		String objectCode = gp.itemObjects[index].getClass().getName();
		objectCode = objectCode.substring(findIndex, objectCode.length());
		switch(objectCode) {
		case "Key":
			gp.ui.currentDialogue = "You obtained a " + objectCode;
			gp.playSoundEffect(7);
			gp.gameState = GameState.Dialogue.state;
			this.keyCount++;
			gp.itemObjects[index] = null;
			break;
		case "Boots":
			gp.ui.currentDialogue = "You obtained a " + objectCode;
			gp.playSoundEffect(7);
			gp.gameState = GameState.Dialogue.state;
			gp.itemObjects[index] = null;
			break;
		}
	}
	
	public void interactEntity(int index, Entity[] entities) {
		if (keyInput.haveKeyPressed.get("ENTER")) {
			if(index != 999) {
				attacking = false;
				Entity entity = entities[index];
				switch (entity.name) {
				case "Door":
					if(this.keyCount > 0) {
						gp.ui.currentDialogue = "You unlocked the door.";
						gp.gameState = GameState.Dialogue.state;
						entity.trigger();
						entities[index] = null;
						gp.playSoundEffect(3);
						keyCount--;
					} else {
						gp.ui.currentDialogue = "Locked Door. You need 1 key\nto open this door.";
						gp.gameState = GameState.Dialogue.state;
					}
					break;
				case "NPC":
					gp.gameState = GameState.Dialogue.state;
					// FLIP Dialogue of NPCs
//					gp.ui.currentDialogue = entity.dialogues[dialogueIndex];
//					entity.dialogueIndex++;
//					dialogueIndex %= 3;
					System.out.println(dialogueIndex);
					gp.playSoundEffect(6);
					break;
				}
				keyInput.haveKeyPressed.replace("ENTER", false);
			} else {
				attacking = true;
				return;
			}
		}
	}
	
	public void contactMonster(int index) {
		if (index == 999) return;
		if(!invincible) {
			currentLife -= 1;
			gp.playSoundEffect(5);
			invincible = true;
		}
	}
	
	public void damageMonster(int index) {
		if (index == 999) {
			System.out.println("Miss");
			return;
		}
		
		Entity entity = gp.monsters[index];
		if(!entity.invincible) {
			if(entity.currentLife <= 0) {
				gp.monsters[index] = null;
			}
			System.out.println("HP :" + entity.currentLife);
			entity.currentLife--;
			gp.playSoundEffect(4);
			entity.invincible = true;
		}
		
		
	}
	
	@Override
	protected void getSprite() {
		getAttackSprites();
		getMoveSprites();
		
	}
	
	private void getAttackSprites() {
		attack_up1 = loadSprite("player\\attack\\boy_attack_up_1.png", gp.tileSize, gp.tileSize * 2);
		attack_up2 = loadSprite("player\\attack\\boy_attack_up_2.png", gp.tileSize, gp.tileSize * 2);
		attack_down1 = loadSprite("player\\attack\\boy_attack_down_1.png", gp.tileSize, gp.tileSize * 2);
		attack_down2 = loadSprite("player\\attack\\boy_attack_down_2.png", gp.tileSize, gp.tileSize * 2);
		attack_right1 = loadSprite("player\\attack\\boy_attack_right_1.png", gp.tileSize * 2, gp.tileSize);
		attack_right2 = loadSprite("player\\attack\\boy_attack_right_2.png", gp.tileSize * 2, gp.tileSize);
		attack_left1 = loadSprite("player\\attack\\boy_attack_left_1.png", gp.tileSize * 2, gp.tileSize);
		attack_left2 = loadSprite("player\\attack\\boy_attack_left_2.png", gp.tileSize * 2, gp.tileSize);
	}
	
	private void getMoveSprites() {
		up1 = loadSprite("player\\move\\boy_up_1.png", gp.tileSize, gp.tileSize);
		up2 = loadSprite("player\\move\\boy_up_2.png", gp.tileSize, gp.tileSize);
		down1 = loadSprite("player\\move\\boy_down_1.png", gp.tileSize, gp.tileSize);
		down2 = loadSprite("player\\move\\boy_down_2.png", gp.tileSize, gp.tileSize);
		right1 = loadSprite("player\\move\\boy_right_1.png", gp.tileSize, gp.tileSize);
		right2 = loadSprite("player\\move\\boy_right_2.png", gp.tileSize, gp.tileSize);
		left1 = loadSprite("player\\move\\boy_left_1.png", gp.tileSize, gp.tileSize);
		left2 = loadSprite("player\\move\\boy_left_2.png", gp.tileSize, gp.tileSize);
	}
	
	public void draw(Graphics2D g) {
		image = null;
		Vector2D tempScreen = new Vector2D(screen.x, screen.y);
		
		switch(direction) {
		case "up":
			if (!attacking) {
				if (spriteNum == 1) image = up1;
				if (spriteNum == 2) image = up2;
			}
			if (attacking) {
				tempScreen.y = screen.y - gp.tileSize;
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
				tempScreen.x = screen.x - gp.tileSize;
				if (spriteNum == 1) image = attack_left1;
				if (spriteNum == 2) image = attack_left2;
			}
			break;
		default:
			break;
		}
		if (invincible && invincibleCount % 5 == 0) g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		g.drawImage(image, (int) tempScreen.x, (int) tempScreen.y, null);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}

	@Override
	public void trigger() {
		// TODO Auto-generated method stub
		
	}
}
