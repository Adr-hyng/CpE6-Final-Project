package com.adrian.events;

import com.adrian.user_interfaces.GamePanel;
import com.adrian.user_interfaces.GameState;
import com.adrian.utils.Vector2DUtil;

public class EventHandler {
	GamePanel gp;
	EventRect rect[][];
	
	Vector2DUtil previousEvent = new Vector2DUtil(0, 0);
	boolean canTouchEvent = true;
	
	public EventHandler (GamePanel gp) {
		this.gp = gp;
		
		this.rect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];
		int col = 0;
		int row = 0;
		while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
			rect[col][row] = new EventRect();
			rect[col][row].x = 23;
			rect[col][row].y = 23;
			rect[col][row].width = 2;
			rect[col][row].height = 2;
			rect[col][row].defaultRect.x = rect[col][row].x;
			rect[col][row].defaultRect.y = rect[col][row].y;
			col++;
			
			if (col == gp.maxWorldCol) {
				col = 0;
				row++;
			}
		}
	}
	
	public void checkEvent() {
		Vector2DUtil lastPositionExecutedDistance = new Vector2DUtil(
				Math.abs(gp.player.worldPosition.x - previousEvent.x),
				Math.abs(gp.player.worldPosition.y - previousEvent.y));
		
		int distance = Math.max((int) lastPositionExecutedDistance.x, (int) lastPositionExecutedDistance.y);
		int tileDistance = 1;
		if (distance > (gp.tileSize * tileDistance)) {
			canTouchEvent = true;
		}
		
		if (canTouchEvent) {
			if(hit(37, 16, "any")) {
				this.damagePit(37, 16, GameState.Dialogue.state);
			}
			
			if(hit(38, 14, "any")) {
				this.damagePit(38, 14, GameState.Dialogue.state);
			}
			
			if(hit(37, 12, "any")) {
				this.damagePit(37, 12, GameState.Dialogue.state);
			}
			
			if(hit(23, 10, "up")) {
				healingPool(28, 8, GameState.Dialogue.state);
			}
		}
	}
	
	public boolean hit(int col, int row, String directionFacing) {
		boolean hit = false;
		
		gp.player.solidArea.x = (int) (gp.player.worldPosition.x + gp.player.solidArea.x);
		gp.player.solidArea.y = (int) (gp.player.worldPosition.y + gp.player.solidArea.y);
		
		rect[col][row].x = col * gp.tileSize + rect[col][row].x;
		rect[col][row].y = row * gp.tileSize + rect[col][row].y;
		
		if (gp.player.solidArea.intersects(rect[col][row]) && !rect[col][row].eventDone) {
			if(gp.player.direction.contentEquals(directionFacing) || directionFacing.contentEquals("any")) {
				hit = true;
				
				previousEvent = new Vector2DUtil(gp.player.worldPosition.x, gp.player.worldPosition.y);
				
			}
		}
		
		gp.player.solidArea.x = gp.player.solidAreaDefaultX;
		gp.player.solidArea.y = gp.player.solidAreaDefaultY;
		rect[col][row].x = (int) rect[col][row].defaultRect.x;
		rect[col][row].y = (int) rect[col][row].defaultRect.y;
		
		return hit;
	}
	
	public void damagePit(int col, int row, int gameState) {
		gp.ui.dialogueOffset.y = 7;
		gp.gameState = gameState;
		gp.ui.currentDialogue = "You fell into a hole.";
		gp.player.currentLife = 0;
		canTouchEvent = false;
	}
	
	public void healingPool(int col, int row, int gameState) {
		if(gp.keyInput.haveKeyPressed.get("ENTER")) {
			gp.ui.dialogueOffset.y = 7;
			gp.gameState = gameState;
			int healAmount = 2;
			int manaAmount = 2;
			gp.ui.currentDialogue = "Mystical Tree magically \nhealed you.";
			gp.player.currentLife += healAmount;
			gp.player.currentMana += manaAmount;
			gp.keyInput.haveKeyPressed.replace("ENTER", false);
			gp.assetHandler.clearMonsters();
			gp.assetHandler.setMonsters();
		}
	}
}
