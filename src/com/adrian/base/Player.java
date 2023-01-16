package com.adrian.base;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.adrian.inputs.KeyHandler;
import com.adrian.inventory.Inventory;
import com.adrian.items.Key;
import com.adrian.types.ItemType;
import com.adrian.types.WeaponType;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.user_interfaces.GameState;
import com.adrian.utils.Vector2D;

public class Player extends Entity {
	
	GamePanel gp;
	KeyHandler keyInput;
	
	public Vector2D screen;
	
	// Character Equipment
	public Weapon currentWeapon;
	public Shield currentShield;
	public Inventory inventory;
	
	public Player(GamePanel gp, KeyHandler keyInput, Vector2D position) {
		super(gp);
		this.screen = new Vector2D(
				gp.screenWidth / 2 - (gp.tileSize), 
				gp.screenHeight / 2 - (gp.tileSize));
		
		this.gp = gp;
		this.keyInput = keyInput;
		this.worldPosition = position;
		
		// Default Values
		this.defaultValue();
 		
		this.solidArea = new Rectangle(8, 16, 28, 28); // IMPROVEMENT: Use percentage to calculate the rect for responsiveness.
		solidAreaDefaultX = this.solidArea.x;
		solidAreaDefaultY = this.solidArea.y;
		this.getSprite();
	}
	
	public void defaultValue() {
		this.movementSpeed = 4;
		this.maxLife = 6;
		this.currentLife = maxLife;
		this.direction = "down";
		this.attackArea = new Rectangle(0, 0, 36, 36);
		
		this.level = 1;
		this.strength = 1;
		this.dexterity = 1;
		this.exp = 0;
		this.nextLevelExp = 5;
		this.coin = 0;
		this.inventory = new Inventory(gp);
		this.currentWeapon = this.inventory.getItem(0);
		this.currentShield = this.inventory.getItem(1);
		this.attack = getAttackStat();
		this.defense = getDefenseStat();
	}
	
	
	// PUT THIS SOMEWHERE ELSE like PlayerStats Manager
	private int getAttackStat() {
		attackArea = currentWeapon.attackArea;
		return attack = strength * currentWeapon.attackValue;
	}
	
	private int getDefenseStat() {
		return defense = dexterity * currentShield.defenseValue;
	}
	
	private void checkLevelUp() {
		if(exp >= nextLevelExp) {
			level++;
			nextLevelExp *= 2;
			maxLife += 2;
			strength++;
			dexterity++;
			attack = getAttackStat();
			defense = getDefenseStat();
			gp.playSoundEffect(7);
			
			gp.gameState = GameState.Dialogue.state;
			gp.ui.currentDialogue = "\tYou are level " + this.level + " now!";
		}
	}
	
