package com.adrian.utils;

import java.awt.Graphics2D;

import com.adrian.entity.NPC;
import com.adrian.objects.Boots;
import com.adrian.obstacles.Door;
import com.adrian.user_interface.GamePanel;

public class AssetSetter {
	GamePanel gp;
	
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	
	public void setItemObject() {
		gp.itemObjects[0] = new Boots(gp);
		gp.itemObjects[0].worldPosition = new Vector2D(gp.tileSize * 21, gp.tileSize * 22);
	}
	
	public void setObstacle() {
		gp.obstacles[0] = new Door(gp);
		gp.obstacles[0].worldPosition = new Vector2D(28 * gp.tileSize, 24 * gp.tileSize);
	}
	
	public void setNPC() {
		gp.npcs[0] = new NPC(gp);
		gp.npcs[1] = new NPC(gp);
		gp.npcs[2] = new NPC(gp);
		
		gp.npcs[0].worldPosition = new Vector2D(gp.tileSize * 21, gp.tileSize * 21);
		gp.npcs[1].worldPosition = new Vector2D(gp.tileSize * 24, gp.tileSize * 21);
		gp.npcs[2].worldPosition = new Vector2D(gp.tileSize * 24, gp.tileSize * 22);
	}
	
	public void compressEntities() {
		for(int i = 0; i < gp.obstacles.length; i++) {
			if(gp.obstacles[i] != null) {
				gp.entityList.add(gp.obstacles[i]);
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
		for(int i = 0; i < gp.entityList.size(); i++) {
			gp.entityList.remove(i);
		}
	}
}
