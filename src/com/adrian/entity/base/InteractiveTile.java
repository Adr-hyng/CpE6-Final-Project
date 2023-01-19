package com.adrian.entity.base;

import java.awt.Graphics2D;

import com.adrian.user_interfaces.GamePanel;
import com.adrian.utils.Vector2DUtil;

public class InteractiveTile extends Entity{
	
	public boolean isDestructible;

	public InteractiveTile(GamePanel gp, int x, int y) {
		super(gp);
	}
	
	public void update() {
		if(invincible) {
			invincibleCount++;
			if(invincibleCount > 20) {
				invincible = false;
				invincibleCount = 0;
			}
		}
	}

	public void draw(Graphics2D g2) {
		Vector2DUtil screenView = new Vector2DUtil(worldPosition.x - gp.player.worldPosition.x + gp.player.screen.x, worldPosition.y - gp.player.worldPosition.y + gp.player.screen.y);
		// To remove black spots when chunk loading.
		final int screenOffset = 2;
		
		// Only load tiles to the visible tiles in the player's screen.
		if(worldPosition.x + (gp.tileSize * screenOffset) > gp.player.worldPosition.x - gp.player.screen.x && 
		   worldPosition.x - (gp.tileSize * screenOffset) < gp.player.worldPosition.x + gp.player.screen.x &&
		   worldPosition.y + (gp.tileSize * screenOffset) > gp.player.worldPosition.y - gp.player.screen.y &&
		   worldPosition.y - (gp.tileSize * screenOffset) < gp.player.worldPosition.y + gp.player.screen.y) {
			g2.drawImage(down1, screenView.x, screenView.y, null);
		}
	}
	
	@Override
	protected void getSprite() {
		// TODO Auto-generated method stub
	}
	
	public void setSound() {
		
	}
	
	public InteractiveTile getDestroyedForm() {
		InteractiveTile tile = null;
		return tile;
	}
	
	public boolean isItemValid(Player entity, Class<?> requiredType) {
		return false;
	}
	

}
