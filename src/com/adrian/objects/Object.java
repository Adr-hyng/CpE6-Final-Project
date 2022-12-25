package com.adrian.objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.adrian.user_interface.GamePanel;
import com.adrian.utils.Vector2D;

public class Object {
	protected GamePanel gp;
	
	public BufferedImage image;
	public String name;
	public boolean collision = false;
	public Vector2D worldPosition;
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public int solidAreaDefaultX = 0;
	public int solidAreaDefaultY = 0;
	
	public Object(GamePanel gp) {
		this.gp = gp;
	}
	
	public void draw(Graphics2D g) {
		Vector2D screenView = new Vector2D(worldPosition.x - gp.player.worldPosition.x + gp.player.screen.x, worldPosition.y - gp.player.worldPosition.y + gp.player.screen.y);
		
		// To remove black spots when chunk loading.
		final int screenOffset = 2;
		
		// Only load tiles to the visible tiles in the player's screen.
		if(worldPosition.x + (gp.tileSize * screenOffset) > gp.player.worldPosition.x - gp.player.screen.x && 
		   worldPosition.x - (gp.tileSize * screenOffset) < gp.player.worldPosition.x + gp.player.screen.x &&
		   worldPosition.y + (gp.tileSize * screenOffset) > gp.player.worldPosition.y - gp.player.screen.y &&
		   worldPosition.y - (gp.tileSize * screenOffset) < gp.player.worldPosition.y + gp.player.screen.y) {
			g.drawImage(image, (int) screenView.x, (int) screenView.y, null);	
		}
	}
}