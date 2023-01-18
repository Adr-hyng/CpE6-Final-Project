package com.adrian.tiles;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.adrian.base.Global;
import com.adrian.user_interfaces.GamePanel;

public class Tile {
	public BufferedImage image;
	public boolean collision = false;
	
	public Tile(String imagePath, boolean collision, GamePanel gp) {
		try {
			this.image = ImageIO.read(new File(Global.assets + "tiles\\" + imagePath));
			this.image = Global.util.scaleImage(this.image, gp.tileSize, gp.tileSize);
			this.collision = collision;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
