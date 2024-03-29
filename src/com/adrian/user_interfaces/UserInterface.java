package com.adrian.user_interfaces;

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

import com.adrian.base.GameState;
import com.adrian.base.Global;
import com.adrian.items.Heart;
import com.adrian.items.ManaCrystal;
import com.adrian.utils.Sound;
import com.adrian.utils.Vector2DUtil;

public class UserInterface {
	GamePanel gp;
	Graphics2D g2;
	
	// Player
	BufferedImage fullHeart, halfHeart, emptyHeart;
	BufferedImage fullMana, emptyMana;
	
	Font arial_40, arial_80B;
	Font futilePro, matchupPro;
	
	public String currentDialogue = "";
	public Vector2DUtil dialogueOffset;
	
	public ArrayList<String> messages = new ArrayList<>();
	public ArrayList<Integer> messageCounter = new ArrayList<>();
	
	
	public String titleScreen = "Rad-dew's Adventure";
	public int titleScreenState = 0;
	public int dialogBoxHeightOffset = 0;
	public int itemDescriptionHeight = 2;
	
	public List<String> menuOption;
	public List<String> classOption;
	
	// Cursors
	public int defaultSelectionY = 4;
	public int selectionY = 0;
	
	// Inventory
	public int inventorySlotCol = 0;
	public int inventorySlotRow = 0;
	public final int inventoryMaxSlotX = 5; 
	public int inventoryMaxSlotY = 4; 
	
	// Options
	public int subState = 0;
	
	
	@SuppressWarnings("serial")
	public UserInterface(GamePanel gp) {
		this.gp = gp;
		this.dialogueOffset = new Vector2DUtil(0, 0);
		this.menuOption = new ArrayList<>() {{
			add("Continue");
			add("New Game");
			add("Option");
			add("Exit");
		}};
		
		this.classOption = new ArrayList<>() {{
			for(String availableClass: Global.util.getStartingClass()) {
				add(availableClass);
			}
			add("Back");
		}};
		
		arial_40 = new Font("Arial", Font.PLAIN, 40);
		arial_80B = new Font("Arial", Font.BOLD, 80);
		futilePro = createFont("FutilePro.ttf");
		matchupPro = createFont("MatchupPro.ttf");
		
		// CREATE HUD OBJECT
		Heart heart = new Heart(gp);
		fullHeart = heart.image;
		halfHeart = heart.halfImage;
		emptyHeart = heart.emptyImage;
		
		ManaCrystal mana = new ManaCrystal(gp);
		fullMana = mana.image;
		emptyMana = mana.emptyImage;
		
	}
	
	public Font createFont(String fontPath) {
		Font font = null;
		try {
			InputStream openFont = new FileInputStream(Global.assets + "fonts\\" + fontPath);
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
			drawPlayerHUD();
			drawMessageLog();
			
		}
		
		else if (gp.gameState == GameState.Pause.state) {
			drawPlayerHUD();
			drawPauseScreen();
		}
		
		else if(gp.gameState == GameState.Dialogue.state) {
			drawDialogScreen();
		}
		else if(gp.gameState == GameState.ShowStat.state) {
			drawPlayerStat();
			drawPlayerInventory();
		}
		else if(gp.gameState == GameState.ShowOption.state) {
			drawOptionScreen();
		}
	}
	
	public void drawOptionScreen() {
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		int frameX = gp.tileSize * 6;
		int frameY = gp.tileSize;
		int frameWidth = gp.tileSize * 8;
		int frameHeight = gp.tileSize * 10;
		this.drawSubWindow(frameX, frameY, frameWidth, frameHeight, 255);
		
		switch(subState) {
		case 0: optionsTop(frameX, frameY); break;
		case 1: optionsFullScreenification(frameX, frameY); break;
		case 2: optionsControlScreen(frameX, frameY); break;
		case 3: optionsConfirmExit(frameX, frameY); break;
		}
		gp.keyInput.haveKeyPressed.replace("ENTER", false);
	}
	
