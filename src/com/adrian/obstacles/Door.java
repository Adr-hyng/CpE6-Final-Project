package com.adrian.obstacles;

import java.awt.Rectangle;

import com.adrian.base.Entity;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.user_interfaces.GameState;

public class Door extends Entity {
	public Door(GamePanel gp) {
		super(gp);
		this.name = "Door";
		this.direction = "any";
		this.collision = true;
		
		this.solidArea = new Rectangle(0, 16, 48, 32);
		this.solidAreaDefaultX = this.solidArea.x;
		this.solidAreaDefaultY = this.solidArea.y;
		this.getSprite();
	}
	
	@Override
	protected void getSprite() {
		this.image = this.loadSprite("objects\\door.png", gp.tileSize, gp.tileSize);
	}
	
	public void setDialogue(String text) {
		gp.ui.currentDialogue = text;
	}
	
	public void trigger() {
		gp.gameState = GameState.Dialogue.state;
	}
}
