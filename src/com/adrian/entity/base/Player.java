package com.adrian.entity.base;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.adrian.base.Global;
import com.adrian.entity.projectiles.Fireball;
import com.adrian.inputs.KeyHandler;
import com.adrian.inventory.Inventory;
import com.adrian.items.Key;
import com.adrian.items.base.Coin;
import com.adrian.items.base.Consumable;
import com.adrian.items.base.Item;
import com.adrian.items.base.ItemTypes;
import com.adrian.items.base.Shield;
import com.adrian.items.base.Weapon;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.user_interfaces.GameState;
import com.adrian.utils.CharacterClass;
import com.adrian.utils.Sound;
import com.adrian.utils.Vector2DUtil;

public class Player extends Entity {
	
	GamePanel gp;
	KeyHandler keyInput;
	
	public Vector2DUtil screen;
	public String selectedClass;
	
	// Character Equipment
	public Weapon currentWeapon;
	public Shield currentShield;
	public Inventory inventory;
	
	public Player(GamePanel gp, KeyHandler keyInput, Vector2DUtil position, String selectedClass) {
		super(gp);
		this.screen = new Vector2DUtil(
				gp.screenWidth / 2 - (gp.tileSize), 
				gp.screenHeight / 2 - (gp.tileSize));
		
		this.gp = gp;
		this.keyInput = keyInput;
		this.worldPosition = position;
		this.selectedClass = selectedClass;
		
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
		this.maxMana = 5;
		this.currentMana = maxMana;
		this.direction = "down";
		this.attackArea = new Rectangle(0, 0, 36, 36);
		
		this.level = 1;
		this.strength = 1;
		this.dexterity = 1;
		this.exp = 0;
		this.nextLevelExp = 20;
		this.coin = 0;
		this.inventory = new Inventory(gp);
		this.currentWeapon = this.inventory.getItem(0);
		this.currentShield = this.inventory.getItem(1);
		this.attack = getAttackStat();
		this.defense = getDefenseStat();
		this.projectile = new Fireball(gp);
		this.classSelection();
	}
	
	// PUT THIS SOMEWHERE ELSE like PlayerStats Manager
	private int getAttackStat() {
		attackArea = currentWeapon.attackArea;
		return attack = strength * currentWeapon.attackValue;
	}
	
	private int getDefenseStat() {
		return defense = dexterity * currentShield.defenseValue;
	}
	
