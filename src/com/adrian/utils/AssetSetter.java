package com.adrian.utils;

import java.awt.Graphics2D;

import com.adrian.entity.base.NPC;
import com.adrian.entity.base.Projectile;
import com.adrian.entity.monsters.GreenSlime;
import com.adrian.items.Boots;
import com.adrian.items.BronzeCoin;
import com.adrian.items.Key;
import com.adrian.items.RedPotion;
import com.adrian.items.equipments.BlueShield;
import com.adrian.items.equipments.RustyAxe;
import com.adrian.obstacles.Door;
import com.adrian.user_interfaces.GamePanel;

public class AssetSetter {
	GamePanel gp;
	
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	
	public void setItemObject() {
		gp.itemObjects[0] = new Boots(gp);
		gp.itemObjects[0].worldPosition = new Vector2D(gp.tileSize * 34, gp.tileSize * 42);
		
		gp.itemObjects[1] = new Key(gp);
		gp.itemObjects[1].worldPosition = new Vector2D(gp.tileSize * 37, gp.tileSize * 7);
		
		gp.itemObjects[2] = new Key(gp);
		gp.itemObjects[2].worldPosition = new Vector2D(gp.tileSize * 37, gp.tileSize * 40);
		
		gp.itemObjects[3] = new RustyAxe(gp);
		gp.itemObjects[3].worldPosition = new Vector2D(gp.tileSize * 9, gp.tileSize * 8);
		
		gp.itemObjects[4] = new BlueShield(gp);
		gp.itemObjects[4].worldPosition = new Vector2D(gp.tileSize * 11, gp.tileSize * 8);
		
		gp.itemObjects[5] = new RedPotion(gp);
		gp.itemObjects[5].worldPosition = new Vector2D(gp.tileSize * 23, gp.tileSize * 27);
		
		gp.itemObjects[6] = new BronzeCoin(gp);
		gp.itemObjects[6].worldPosition = new Vector2D(gp.tileSize * 21, gp.tileSize * 19);
		
		gp.itemObjects[7] = new BronzeCoin(gp);
		gp.itemObjects[7].worldPosition = new Vector2D(gp.tileSize * 21, gp.tileSize * 23);
		
		gp.itemObjects[8] = new BronzeCoin(gp);
		gp.itemObjects[8].worldPosition = new Vector2D(gp.tileSize * 25, gp.tileSize * 19);
		
		gp.itemObjects[9] = new BronzeCoin(gp);
		gp.itemObjects[9].worldPosition = new Vector2D(gp.tileSize * 25, gp.tileSize * 23);
		
		gp.itemObjects[10] = new RedPotion(gp);
		gp.itemObjects[10].worldPosition = new Vector2D(gp.tileSize * 23, gp.tileSize * 25);
	}
	
	public void setObstacle() {
		gp.obstacles[0] = new Door(gp);
		gp.obstacles[0].worldPosition = new Vector2D(13 * gp.tileSize, 23 * gp.tileSize);
		
		gp.obstacles[1] = new Door(gp);
		gp.obstacles[1].worldPosition = new Vector2D(10 * gp.tileSize, 12 * gp.tileSize);
	}
	
	public void setNPC() {
		// BUG WHEN 1st Index
		gp.npcs[0] = new NPC(gp, new String[] {
				"I may be an old man, but I was once a great adventurer like you!",
				"I remember as it was like yesterday, wherein I was strong lad like you."
		});
		gp.npcs[2] = new NPC(gp, new String[] {
				"All we have to decide is what to do with the time that is given us.",
				"I will not say: do not weep; for not all tears are an evil."
		}); 
		gp.npcs[0].worldPosition = new Vector2D(gp.tileSize * 23, gp.tileSize * 10);
		gp.npcs[2].worldPosition = new Vector2D(gp.tileSize * 35, gp.tileSize * 8);
	}
	
	public void setMonster() {
		int i = 0;
		
		// Slime Monsters
		gp.monsters[i] = new GreenSlime(gp);
		gp.monsters[i].worldPosition = new Vector2D(gp.tileSize * 20, gp.tileSize * 37);
		i++;
		gp.monsters[i] = new GreenSlime(gp);
		gp.monsters[i].worldPosition = new Vector2D(gp.tileSize * 20, gp.tileSize * 40);
		i++;
		gp.monsters[i] = new GreenSlime(gp);
		gp.monsters[i].worldPosition = new Vector2D(gp.tileSize * 25, gp.tileSize * 40);
		i++;
		gp.monsters[i] = new GreenSlime(gp);
		gp.monsters[i].worldPosition = new Vector2D(gp.tileSize * 25, gp.tileSize * 37);
		i++;
		gp.monsters[i] = new GreenSlime(gp);
		gp.monsters[i].worldPosition = new Vector2D(gp.tileSize * 22, gp.tileSize * 41);
		i++;
	}
	
	public void compressEntities() {
		for(int i = 0; i < gp.obstacles.length; i++) {
			if(gp.obstacles[i] != null) {
				gp.entityList.add(gp.obstacles[i]);
			}
		}
		
		for(int i = 0; i < gp.monsters.length; i++) {
			if(gp.monsters[i] != null) {
				gp.entityList.add(gp.monsters[i]);
			}
		}
		
		for(int i = 0; i < gp.projectileList.size(); i++) {
			if(gp.projectileList.get(i) != null) {
				gp.entityList.add(gp.projectileList.get(i));
			}
		}
		
		for(int i = 0; i < gp.npcs.length; i++) {
			if(gp.npcs[i] != null) {
				gp.entityList.add(gp.npcs[i]);
			}
		}
	}
	
	public void draw(Graphics2D g) {
		for(int i = 0; i < gp.itemObjects.length; i++) {
			if(gp.itemObjects[i] != null) {
				gp.itemObjects[i].draw(g);
			}
		}
		
		for(int i = 0; i < gp.entityList.size(); i++) {
			if(gp.entityList.get(i) instanceof Projectile) {
				gp.entityList.get(i).update();
			}
			if(gp.entityList.get(i).isAlive) {
				gp.entityList.get(i).draw(g);
			}
		}
		
	}
	
	public void clearEntities() {
		gp.entityList.clear();
	}
	
	public void clearMonsters() {
		for(int i = 0; i < gp.monsters.length; i++) {
			if(gp.monsters[i] != null && !gp.monsters[i].isAlive) {
				gp.monsters[i] = null;
			}
		}
	}
	
	public void reset() {
		gp.ui.messages.clear();
		gp.ui.messageCounter.clear();
		gp.entityList.clear();
		
		for(int i = 0; i < gp.obstacles.length; i++) {
			if(gp.obstacles[i] != null) {
				gp.obstacles[i] = null;
			}
		}
		
		for(int i = 0; i < gp.monsters.length; i++) {
			if(gp.monsters[i] != null && !gp.monsters[i].isAlive) {
				gp.monsters[i] = null;
			}
		}
		
		for(int i = 0; i < gp.npcs.length; i++) {
			if(gp.npcs[i] != null) {
				gp.npcs[i] = null;
			}
		}
		
		for(int i = 0; i < gp.projectileList.size(); i++) {
			if(gp.projectileList.get(i) != null) {
				gp.projectileList.remove(i);
			}
		}
	}
}
