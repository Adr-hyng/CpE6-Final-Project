package com.adrian.user_interface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import com.adrian.objects.Key;

public class UserInterface {
	GamePanel gp;
	Font arial_40, arial_80B;
	
	BufferedImage keyCountIcon;
	public boolean messageOn = false;
	public String message = "";
	int messageFadesIn = 0;
	int textTransformY = 0;
	
	double playTime;
	DecimalFormat dFormat = new DecimalFormat("#0.00");
	
	public boolean gameFinished = false;
	
	public UserInterface(GamePanel gp) {
		this.gp = gp;
		
		arial_40 = new Font("Arial", Font.PLAIN, 40);
		arial_80B = new Font("Arial", Font.BOLD, 80);
		Key key = new Key(gp);
		keyCountIcon = key.image;
	}
	
	public void showMessage(String text) {
		message = text;
		messageOn = true;
	}
	
	public void draw(Graphics2D g2) {
		if (gameFinished) {
			g2.setFont(arial_40);
			g2.setColor(Color.white);
			
			String text;
			int textLength;
			int x;
			int y;
			text = "You found the treasure!";
			textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			x = gp.screenWidth / 2 - textLength / 2;
			y = gp.screenHeight / 2 - (gp.tileSize * 3);
			g2.drawString(text, x, y);
			
			
			text = "Total time spent: " + dFormat.format(playTime);
			textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			x = gp.screenWidth / 2 - textLength / 2;
			y = gp.screenHeight / 2 + (gp.tileSize * 4);
			g2.drawString(text, x, y);
			
			g2.setFont(arial_80B);
			g2.setColor(Color.yellow);
			text = "Congratulations!";
			textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			
			x = gp.screenWidth / 2 - textLength / 2;
			y = gp.screenHeight / 2 + (gp.tileSize * 2);
			g2.drawString(text, x, y);
			
			gp.gameThread = null;
			
		} else {
			g2.setFont(arial_40);
			g2.setColor(Color.white);
			g2.drawImage(keyCountIcon, gp.tileSize / 2, gp.tileSize / 2, gp.tileSize, gp.tileSize, null);
			g2.drawString("x " + gp.player.haveKeyCount, 74, 64);
			
			// Time
			playTime += (double) 1 / 60;
			g2.drawString("Time: " + dFormat.format(playTime), gp.tileSize * 11, 65);
			
			
			// Message
			if(messageOn) {
				g2.setFont(g2.getFont().deriveFont(30F));
				g2.drawString(message, (int) gp.player.screen.x - (message.length() * 4), (gp.tileSize * 5) + textTransformY);
				textTransformY--;
				messageFadesIn++;
				if(messageFadesIn > 120) {
					messageFadesIn = 0;
					messageOn = false;
					textTransformY = 0;
				}
			}
		}
	}
}
