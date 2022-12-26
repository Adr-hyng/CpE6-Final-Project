package com.adrian.user_interface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.adrian.GlobalTool;

public class UserInterface {
	GamePanel gp;
	Graphics2D g2;
	Font arial_40, arial_80B;
	Font futilePro, matchupPro;
	
	public boolean messageOn = false;
	public String message = "";
	public String currentDialogue = "";
	
	public List<String> menuOption;
	public List<String> classOption;
	public int defaultSelectionY = 4;
	public int selectionY = 0;
	
	public int titleScreenState = 0;
	
	@SuppressWarnings("serial")
	public UserInterface(GamePanel gp) {
		this.gp = gp;
		this.menuOption = new ArrayList<>() {{
			add("Play");
			add("Option");
			add("Exit");
		}};
		
		this.classOption = new ArrayList<>() {{
			add("Fighter");
			add("Mage");
			add("Back");
		}};
		
		arial_40 = new Font("Arial", Font.PLAIN, 40);
		arial_80B = new Font("Arial", Font.BOLD, 80);
		futilePro = createFont("FutilePro.ttf");
		matchupPro = createFont("MatchupPro.ttf");
		
	}
	
	public Font createFont(String fontPath) {
		Font font = null;
		try {
			InputStream openFont = new FileInputStream(GlobalTool.assetsDirectory + "fonts\\" + fontPath);
			font = Font.createFont(Font.TRUETYPE_FONT, openFont);
			openFont.close();
			
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return font;
	}
	
	public void showMessage(String text) {
		message = text;
		messageOn = true;
	}
		
	public void draw(Graphics2D g2) {
		this.g2 = g2;
		
		g2.setFont(matchupPro);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor(Color.white);
		
		if(gp.gameState == GameState.Menu.state) {
			drawTitleScreen();
		}
		
		else if(gp.gameState == GameState.Play.state) {
			
		}
		
		else if (gp.gameState == GameState.Pause.state) {
			drawPauseScreen();
		}
		
		else if(gp.gameState == GameState.Dialogue.state) {
			drawDialogScreen();
		}
	}
	
	public void drawTitleScreen() {
		if(titleScreenState == 0) {
			g2.setColor(new Color(0, 0, 0));
			g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
			String text = "MY TITLE";
			int x = getCenterText(text);
			int y = gp.tileSize * 2;
			
			// Title Shadow
			g2.setColor(Color.gray);
			g2.drawString(text, x + 5, y + 5);
			
			// Title Main
			g2.setColor(Color.white);
			g2.drawString(text, x, y);
			
			// Logo
			x = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
			y += gp.tileSize * 2;
			g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);
			
			// Menu
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50F));
			for(int i = 0; i < menuOption.size(); i++) {
				createSubTitleText(menuOption.get(i), x, y, 0, defaultSelectionY + (i));
			}
			createSubTitleText(">>", x, y, -2, selectionY + defaultSelectionY);
		}
		
		else if (titleScreenState == 1) {
			g2.setColor(Color.white);
			g2.setFont(futilePro.deriveFont(Font.PLAIN, 42F));
			
			String text = "SELECT YOUR CLASS";
			int x = getCenterText(text);
			int y = gp.tileSize * 3;
			g2.drawString(text, x, y);
			
			
			for(int i = 0; i < classOption.size(); i++) {
				if (i >= classOption.size() - 1) {
					createSubTitleText(classOption.get(i), x, y, 0, 3 + (i + 1));
					break;
				}
				createSubTitleText(classOption.get(i), x, y, 0, 3 + (i));
			}
//			createSubTitleText("Fighter", x, y, 0, 3);
//			createSubTitleText("Mage", x, y, 0, 4);
//			createSubTitleText("Back", x, y, 0, 6);
			createSubTitleText(">>", x, y, -2, selectionY + 3);
		}
		
	}
	
	private void createSubTitleText(String subTitle, int x, int y, int xOffset, int yOffset) {
		// USE VECTOR2D
		x = getCenterText(subTitle) + (xOffset * gp.tileSize);
		y += gp.tileSize * yOffset;
		g2.drawString(subTitle, x, y);
	}
	
	public void drawPauseScreen() {
		String text = "PAUSED";
		g2.setFont(futilePro.deriveFont(Font.PLAIN, 80F));
		int x = getCenterText(text);
		int y = gp.screenHeight / 2;
		g2.drawString(text, x, y);
	}
	
	public void drawDialogScreen() {
		// USE VECTOR2D or Rectangle
		int x = gp.tileSize * 2;
		int y = gp.tileSize / 2;
		int width = gp.screenWidth - (gp.tileSize * 4);
		int height = gp.tileSize * 4;
		
		drawSubWindow(x, y, width, height);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
		x += gp.tileSize;
		y += gp.tileSize;
		
		for(String line: currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}
	}
	
	public void drawSubWindow(int x, int y, int width, int height) {
		Color color = new Color(0, 0, 0, 200);
		g2.setColor(color);
		g2.fillRoundRect(x, y, width, height, 35, 35);
		color = new Color(255, 255, 255);
		g2.setColor(color);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
	}
	
	private int getCenterText(String text) {
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth / 2 - length / 2;
		return x;
	}
}