	// REFACTOR
	public void optionsTop(int frameX, int frameY) {
		int textX;
		int textY;
		
		// Title
		String text = "Options";
		textX = getCenterText(text);
		textY = frameY + gp.tileSize;
		g2.drawString(text, textX, textY);
		
		// FULL SCREEN ON/OFF
		textX = frameX + gp.tileSize;
		textY += gp.tileSize * 2;
		g2.drawString("Full Screen", textX, textY);
		if(selectionY == 0) {
			g2.drawString(">", textX - 25, textY);
			if(gp.keyInput.haveKeyPressed.get("ENTER")) {
				if(!gp.isFullScreen) gp.isFullScreen = true;
				else gp.isFullScreen = false;
				subState = 1;
			}
		}
		// MUSIC
		textY += gp.tileSize;
		g2.drawString("Music", textX, textY);
		if(selectionY == 1) {
			g2.drawString(">", textX - 25, textY);
		}
		
		// SOUND EFFECTS
		textY += gp.tileSize;
		g2.drawString("Sound Effect", textX, textY);
		if(selectionY == 2) {
			g2.drawString(">", textX - 25, textY);
		}
		
		// CONTROL
		textY += gp.tileSize;
		g2.drawString("Control", textX, textY);
		if(selectionY == 3) {
			g2.drawString(">", textX - 25, textY);
			if(gp.keyInput.haveKeyPressed.get("ENTER")) {
				subState = 2;
				selectionY = 0;
			}
		}
		
		// END GAME
		textY += gp.tileSize;
		g2.drawString("Exit Game", textX, textY);
		if(selectionY == 4) {
			g2.drawString(">", textX - 25, textY);
			if(gp.keyInput.haveKeyPressed.get("ENTER")) {
				subState = 3;
				selectionY = 0;
			}
		}
		
		// GO BACK GAME
		textY += gp.tileSize * 2;
		g2.drawString("Back", textX, textY);
		if(selectionY == 5) {
			g2.drawString(">", textX - 25, textY);
			if(gp.keyInput.haveKeyPressed.get("ENTER")) {
				gp.gameState = GameState.Continue.state;
				selectionY = 0;
				subState = 0;
			}
		}
		
		
		// Full Screen Check Box
		textX = frameX + (int) (gp.tileSize * 4.5);
		textY = frameY + gp.tileSize * 2 + 24;
		g2.setStroke(new BasicStroke(3));
		g2.drawRect(textX, textY, 24, 24);
		if(gp.isFullScreen) {
			g2.fillRect(textX, textY, 24, 24);
		}
		
		// Music Volume
		textY += gp.tileSize;
		g2.drawRect(textX, textY, 120, 24);
		int volumeWidth = 24 * Sound.INTRO.volumeScale;			
		g2.fillRect(textX, textY, volumeWidth, 24);
		
		// Special Effects Volume
		textY += gp.tileSize;
		g2.drawRect(textX, textY, 120, 24);
		volumeWidth = 24 * Sound.COIN.volumeScale;			
		g2.fillRect(textX, textY, volumeWidth, 24);
		
		
	}
	
	public void optionsFullScreenification(int frameX, int frameY) {
		int textX = frameX + gp.tileSize;
		int textY = frameY + gp.tileSize * 3;
		
		currentDialogue = "The change will take \neffect after restarting \nthe game.";
		
		for(String line: currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY += 40;
		}
		
		// Back
		textY = frameY + gp.tileSize * 9;
		g2.drawString("Back", textX, textY);
		if(selectionY == 0) {
			g2.drawString(">", textX - 25, textY);
			if(gp.keyInput.haveKeyPressed.get("ENTER")) {
				subState = 0;
			}
		}
	}
	
	public void optionsControlScreen(int frameX, int frameY) {
		int textX;
		int textY;
		
		// TITLE
		String text = "Control";
		textX = getCenterText(text);
		textY = frameY + gp.tileSize;
		g2.drawString(text, textX, textY);
		
		textX = frameX + gp.tileSize;
		textY += gp.tileSize;
		g2.drawString("Move", textX, textY); textY += gp.tileSize;
		g2.drawString("Confirm/Attack", textX, textY); textY += gp.tileSize;
		g2.drawString("Shoot/Cast", textX, textY); textY += gp.tileSize;
		g2.drawString("Character Screen", textX, textY); textY += gp.tileSize;
		g2.drawString("Pause", textX, textY); textY += gp.tileSize;
		g2.drawString("Options", textX, textY); textY += gp.tileSize;
		
		textX = frameX + gp.tileSize * 6;
		textY = frameY + gp.tileSize * 2;
		g2.drawString("WASD", textX, textY); textY += gp.tileSize;
		g2.drawString("ENTER", textX, textY); textY += gp.tileSize;
		g2.drawString("F", textX, textY); textY += gp.tileSize;
		g2.drawString("C", textX, textY); textY += gp.tileSize;
		g2.drawString("P", textX, textY); textY += gp.tileSize;
		g2.drawString("ESCAPE", textX, textY); textY += gp.tileSize;
		
		// Back
		textX = frameX + gp.tileSize;
		textY = frameY + gp.tileSize * 9;
		g2.drawString("Back", textX, textY);
		if(selectionY == 0) {
			g2.drawString(">", textX - 25, textY);
			if(gp.keyInput.haveKeyPressed.get("ENTER")) {
				selectionY = 3;
				subState = 0;
			}
		}
	}
	
