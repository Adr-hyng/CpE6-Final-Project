package com.adrian.utils;

import java.awt.Graphics2D;

import com.adrian.entity.GreenSlime;
import com.adrian.entity.NPC;
import com.adrian.objects.Boots;
import com.adrian.objects.Key;
import com.adrian.obstacles.Door;
import com.adrian.user_interface.GamePanel;

public class AssetSetter {
	GamePanel gp;
	
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	
	public void setItemObject() {
		gp.itemObjects[0] = new Boots(gp);
		gp.itemObjects[0].worldPosition = new Vector2D(gp.tileSize * 45, gp.tileSize * 33);
		
		gp.itemObjects[1] = new Key(gp);
		gp.itemObjects[1].worldPosition = new Vector2D(gp.tileSize * 28, gp.tileSize * 8);
		
		gp.itemObjects[2] = new Key(gp);
		gp.itemObjects[2].worldPosition = new Vector2D(gp.tileSize * 39, gp.tileSize * 30);
	}
	
	public void setObstacle() {
		gp.obstacles[0] = new Door(gp);
		gp.obstacles[0].worldPosition = new Vector2D(28 * gp.tileSize, 14 * gp.tileSize);
		
		gp.obstacles[1] = new Door(gp);
		gp.obstacles[1].worldPosition = new Vector2D(46 * gp.tileSize, 27 * gp.tileSize);
	}
	
	public void setNPC() {
		// BUG WHEN 1st Index
		gp.npcs[0] = new NPC(gp, new String[] {
				"I may be an old man, but I was \nonce a great adventurer like \nyou!",
				"I remember as it was like \nyesterday, wherein I was \nstrong lad like you."
		});
		gp.npcs[2] = new NPC(gp, new String[] {
				"All we have to decide is what \nto do with the time that is \ngiven us.",
				"I will not say: do not weep; \nfor not all tears are an evil."
		});
		
		gp.npcs[0].worldPosition = new Vector2D(gp.tileSize * 47, gp.tileSize * 28);
		gp.npcs[2].worldPosition = new Vector2D(gp.tileSize * 48, gp.tileSize * 28);
	}
	
	public void setMonster() {
		gp.monsters[0] = new GreenSlime(gp);
		gp.monsters[0].worldPosition = new Vector2D(gp.tileSize * 20, gp.tileSize * 37);
		
		gp.monsters[1] = new GreenSlime(gp);
		gp.monsters[1].worldPosition = new Vector2D(gp.tileSize * 12, gp.tileSize * 23);
		
		gp.monsters[2] = new GreenSlime(gp);
		gp.monsters[2].worldPosition = new Vector2D(gp.tileSize * 21, gp.tileSize * 6);
		
		gp.monsters[3] = new GreenSlime(gp);
		gp.monsters[3].worldPosition = new Vector2D(gp.tileSize * 43, gp.tileSize * 13);
		
		gp.monsters[4] = new GreenSlime(gp);
		gp.monsters[4].worldPosition = new Vector2D(gp.tileSize * 40, gp.tileSize * 30);
		
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
			gp.entityList.get(i).draw(g);
		}
		
	}
	
	public void clearEntities() {
		gp.entityList.clear();
	}
	
	public void reset() {
		gp.entityList.clear();
		
		for(int i = 0; i < gp.obstacles.length; i++) {
			if(gp.obstacles[i] != null) {
				gp.obstacles[i] = null;
			}
		}
		
		for(int i = 0; i < gp.monsters.length; i++) {
			if(gp.monsters[i] != null) {
				gp.monsters[i] = null;
			}
		}
		
		for(int i = 0; i < gp.npcs.length; i++) {
			if(gp.npcs[i] != null) {
				gp.npcs[i] = null;
			}
		}
	}
}