	private void classSelection() {
		if(this.selectedClass == CharacterClass.Selection.WARRIOR.name()) {
			this.maxLife = 12;
			this.currentLife = this.maxLife;
		}
		if(this.selectedClass == CharacterClass.Selection.MAGE.name()) {
			this.maxLife = 6;
			this.currentLife = this.maxLife;
		}
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
			Sound.LEVELUP.playSE();
			gp.gameState = GameState.Dialogue.state;
			gp.ui.currentDialogue = "\tYou are level " + this.level + " now!";
		}
	}
	
	public <T extends Item> void selectedItem() {
		
		int itemIndex = this.inventory.getItemIndex();
		if(itemIndex < this.inventory.items.size()) {
			T selectedItem = this.inventory.getItem(itemIndex);
			if(selectedItem.type == ItemTypes.Weapon.class) {
				this.currentWeapon = (Weapon) selectedItem;
				this.attack = getAttackStat();
				this.getAttackSprites();
			}
			if(selectedItem.type == ItemTypes.Shield.class) {
				this.currentShield = (Shield) selectedItem;
				this.defense = getDefenseStat();
			}
			
			if(selectedItem.type == ItemTypes.Consumable.class) {
				((Consumable) selectedItem).useItem(this);
				this.inventory.removeItem(selectedItem);
				if(!this.inventory.hasItem(selectedItem)) {
					gp.gameState = GameState.Continue.state;
				}
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
		
		else if(keyInput.haveKeyPressed.get("F") && 
				!projectile.isAlive && 
				this.projectileCooldown == this.projectileMaxCooldown && 
				this.selectedClass == CharacterClass.Selection.MAGE.name() &&
				this.projectile.haveResource(this)
				) {
			projectile.set(this.worldPosition.x, this.worldPosition.y, this.direction, true, this);
			projectile.substractResource(this);
			this.projectileCooldown = 0;
			gp.projectileList.add(projectile);
//			gp.playSoundEffect(11);
			Sound.MAGIC.playSE();;
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
				!keyInput.haveKeyPressed.get("F") &&
				!keyInput.haveKeyPressed.get("ENTER")) {
			isMoving = false;
			attacking = false;
		}
	}
	
	private void gameOver() {
		if(this.currentLife <= 0 && gp.gameState == GameState.Continue.state) {
			Global.util.Sound.stop();
			gp.gameState = GameState.Menu.state;
			gp.ui.titleScreenState = 0;
			gp.ui.titleScreen = "Game Over";
			Sound.GAMEOVER.playSE();
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
				if ((max + min) / 2 == spriteCounter) getDamageFromMonster(entityIndex, this.attack);
				
				int interactiveTileIndex = gp.collisionHandler.collideEntity(this, gp.interactableTiles);
				if ((max + min) / 2 == spriteCounter) interactTile(interactiveTileIndex);
				
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
		if(obtainedItem.type == ItemTypes.NotObtainable.class) {
			int coinValue = ((Coin) obtainedItem).value;
			this.coin += coinValue;
			gp.ui.addMessage("Coin +" + coinValue);
			Sound.COIN.playSE();
			gp.itemObjects[index] = null;
		}
		
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
						Sound.UNLOCK.playSE();
//						gp.playSoundEffect(3);
						entity.setDialogue("Unlocked the door.");
						entities[index] = null;
						gp.player.inventory.popItem(key);
					} else {
						Sound.SPEAK.playSE();
//						gp.playSoundEffect(6);
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
	
	private void interactTile(int index) {
		if(index == 999 || !gp.interactableTiles[index].isDestructible || !gp.interactableTiles[index].isItemValid(this, ItemTypes.Weapon.Axe.class)) return;
		gp.interactableTiles[index].currentLife--;
		gp.interactableTiles[index].invincible = true;
		gp.interactableTiles[index].generateParticle(
				gp.interactableTiles[index], 
				gp.interactableTiles[index]
		);
		if(gp.interactableTiles[index].currentLife == 0) {
			gp.interactableTiles[index] = gp.interactableTiles[index].getDestroyedForm();
		}
	}
	
	protected void contactMonster(int index) {
		if (index == 999) return;
		Entity entity = gp.monsters[index];
		if(!invincible && !entity.invincible) {
			switch (entity.name) {
			case "Green Slime":
				this.takeDamage(entity.attack);
				Sound.PLAYER_HURT.playSE();
//				gp.playSoundEffect(5);
				invincible = true;
				break;
			}
		}
	}
	
	// Put in Entity.class
	public void getDamageFromMonster(int index, int damageTaken) {
		if (index == 999) return;
		Entity entity = gp.monsters[index];
		switch (entity.name) {
		case "Green Slime":
			entity.takeDamage(damageTaken);
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
			gp.collisionHandler.collideEntity(this, gp.interactableTiles);
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
		if(this.currentWeapon.weaponType == ItemTypes.Weapon.Sword.class) {
			attack_up1 = loadSprite("player\\attack\\boy_attack_up_1.png", gp.tileSize, gp.tileSize * 2);
			attack_up2 = loadSprite("player\\attack\\boy_attack_up_2.png", gp.tileSize, gp.tileSize * 2);
			attack_down1 = loadSprite("player\\attack\\boy_attack_down_1.png", gp.tileSize, gp.tileSize * 2);
			attack_down2 = loadSprite("player\\attack\\boy_attack_down_2.png", gp.tileSize, gp.tileSize * 2);
			attack_right1 = loadSprite("player\\attack\\boy_attack_right_1.png", gp.tileSize * 2, gp.tileSize);
			attack_right2 = loadSprite("player\\attack\\boy_attack_right_2.png", gp.tileSize * 2, gp.tileSize);
			attack_left1 = loadSprite("player\\attack\\boy_attack_left_1.png", gp.tileSize * 2, gp.tileSize);
			attack_left2 = loadSprite("player\\attack\\boy_attack_left_2.png", gp.tileSize * 2, gp.tileSize);
		}
		if(this.currentWeapon.weaponType == ItemTypes.Weapon.Axe.class) {
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
		Vector2DUtil tempScreen = new Vector2DUtil(screen.x, screen.y);
		
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
