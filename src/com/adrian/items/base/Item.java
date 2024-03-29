package com.adrian.items.base;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.adrian.base.Global;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.utils.Vector2DUtil;



public abstract class Item{
	protected GamePanel gp;
		
	public BufferedImage image;
	public String name = "";
	public String description = "";
	public Class<?> type;
	public int textDescriptionLimit = 30;
	public boolean collision = false;
	public Vector2DUtil worldPosition;
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public int solidAreaDefaultX = 0;
	public int solidAreaDefaultY = 0;
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0);

	public Item(GamePanel gp) {
		this.gp = gp;
	}
	
	public void setDialogue(String text) {}

	public void draw(Graphics2D g) {
		Vector2DUtil screenView = new Vector2DUtil(worldPosition.x - gp.player.worldPosition.x + gp.player.screen.x, worldPosition.y - gp.player.worldPosition.y + gp.player.screen.y);

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
	
	protected BufferedImage loadSprite(final String imagePath) {
		image = null;
		try {
			image = ImageIO.read(new File(Global.assets + imagePath));
			image = Global.util.scaleImage(image, gp.tileSize, gp.tileSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	protected void getSprite() {}
}