package com.adrian.utils;

import java.awt.Graphics2D;

import com.adrian.entity.NPC;
import com.adrian.user_interface.GamePanel;

public class AssetSetter {
	GamePanel gp;
	
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	
	public void setObject() {
//		gp.objects[0] = new Key(gp);
//		gp.objects[0].worldPosition = new Vector2D(40 * gp.tileSize, 4 * gp.tileSize);
	}
	
	public void setNPC() {
		gp.npcs[0] = new NPC(gp);
		gp.npcs[0].worldPosition = new Vector2D(gp.tileSize * 21, gp.tileSize * 21);
	}
	
	public void draw(Graphics2D g) {
		for(int i = 0; i < gp.objects.length; i++) {
			if(gp.objects[i] == null) continue;
			gp.objects[i].draw(g);
		}
		
		for(int i = 0; i < gp.npcs.length; i++) {
			if(gp.npcs[i] != null) {
				gp.npcs[i].draw(g);
			}
		}
	}
}
