package com.adrian.objects;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.adrian.GlobalTool;
import com.adrian.user_interface.GamePanel;

public class Key extends com.adrian.objects.Object {
	public Key(GamePanel gp) {
		super(gp);
		this.name = "Key";
		try {
			image = ImageIO.read(new File(GlobalTool.assetsDirectory + "objects\\key.png"));
			image = GlobalTool.utilityTool.scaleImage(image, gp.tileSize, gp.tileSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}