	public void optionsConfirmExit(int frameX, int frameY) {
		int textX = frameX + gp.tileSize;
		int textY = frameY + gp.tileSize * 3;
		
		currentDialogue = "Quit the game and \nreturn to the title screen?";
		for(String line: currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY += 40;
		}
		
		// Yes
		String text = "Yes";
		textX = getCenterText(text);
		textY += gp.tileSize * 3;
		g2.drawString(text, textX, textY);
		if(selectionY == 0) {
			g2.drawString(">", textX - 25, textY);
			if(gp.keyInput.haveKeyPressed.get("ENTER")) {
				subState = 0;
				gp.reset();
			}
		}
		
		// No
		text = "No";
		textX = getCenterText(text);
		textY += gp.tileSize;
		g2.drawString(text, textX, textY);
		if(selectionY == 1) {
			g2.drawString(">", textX - 25, textY);
			if(gp.keyInput.haveKeyPressed.get("ENTER")) {
				subState = 0;
				selectionY = 4;
			}
		}
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
	
	public void drawPlayerInventory() {
		int frameX = gp.tileSize * 12;
		int frameY = gp.tileSize;
		int frameWidth = gp.tileSize * (inventoryMaxSlotX + 1);
		int frameHeight = gp.tileSize * (inventoryMaxSlotY + 1);
		drawSubWindow(frameX, frameY, frameWidth, frameHeight, 200);
		
		// Slot 
		final int slotXStart = frameX + 20;
		final int slotYStart = frameY + 20;
		int slotX = slotXStart;
		int slotY = slotYStart;
		int slotSize = gp.tileSize + 3;
		
		int j = 0;
		for(int i = 0; i < gp.player.inventory.items.size(); i++) {
			
			if(gp.player.inventory.items.get(i) == gp.player.currentWeapon || gp.player.inventory.items.get(i) == gp.player.currentShield) {
				g2.setColor(new Color(240, 190, 90));
				g2.fillRoundRect(slotX, slotY, slotSize, slotSize, 10, 10);
			}
			
			g2.drawImage(gp.player.inventory.items.get(i).image, slotX, slotY, null);
			slotX += slotSize;
			if(i == 4 + (j * inventoryMaxSlotX)) {
				slotX = slotXStart;
				slotY += slotSize;
				j++;
			}
		}
		j = 0;
		
		// Cursor
		int cursorX = slotXStart + (slotSize * inventorySlotCol);
		int cursorY = slotYStart + (slotSize * inventorySlotRow);
		int cursorWidth = gp.tileSize;
		int cursorHeight = gp.tileSize;
		
		// Draw Cursor
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(3));
		g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
		
		// Description Frame
		int dFrameX = frameX;
		int dFrameY = frameY + frameHeight + gp.tileSize;
		int dFrameWidth = frameWidth;
		int dFrameHeight = gp.tileSize * itemDescriptionHeight;
		
		// Description Label
		int textX = dFrameX + 20;
		int textY = dFrameY + gp.tileSize;
		g2.setFont(g2.getFont().deriveFont(28F));
		
		int itemIndex = gp.player.inventory.getItemIndex();
		if(itemIndex < gp.player.inventory.items.size()) {
			String itemDescription = gp.player.inventory.items.get(itemIndex).description;
			String description = itemDescription.substring(itemDescription.indexOf("\n"));
			int textLimit = gp.player.inventory.items.get(itemIndex).textDescriptionLimit;
			itemDescriptionHeight = (description.length() / textLimit) + 3;
			drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight, 200);
			for(String line: gp.player.inventory.items.get(itemIndex).description.split("\n")) {
				g2.drawString(line, textX, textY);
				textY += 32;
			}
		}
	}
	
	public void drawPlayerStat() {
		final int frameX = gp.tileSize * 2;
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
	
	public void drawPlayerHUD() {
		int position = (gp.tileSize / 2) - gp.originalTileSize;
		int width = gp.tileSize * (gp.player.maxLife / 2) + (gp.originalTileSize * 2);
		drawSubWindow(position, position, width, (gp.tileSize * 2) - gp.originalTileSize, 255);
		
		int x = gp.tileSize / 2;
		int y = gp.tileSize / 2;
		int i = 0;
		
		// Initialize Max Life
		while (i < gp.player.maxLife / 2) {
			g2.drawImage(emptyHeart, x, y, null);
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
		
		// Draw Max Mana
		width = gp.tileSize * Math.round(gp.player.maxMana / 2) + (gp.originalTileSize * 6);
		int initialX = gp.screenWidth - 215;
		drawSubWindow(initialX, position, width, (gp.tileSize * 2) - gp.originalTileSize, 255);
		
		x = (int) (gp.screenWidth - Math.round(gp.tileSize * 1.5));
		y = gp.tileSize / 2;
		i = 0;
		while(i < gp.player.maxMana) {
			g2.drawImage(emptyMana, x, y, null);
			i++;
			x -= 35;
		}
		
		// Draw Current Mana
		x = (int) (gp.screenWidth - Math.round(gp.tileSize * 1.5));
		y = gp.tileSize / 2;
		i = 0;
		while(i < gp.player.currentMana) {
			g2.drawImage(fullMana, x, y, null);
			i++;
			x -= 35;
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
		// USE Vector2DUtil
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
		// USE Vector2DUtil or Rectangle
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

	public void addMessage(String text) {
		messages.add(text);
		messageCounter.add(0);
	}
	
	public String getAdjustableText(String originalText, int textLimit) {
		String text = originalText;
		String newText = "";
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
