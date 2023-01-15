package com.adrian.user_interface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.adrian.GlobalTool;
import com.adrian.objects.Heart;
import com.adrian.utils.Vector2D;

public class UserInterface {
	GamePanel gp;
	Graphics2D g2;
	
	// Player
	BufferedImage fullHeart, halfHeart, blankHeart;
	
	Font arial_40, arial_80B;
	Font futilePro, matchupPro;
	
	public String currentDialogue = "";
	public Vector2D dialogueOffset;
	
	public ArrayList<String> messages = new ArrayList<>();
	public ArrayList<Integer> messageCounter = new ArrayList<>();
	
	
	public String titleScreen = "Rad-dew's Adventure";
	
	public List<String> menuOption;
	public List<String> classOption;
	public int defaultSelectionY = 4;
	public int selectionY = 0;
	
	public int titleScreenState = 0;
	
	public int dialogBoxHeightOffset = 0;
	
	@SuppressWarnings("serial")
	public UserInterface(GamePanel gp) {
		this.gp = gp;
		this.dialogueOffset = new Vector2D(0, 0);
		this.menuOption = new ArrayList<>() {{
			add("Continue");
			add("New Game");
			add("Option");
			add("Exit");
		}};
		
		this.classOption = new ArrayList<>() {{
			add("Fighter");
			add("Rogue");
			add("Back");
		}};
		
		arial_40 = new Font("Arial", Font.PLAIN, 40);
		arial_80B = new Font("Arial", Font.BOLD, 80);
		futilePro = createFont("FutilePro.ttf");
		matchupPro = createFont("MatchupPro.ttf");
		
		// CREATE HUD OBJECT
		Heart heart = new Heart(gp);
		fullHeart = heart.image;
		halfHeart = heart.halfHeartImage;
		blankHeart = heart.emptyHeartImage;
		
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
	
		
	public void draw(Graphics2D g2) {
		this.g2 = g2;
		
		g2.setFont(matchupPro);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor(Color.white);
		
		if(gp.gameState == GameState.Menu.state) {
			drawTitleScreen();
		}
		
		else if(gp.gameState == GameState.Continue.state) {
			drawPlayerLife();
			drawMessageLog();
			
		}
		
		else if (gp.gameState == GameState.Pause.state) {
			drawPlayerLife();
			drawPauseScreen();
		}
		
		else if(gp.gameState == GameState.Dialogue.state) {
			drawPlayerLife();
			drawDialogScreen();
		}
		else if(gp.gameState == GameState.ShowStat.state) {
			drawPlayerStat();
		}
	}
	
	public void addMessage(String text) {
		messages.add(text);
		messageCounter.add(0);
	}
	
	
	public void drawMessageLog() {
		int messageX = gp.tileSize;
		int messageY = gp.tileSize * 4;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
		
		for(int i = 0; i < messages.size(); i++) {
			if(messages.get(i) != null) {
				g2.setColor(Color.black);
				g2.drawString(messages.get(i), messageX + 2, messageY + 2);
				g2.setColor(Color.white);
				g2.drawString(messages.get(i), messageX, messageY);
				
				int counter = messageCounter.get(i) + 1;
				messageCounter.set(i, counter);
				messageY += 50;
				
				if(messageCounter.get(i) > 180) {
					messages.remove(i);
					messageCounter.remove(i);
				}
			}
		}
	}
	
	public void drawPlayerStat() {
		final int frameX = gp.tileSize * 1;
		final int frameY = gp.tileSize;
		final int frameWidth = gp.tileSize * 5;
		final int frameHeight = gp.tileSize * 10;
		drawSubWindow(frameX, frameY, frameWidth, frameHeight, 200);
		
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		int textX = frameX + 20;
		int textY = frameY + gp.tileSize;
		final int lineHeight = 35;
		
		g2.drawString("Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Life", textX, textY);
		textY += lineHeight;
		g2.drawString("Strength", textX, textY);
		textY += lineHeight;
		g2.drawString("Dexterity", textX, textY);
		textY += lineHeight;
		g2.drawString("Attack", textX, textY);
		textY += lineHeight;
		g2.drawString("Defense", textX, textY);
		textY += lineHeight;
		g2.drawString("Exp", textX, textY);
		textY += lineHeight;
		g2.drawString("Next Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Coin", textX, textY);
		textY += lineHeight + 20;
		g2.drawString("Weapon", textX, textY);
		textY += lineHeight + 15;
		g2.drawString("Shield", textX, textY);
		textY += lineHeight;
		
		int tailX = (frameX + frameWidth) - 30;
		textY = frameY + gp.tileSize;
		String value;
		
		value = String.valueOf(gp.player.level);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.currentLife + "/" + gp.player.maxLife);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.strength);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.dexterity);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.attack);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.defense);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.exp);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.nextLevelExp);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.coin);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		g2.drawImage(gp.player.currentWeapon.image, tailX - gp.tileSize, textY - 12, null);
		textY += lineHeight + 20;
		g2.drawImage(gp.player.currentShield.image, tailX - gp.tileSize, textY - 12, null);
	}
	
	public void drawPlayerLife() {
		int position = (gp.tileSize / 2) - gp.originalTileSize;
		int width = gp.tileSize * (gp.player.maxLife / 2) + (gp.originalTileSize * 2);
		drawSubWindow(position, position, width, (gp.tileSize * 2) - gp.originalTileSize, 255);
		
		int x = gp.tileSize / 2;
		int y = gp.tileSize / 2;
		int i = 0;
		
		// Initialize Max Life
		while (i < gp.player.maxLife / 2) {
			g2.drawImage(blankHeart, x, y, null);
			i++; 
			x += gp.tileSize;
		}
		
		// Reset
		x = gp.tileSize / 2;
		y = gp.tileSize / 2;
		i = 0;
		
		// Draw Current Life
		while (i < gp.player.currentLife) {
			g2.drawImage(halfHeart, x, y, null);
			i++;
			if(i < gp.player.currentLife) {
				g2.drawImage(fullHeart, x, y, null);
			}
			i++; 
			x += gp.tileSize;
		}
	}

	public void drawTitleScreen() {
		g2.setColor(new Color(0, 0, 0));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80F));
		
		Image image;
		image = new ImageIcon(System.getProperty("user.dir") + "\\res\\background.gif").getImage();
		g2.drawImage(image, 0, 0, gp.screenWidth, gp.screenHeight, null);
		
		if(titleScreenState == 0) {
			g2.setColor(new Color(168, 138, 93));
			int x = getCenterText(titleScreen);
			int y = gp.tileSize * 2;
			
			// Title Shadow
			g2.setColor(new Color(97, 84, 66));
			g2.drawString(titleScreen, x + 5, y + 5);
			
			// Title Main
			g2.setColor(new Color(168, 138, 93));
			g2.drawString(titleScreen, x, y);
			
			// Logo
			x = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
			y += gp.tileSize * 2;
			image = new ImageIcon(System.getProperty("user.dir") + "\\res\\logo.png").getImage();
			g2.drawImage(image, x, y, gp.tileSize * 3, gp.tileSize * 3, null);
			
			// Menu
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50F));
			for(int i = 0; i < menuOption.size(); i++) {
				g2.setColor(new Color(168, 138, 93));
				createSubTitleText(menuOption.get(i), x, y, 0, defaultSelectionY + (i));
			}
			createSubTitleText(">>", x, y, -3, selectionY + defaultSelectionY);
		}
		
		else if (titleScreenState == 1) {
			
			g2.setFont(futilePro.deriveFont(Font.PLAIN, 42F));
			
			String text = "SELECT YOUR CLASS";
			int x = getCenterText(text);
			int y = gp.tileSize * 3;
			g2.setColor(new Color(168, 138, 93));
			g2.drawString(text, x, y);
			
			
			for(int i = 0; i < classOption.size(); i++) {
				if (i >= classOption.size() - 1) {
					g2.setColor(new Color(168, 138, 93));
					createSubTitleText(classOption.get(i), x, y, 0, 3 + (i + 1));
					break;
				}
				g2.setColor(new Color(191, 74, 42));
				createSubTitleText(classOption.get(i), x, y, 0, 3 + (i));
			}
			createSubTitleText(">>", x, y, -3, selectionY + 3);
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
		dialogBoxHeightOffset = (currentDialogue.length() / 29) + 2;
		
		int x = gp.tileSize * 2;
		int y = (int) ((gp.tileSize / 2) + (gp.tileSize * this.dialogueOffset.y));
		int width = gp.screenWidth - (gp.tileSize * 5);
		int height = gp.tileSize * dialogBoxHeightOffset;
		
		drawSubWindow(x, y, width, height, 200);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
		x += gp.tileSize;
		y += gp.tileSize;
		
		for(String line: currentDialogue.split("\n")) {
			line = line.replace("\t", "    ");
			g2.drawString(line, x, y);
			y += 40;
		}
	}
	
	public void drawSubWindow(int x, int y, int width, int height, int opacity) {
		Color color = new Color(0, 0, 0, opacity);
		g2.setColor(color);
		g2.fillRoundRect(x, y, width, height, 35, 35);
		color = new Color(255, 255, 255);
		g2.setColor(color);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
	}
	
	public String getAdjustableText(String originalText) {
		String text = originalText;
		String newText = "";
		int textLimit = 29;
		int newLineCount = text.length() / textLimit;
		for(int i = 1; i < newLineCount + 1; i++) {
			int leftIndex = text.substring(0, (textLimit * i)).lastIndexOf(" ");
			text = text.substring(0, (leftIndex * 1) + 1) + "\n" + text.substring((leftIndex * 1) + 1);
			newText = text;
		}
		return newText;
	}
	
	private int getCenterText(String text) {
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth / 2 - length / 2;
		return x;
	}
	
	private int getXforAlignToRightText(String text, int tailX) {
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = tailX - length;
		return x;
	}
}
