package com.adrian.utils;

import java.awt.Graphics2D;

import com.adrian.objects.Boots;
import com.adrian.objects.Chest;
import com.adrian.objects.Door;
import com.adrian.objects.Key;
import com.adrian.user_interface.GamePanel;

public class AssetSetter {
	GamePanel gp;
	
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	
	public void setObject() {
		gp.objects[0] = new Key(gp);
		gp.objects[0].worldPosition = new Vector2D(40 * gp.tileSize, 4 * gp.tileSize);
		
		gp.objects[1] = new Key(gp);
		gp.objects[1].worldPosition = new Vector2D(26 * gp.tileSize, 23 * gp.tileSize);
		
		
		gp.objects[2] = new Door(gp);
		gp.objects[2].worldPosition = new Vector2D(28 * gp.tileSize, 24 * gp.tileSize);
		
		gp.objects[3] = new Door(gp);
		gp.objects[3].worldPosition = new Vector2D(23 * gp.tileSize, 5 * gp.tileSize);
		
		gp.objects[4] = new Chest(gp);
		gp.objects[4].worldPosition = new Vector2D(23 * gp.tileSize, 0 * gp.tileSize);
		
		gp.objects[5] = new Boots(gp);
		gp.objects[5].worldPosition = new Vector2D(29 * gp.tileSize, 5 * gp.tileSize);
	}
	
	public void draw(Graphics2D g) {
		for(int i = 0; i < gp.objects.length; i++) {
			if(gp.objects[i] == null) continue;
			gp.objects[i].draw(g);
		}
	}
}
