package com.adrian.tiles;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.adrian.GlobalTool;
import com.adrian.user_interface.GamePanel;
import com.adrian.utils.Vector2D;

public class TileManager {
	// THIS RELIES TO THE MAP EDITOR WITH TILE DATA
	GamePanel gp;
	public Tile[] tiles;
	public int mapTileNum[][];
	
	InputStream textData;
	BufferedReader openData;
	String mapName = "test1";
	
	ArrayList<String> tilesPath = new ArrayList<>();
	ArrayList<String> tilesCollisions = new ArrayList<>();
	
	public TileManager(GamePanel gp) {
		this.gp = gp;
		loadSpriteData("tilesData.txt");
		tiles = new Tile[tilesPath.size()]; // Total Sprite Tiles available
		this.getMapSize();
		this.getSprite();
		this.loadMap();
	}
	
	public void getMapSize() {
		try {
			textData = new FileInputStream(GlobalTool.assetsDirectory + "maps\\" + this.mapName + ".txt");
			openData = new BufferedReader(new InputStreamReader(textData));
			String line = openData.readLine();
			String maxTile[] = line.split(" ");
			gp.maxWorldCol = maxTile.length;
			gp.maxWorldRow = maxTile.length;
			mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
			openData.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap() {
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(GlobalTool.assetsDirectory + "maps\\" + this.mapName + ".txt"));
			int col = 0;
			int row = 0;
			
			boolean finishLoad = false;
			
			while (col < gp.maxWorldCol && row < gp.maxWorldRow || finishLoad) {
				String line = fileReader.readLine();
				if(line == null) break;
				
				while(col < gp.maxWorldCol) {
					String numbers[] = line.split(" ");
					int num = Integer.parseInt(numbers[col]);
					mapTileNum[col][row] = num;
					col++;
				}
				
				if(col == gp.maxWorldCol) {
					col = 0;
					row++;
				}
			}
			fileReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadSpriteData(String spriteDataPath) {
		try {
			textData = new FileInputStream(GlobalTool.assetsDirectory + "tiles_data\\" + spriteDataPath);
			openData = new BufferedReader(new InputStreamReader(textData));
			String line;
			
			while((line = openData.readLine()) != null) {
				tilesPath.add(line);
				tilesCollisions.add(openData.readLine());
			}
			openData.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getSprite() {
		for(int i = 0; i < tilesPath.size(); i++) {
			String imagePath;
			boolean isSolid;
			
			imagePath = tilesPath.get(i);
			
			if(tilesCollisions.get(i).equals("true")) {
				isSolid = true;
			} else {
				isSolid = false;
			}
			
			tiles[i] = new Tile(imagePath, isSolid, gp);
		}
	}
	
	public void draw(Graphics2D g) {
		int worldCol = 0;
		int worldRow = 0;
		
		// Loading Chunks / Tiles by creating a camera effect
		while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			int tileNum = mapTileNum[worldCol][worldRow];
			
			Vector2D worldView = new Vector2D(worldCol * gp.tileSize, worldRow * gp.tileSize);
			Vector2D screenView = new Vector2D(worldView.x - gp.player.worldPosition.x + gp.player.screen.x, worldView.y - gp.player.worldPosition.y + gp.player.screen.y);
			
			// To remove black spots when chunk loading.
			final int screenOffset = 2;
			
			// Only load tiles to the visible tiles in the player's screen.
			if(worldView.x + (gp.tileSize * screenOffset) > gp.player.worldPosition.x - gp.player.screen.x && 
			   worldView.x - (gp.tileSize * screenOffset) < gp.player.worldPosition.x + gp.player.screen.x &&
			   worldView.y + (gp.tileSize * screenOffset) > gp.player.worldPosition.y - gp.player.screen.y &&
			   worldView.y - (gp.tileSize * screenOffset) < gp.player.worldPosition.y + gp.player.screen.y) {
				g.drawImage(tiles[tileNum].image, (int) screenView.x, (int) screenView.y, null);	
			}
			
			worldCol++;
			
			if(worldCol == gp.maxWorldCol) {
				worldCol = 0;
				worldRow++;
			}
		}
	}
}
