package com.adrian.user_interface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import com.adrian.collisions.CollisionHandler;
import com.adrian.database.DatabaseManager;
import com.adrian.entity.Entity;
import com.adrian.entity.Player;
import com.adrian.events.EventHandler;
import com.adrian.inputs.KeyHandler;
import com.adrian.inputs.MouseHandler;
import com.adrian.objects.ItemObject;
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
	
	public DatabaseManager db = new DatabaseManager();
	
	// Tile Manager
	public TileManager tileManager = new TileManager(this);
	
	// Input Handler
	public KeyHandler keyInput = new KeyHandler(this);
	MouseHandler mouseInput = new MouseHandler();
	
	// Game Loop
	public Thread gameThread;
	
	// Collision Handler
	public CollisionHandler collisionHandler = new CollisionHandler(this);
	
	// Asset Handler
	public AssetSetter assetHandler = new AssetSetter(this);
	
	public EventHandler eventHandler = new EventHandler(this);
	
	// User Interface (UI)
	public UserInterface ui = new UserInterface(this);
	
	// Sounds
	Sound music = new Sound();
	public Sound soundEffects = new Sound();
	
	// NPC
	public Entity npcs[] = new Entity[10];
	
	// Obstacles
	public Entity obstacles[] = new Entity[10];
	
	// Monsters
	public Entity monsters[] = new Entity[20];
	
	// Item Objects
	public ItemObject itemObjects[] = new ItemObject[10];
	
	// Entity List
	public ArrayList<Entity> entityList = new ArrayList<>();
	
	// Player
	final int respawnX = tileSize * 30;
	final int respawnY = tileSize * 28;
	public Player player;	
	
	public int gameState;
	public boolean canPlay = true;
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth + (tileSize / 2) - (originalTileSize / 2), screenHeight + tileSize - (originalTileSize / 2)));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyInput);
		this.setFocusable(true);
	}
	
	// <---- START OF DATABASE CONNECTION ---->
	public void connectDB() throws SQLException {
		db.createConnection("sys");
		String tableName = "entity";
		String schemaName = "GameDB";
		if(! (db.checkDB(schemaName)) ) {
			db.createDatabase(schemaName);
			db.createConnection(schemaName);
			db.createTable(tableName, "hp INT(64), "
					+ "maxhp INT(64),"
					+ "x INT(64),"
					+ "type VARCHAR(255),"
					+ "y INT(64)");
			db.addDB(new String[] {"0", "0", "0", "0", "Player"}, "entity", "maxhp, hp, x, y, type");
		}
		else {
			db.createConnection(schemaName);
		}
	}
	
	public void loadGame(Entity entity, int index) {
		try {
			connectDB();
			entity.worldPosition.x = Integer.parseInt((String) db.readDB("x", "entity", "idNo = " + index + ";").get(0));
			entity.worldPosition.y = Integer.parseInt((String) db.readDB("y", "entity", "idNo = " + index + ";").get(0));
			entity.currentLife = Integer.parseInt((String) db.readDB("hp", "entity", "idNo = " + index + ";").get(0));
			entity.maxLife = Integer.parseInt((String) db.readDB("maxhp", "entity", "idNo = " + index + ";").get(0));
		} catch (NumberFormatException | SQLException | NullPointerException e) {
			System.out.println("Current Saved File is corrupted. Please create new game.");
			e.printStackTrace();
		}
	}
	
	public void newGame(Entity entity, int index) {
		// Reset
		worldSetup();
		try {
			connectDB();
			db.updateDB((int) player.worldPosition.x, "entity", "x", index);
			db.updateDB((int) player.worldPosition.y, "entity", "y", index);
			db.updateDB(entity.currentLife, "entity", "hp", index);
			db.updateDB(entity.maxLife, "entity", "maxhp", index);
			
			entity.worldPosition.x = Integer.parseInt((String) db.readDB("x", "entity", "idNo = " + index + ";").get(0));
			entity.worldPosition.y = Integer.parseInt((String) db.readDB("y", "entity", "idNo = " + index + ";").get(0));
			entity.currentLife = Integer.parseInt((String) db.readDB("hp", "entity", "idNo = " + index + ";").get(0));
			entity.maxLife = Integer.parseInt((String) db.readDB("maxhp", "entity", "idNo = " + index + ";").get(0));
			this.worldSetup();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void processDB(Entity entity, int index) throws NumberFormatException, SQLException {
		int dbX = Integer.parseInt((String) db.readDB("x", "entity", "idNo = " + index + ";").get(0));
		int dbY = Integer.parseInt((String) db.readDB("y", "entity", "idNo = "+ index +";").get(0));
		
		if(dbX == 0 && dbY == 0) {
			db.updateDB((int) entity.worldPosition.x, "entity", "x", index);
			db.updateDB((int) entity.worldPosition.y, "entity", "y", index);
			db.updateDB((int) entity.currentLife, "entity", "hp", index);
		} else {
			entity.worldPosition.x = Integer.parseInt((String) db.readDB("x", "entity", "idNo = " + index + ";").get(0));
			entity.worldPosition.y = Integer.parseInt((String) db.readDB("y", "entity", "idNo = " + index + ";").get(0));
			entity.currentLife = Integer.parseInt((String) db.readDB("hp", "entity", "idNo = " + index + ";").get(0));
		}
	}
	
	public void saveGame(Entity entity) {
		try {
			connectDB();
			db.updateDB((int) entity.worldPosition.x, "entity", "x", 1);
			db.updateDB((int) entity.worldPosition.y, "entity", "y", 1);
			db.updateDB((int) entity.currentLife, "entity", "hp", 1);
			db.updateDB((int) entity.maxLife, "entity", "maxhp", 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// <---- END OF DATABASE CONNECTION ---->
	
	public void worldSetup() {
		try {
			this.connectDB();
		} catch (SQLException | NumberFormatException e) {
			e.printStackTrace();
		}
		player = new Player(this, keyInput, new Vector2D(respawnX, respawnY));
		assetHandler.reset();
		assetHandler.setItemObject();
		assetHandler.setObstacle();
		assetHandler.setNPC();
		assetHandler.setMonster();
		gameState = GameState.Menu.state;
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
		if (gameState == GameState.Menu.state) {
			
		}
		
		else if(gameState == GameState.Continue.state) {
			player.update();
			for(int i = 0; i < npcs.length; i++) {
				if(npcs[i] != null) {
					npcs[i].update();
				}
			}
			
			for(int i = 0; i < obstacles.length; i++) {
				if(obstacles[i] != null) {
					obstacles[i].update();
				}
			}
			
			for(int i = 0; i < monsters.length; i++) {
				if(monsters[i] != null) {
					if (monsters[i].isAlive && !monsters[i].isDying) {
						monsters[i].update();
					}
					else if (!monsters[i].isAlive) {
						monsters[i] = null;
					}
				}
			}
		}
		else if(gameState == GameState.Pause.state) {

		}
		
		else if(gameState == GameState.Dialogue.state) {
			if(keyInput.haveKeyPressed.get("ESC")) {
				gameState = GameState.Continue.state;
			}
		}
	}
	
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		if(gameState == GameState.Menu.state) {
			// UI
			ui.draw(g2);
		}
		
		else {
			// Tiles
			tileManager.draw(g2);
			
			// Objects / NPC / Player
			entityList.add(player);
			assetHandler.compressEntities();
			
			Collections.sort(entityList, new Comparator<Entity>() {

				@Override
				public int compare(Entity e1, Entity e2) {
					int result = Integer.compare((int) e1.worldPosition.y, (int) e2.worldPosition.y);
					return result;
				}
				
			});
			
			assetHandler.draw(g2);
			assetHandler.clearEntities();
			
			// UI
			ui.draw(g2);
			
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
