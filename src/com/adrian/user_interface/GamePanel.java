package com.adrian.user_interface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.adrian.collisions.collisionHandler;
import com.adrian.entity.Entity;
import com.adrian.entity.Player;
import com.adrian.inputs.KeyHandler;
import com.adrian.objects.Object;
import com.adrian.sounds.Sound;
import com.adrian.tiles.TileManager;
import com.adrian.utils.AssetSetter;
import com.adrian.utils.Vector2D;

public class GamePanel extends JPanel implements Runnable {
	
	private static final long serialVersionUID = 1L;
	// Tile Size
	final int originalTileSize = 16; // Initial Tile Size = 16 x 16 
	final int scale = 3;
	
	// Screen Resolution
	public final int tileSize = originalTileSize * scale;
	public final int maxScreenCol = 16; //16
	public final int maxScreenRow = 12; // 12
	public final int screenWidth = tileSize * maxScreenCol;
	public final int screenHeight = tileSize * maxScreenRow;
	
	// World Settings
	public int maxWorldCol;
	public int maxWorldRow;
	public final int worldWidth = tileSize * maxScreenCol;
	public final int worldHeight = tileSize * maxScreenRow;
	
	// Frame per second
	final int FPS = 60;
	
	// Tile Manager
	public TileManager tileManager = new TileManager(this);
	
	// Key Input Handler
	KeyHandler keyInput = new KeyHandler(this);
	
	// Game Loop
	Thread gameThread;
	
	// Collision Handler
	public collisionHandler collisionHandler = new collisionHandler(this);
	
	// Asset Handler
	public AssetSetter assetHandler = new AssetSetter(this);
	
	// User Interface (UI)
	public UserInterface ui = new UserInterface(this);
	
	// Sounds
	Sound music = new Sound();
	Sound soundEffects = new Sound();
	
	// NPC
	public Entity npcs[] = new Entity[10];
	
	// Objects
	public Object objects[] = new Object[10];
	
	// Player
	public Player player = new Player(this, keyInput, new Vector2D(tileSize * 28, tileSize * 21));	
	
	
	
	// Game State (Use enums)
	public int gameState;
	public final int playState = 1;
	public final int pauseState = 2;
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth + (tileSize / 2) - (originalTileSize / 2), screenHeight + tileSize - (originalTileSize / 2)));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyInput);
		this.setFocusable(true);
	}
	
	public void worldSetup() {
		assetHandler.setObject();
		assetHandler.setNPC();
		playMusic(0);
		gameState = playState;
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		
		double frameInterval = 1_000_000_000 / FPS;
		double deltaTime = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		while(gameThread != null) {
			
			// DeltaTime frame rendering
			currentTime = System.nanoTime();
			
			deltaTime += (currentTime - lastTime) / frameInterval;
			
			lastTime = currentTime;
			
			if(deltaTime >= 1) {
				update();
				repaint();
				deltaTime--;
			}
			
		}
	}
	
	public void update() {
		if(gameState == playState) {
			player.update();
			
			for(int i = 0; i < npcs.length; i++) {
				if(npcs[i] != null) {
					npcs[i].update();
				}
			}
		}
		else if(gameState == pauseState) {
			
		}
	}
	
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		// !!DEBUG: Time Complexity
		long drawStart = 0;
		if(keyInput.checkDrawTime) drawStart = System.nanoTime();
		
		// Tiles
		tileManager.draw(g2);
		
		// Objects / NPC
		assetHandler.draw(g2);
		
		// Player
		player.draw(g2);
		
		// UI
		ui.draw(g2);
		
		// !!DEBUG: Time Complexity (0.003, 0.0017)
		if(keyInput.checkDrawTime) {
			long drawEnd = System.nanoTime();
			long timePassed = drawEnd - drawStart;
			g2.setColor(Color.white);
			g2.drawString("Draw Time: " + timePassed, 10, 40);
		}
		
		g2.dispose();
	}
	
	public void playMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();
	}
	
	public void stopMusic() {
		music.stop();
	}
	
	public void playSoundEffect(int i) {
		soundEffects.setFile(i);
		soundEffects.play();
	}
}
