package com.adrian.tiles.interactive;

import java.awt.Rectangle;

import com.adrian.entity.base.InteractiveTile;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.utils.Vector2DUtil;

public class Trunk extends InteractiveTile{
	
	GamePanel gp;

	public Trunk(GamePanel gp, int x, int y) {
		super(gp, x, y);
		this.gp = gp;
		
		this.worldPosition = new Vector2DUtil(gp.tileSize * x, gp.tileSize * y);
		this.down1 = this.loadSprite("interactive_tiles\\trunk.png", gp.tileSize, gp.tileSize);
		this.isDestructible = false;
		this.solidArea = new Rectangle(0, 0, 0, 0);
	}
}
