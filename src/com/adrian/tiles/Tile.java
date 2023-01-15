package com.adrian.tiles;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.adrian.GlobalTool;
import com.adrian.user_interface.GamePanel;

public class Tile {
	public BufferedImage image;
	public boolean collision = false;
	
	public Tile(String imagePath, boolean collision, GamePanel gp) {
		try {
			this.image = ImageIO.read(new File(GlobalTool.assetsDirectory + "tiles\\" + imagePath));
			this.image = GlobalTool.utilityTool.scaleImage(this.image, gp.tileSize, gp.tileSize);
			this.collision = collision;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