	public <T extends Item> void selectedItem() {
		int itemIndex = this.inventory.getItemIndex();
		if(itemIndex < this.inventory.items.size()) {
			T selectedItem = this.inventory.getItem(itemIndex);
			if(selectedItem.type == ItemType.Weapon) {
				this.currentWeapon = (Weapon) selectedItem;
				this.attack = getAttackStat();
				this.getAttackSprites();
			}
			if(selectedItem.type == ItemType.Shield) {
				this.currentShield = (Shield) selectedItem;
				this.defense = getDefenseStat();
			}
			
			if(selectedItem.type == ItemType.Consumable) {
				((Consumable) selectedItem).useItem(this);
				this.inventory.removeItem(selectedItem);
			}
		}
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
	
	private void gameOver() {
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
				if ((max + min) / 2 == spriteCounter) getDamageFromMonster(entityIndex);
				
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
	
	private void pickUpObject(int index) {
		if(index == 999) return;
		Item obtainedItem = gp.itemObjects[index];
		String text;
		switch(obtainedItem.name) {
		case "Red Potion":
			text = "You obtained a " + obtainedItem.name + "!";
			if(this.inventory.isFull()) {
				text = "Bag is full.";
				obtainedItem.setDialogue(text);
			} else {
				obtainedItem.setDialogue(text);
				this.inventory.addItem(obtainedItem);
			}
			gp.itemObjects[index] = null;
			break;
		case "Key":
			text = "You obtained a " + obtainedItem.name + "!";
			if(this.inventory.isFull()) {
				text = "Bag is full.";
				obtainedItem.setDialogue(text);
			} else {
				obtainedItem.setDialogue(text);
				this.inventory.addItem(obtainedItem);
			}
			gp.itemObjects[index] = null;
			break;
		case "Boots":
			text = "You obtained a " + obtainedItem.name + "!";
			if(this.inventory.isFull()) {
				text = "Bag is full.";
				obtainedItem.setDialogue(text);
			} else {
				obtainedItem.setDialogue(text);
				this.inventory.addItem(obtainedItem);
			}
			gp.itemObjects[index] = null;
			break;
		case "Old Axe":
			text = "You obtained a " + obtainedItem.name + "!";
			if(this.inventory.isFull()) {
				text = "Bag is full.";
				obtainedItem.setDialogue(text);
			} else {
				obtainedItem.setDialogue(text);
				this.inventory.addItem(obtainedItem);
			}
			gp.itemObjects[index] = null;
			break;
		case "Blue Shield":
			text = "You obtained a " + obtainedItem.name + "!";
			if(this.inventory.isFull()) {
				text = "Bag is full.";
				obtainedItem.setDialogue(text);
			} else {
				obtainedItem.setDialogue(text);
				this.inventory.addItem(obtainedItem);
			}
			gp.itemObjects[index] = null;
			break;
		}
	}
	
	private void interactEntity(int index, Entity[] entities) {
		if (keyInput.haveKeyPressed.get("ENTER")) {
			if(index != 999) {
				attacking = false;
				Entity entity = entities[index];
				switch (entity.name) {
				case "Door":
					Key key = new Key(this.gp);
					if(this.inventory.hasItem(key)) {
						gp.playSoundEffect(3);
						entity.setDialogue("Unlocked the door.");
						entities[index] = null;
						Item item = gp.player.inventory.popItem(key);
					} else {
						gp.playSoundEffect(6);
						entity.setDialogue("Locked Door. You need 1 key\nto open this door.");
					}
					entity.trigger();
					break;
				case "NPC":
					entity.setDialogue();
					entity.trigger();
					break;
				}
				keyInput.haveKeyPressed.replace("ENTER", false);
			} else {
				attacking = true;
				return;
			}
		}
	}
	
	protected void contactMonster(int index) {
		if (index == 999) return;
		Entity entity = gp.monsters[index];
		if(!invincible && !entity.invincible) {
			switch (entity.name) {
			case "Green Slime":
				this.takeDamage(entity.attack);
				gp.playSoundEffect(5);
				invincible = true;
				break;
			}
		}
	}
	
	private void getDamageFromMonster(int index) {
		if (index == 999) return;
		Entity entity = gp.monsters[index];
		
		switch (entity.name) {
		case "Green Slime":
			entity.takeDamage(this.attack);
			entity.damageReaction();
			break;
		}
		if(entity.currentLife <= 0) {
			gp.monsters[index].isDying = true;
			gp.ui.addMessage("killed the " + gp.monsters[index].name + "!");
			gp.ui.addMessage("Gained " + gp.monsters[index].exp + " exp!");
			exp += entity.exp;
			checkLevelUp();
		}
	}
	
	@Override
	protected void startMove() {
		gameOver();
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
	
	@Override
	protected void getSprite() {
		getAttackSprites();
		getMoveSprites();
		
	}
	
	private void getAttackSprites() {
		if(this.currentWeapon.weaponType == WeaponType.Sword) {
			attack_up1 = loadSprite("player\\attack\\boy_attack_up_1.png", gp.tileSize, gp.tileSize * 2);
			attack_up2 = loadSprite("player\\attack\\boy_attack_up_2.png", gp.tileSize, gp.tileSize * 2);
			attack_down1 = loadSprite("player\\attack\\boy_attack_down_1.png", gp.tileSize, gp.tileSize * 2);
			attack_down2 = loadSprite("player\\attack\\boy_attack_down_2.png", gp.tileSize, gp.tileSize * 2);
			attack_right1 = loadSprite("player\\attack\\boy_attack_right_1.png", gp.tileSize * 2, gp.tileSize);
			attack_right2 = loadSprite("player\\attack\\boy_attack_right_2.png", gp.tileSize * 2, gp.tileSize);
			attack_left1 = loadSprite("player\\attack\\boy_attack_left_1.png", gp.tileSize * 2, gp.tileSize);
			attack_left2 = loadSprite("player\\attack\\boy_attack_left_2.png", gp.tileSize * 2, gp.tileSize);
		}
		if(this.currentWeapon.weaponType == WeaponType.Axe) {
			attack_up1 = loadSprite("player\\attack\\boy_axe_up_1.png", gp.tileSize, gp.tileSize * 2);
			attack_up2 = loadSprite("player\\attack\\boy_axe_up_2.png", gp.tileSize, gp.tileSize * 2);
			attack_down1 = loadSprite("player\\attack\\boy_axe_down_1.png", gp.tileSize, gp.tileSize * 2);
			attack_down2 = loadSprite("player\\attack\\boy_axe_down_2.png", gp.tileSize, gp.tileSize * 2);
			attack_right1 = loadSprite("player\\attack\\boy_axe_right_1.png", gp.tileSize * 2, gp.tileSize);
			attack_right2 = loadSprite("player\\attack\\boy_axe_right_2.png", gp.tileSize * 2, gp.tileSize);
			attack_left1 = loadSprite("player\\attack\\boy_axe_left_1.png", gp.tileSize * 2, gp.tileSize);
			attack_left2 = loadSprite("player\\attack\\boy_axe_left_2.png", gp.tileSize * 2, gp.tileSize);
		}
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
}
