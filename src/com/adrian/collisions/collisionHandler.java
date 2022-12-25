package com.adrian.collisions;

import com.adrian.entity.Entity;
import com.adrian.user_interface.GamePanel;

public class collisionHandler {
	GamePanel gp;
	CollisionRect entityWorld;
	CollisionRect entityDirection;
	
	public collisionHandler(GamePanel gp) {
		this.gp = gp;
	}
	
	public void collideTile(Entity entity) {
		entityWorld = new CollisionRect(
				(int) (entity.worldPosition.y + entity.solidArea.y), 
				(int) (entity.worldPosition.y + entity.solidArea.y + entity.solidArea.height), 
				(int) (entity.worldPosition.x + entity.solidArea.x), 
				(int) (entity.worldPosition.x + entity.solidArea.x + entity.solidArea.width));
		
		entityDirection = new CollisionRect(
				entityWorld.top / gp.tileSize, 
				entityWorld.down / gp.tileSize, 
				entityWorld.left / gp.tileSize, 
				entityWorld.right / gp.tileSize);
		
		int tileNum1, tileNum2;
		
		switch(entity.direction) {
		case "up":
			entityDirection.top = (int) ((entityWorld.top - entity.movementSpeed) / gp.tileSize);
			tileNum1 = gp.tileManager.mapTileNum[entityDirection.left][entityDirection.top];
			tileNum2 = gp.tileManager.mapTileNum[entityDirection.right][entityDirection.top];
			if(gp.tileManager.tiles[tileNum1].collision || gp.tileManager.tiles[tileNum2].collision) {
				entity.collisionOn = true;
			}
			break;
		case "down":
			entityDirection.down = (int) ((entityWorld.down + entity.movementSpeed) / gp.tileSize);
			tileNum1 = gp.tileManager.mapTileNum[entityDirection.left][entityDirection.down];
			tileNum2 = gp.tileManager.mapTileNum[entityDirection.right][entityDirection.down];
			if(gp.tileManager.tiles[tileNum1].collision || gp.tileManager.tiles[tileNum2].collision) {
				entity.collisionOn = true;
			}
			break;
		case "left":
			entityDirection.left = (int) ((entityWorld.left - entity.movementSpeed) / gp.tileSize);
			tileNum1 = gp.tileManager.mapTileNum[entityDirection.left][entityDirection.top];
			tileNum2 = gp.tileManager.mapTileNum[entityDirection.left][entityDirection.down];
			if(gp.tileManager.tiles[tileNum1].collision || gp.tileManager.tiles[tileNum2].collision) {
				entity.collisionOn = true;
			}
			break;
		case "right":
			entityDirection.right = (int) ((entityWorld.right + entity.movementSpeed) / gp.tileSize);
			tileNum1 = gp.tileManager.mapTileNum[entityDirection.right][entityDirection.top];
			tileNum2 = gp.tileManager.mapTileNum[entityDirection.right][entityDirection.down];
			if(gp.tileManager.tiles[tileNum1].collision || gp.tileManager.tiles[tileNum2].collision) {
				entity.collisionOn = true;
			}
			break;
		}
		
	}
	
	public int collideObject(Entity entity, boolean isPlayer) {
		int index = 999;
		
		for(int i = 0; i < gp.objects.length; i++) {
			if(gp.objects[i] == null) continue;
			
			// Get entity's solid area position
			entity.solidArea.x = (int) (entity.worldPosition.x + entity.solidArea.x);
			entity.solidArea.y = (int) (entity.worldPosition.y + entity.solidArea.y);
			
			// Get the object's solid area position
			gp.objects[i].solidArea.x = (int) (gp.objects[i].worldPosition.x + gp.objects[i].solidArea.x);
			gp.objects[i].solidArea.y = (int) (gp.objects[i].worldPosition.y + gp.objects[i].solidArea.y);
			
			switch(entity.direction) {
			case "up":
				entity.solidArea.y -= entity.movementSpeed;
				if(entity.solidArea.intersects(gp.objects[i].solidArea)) {
					if(gp.objects[i].collision) {
						entity.collisionOn = true;
					}
					if(isPlayer) {
						index = i;
					}
				}
				break;
			case "down":
				entity.solidArea.y += entity.movementSpeed;
				if(entity.solidArea.intersects(gp.objects[i].solidArea)) {
					if(gp.objects[i].collision) {
						entity.collisionOn = true;
					}
					if(isPlayer) {
						index = i;
					}
				}
				break;
			case "left":
				entity.solidArea.x -= entity.movementSpeed;
				if(entity.solidArea.intersects(gp.objects[i].solidArea)) {
					if(gp.objects[i].collision) {
						entity.collisionOn = true;
					}
					if(isPlayer) {
						index = i;
					}
				}
				break;
			case "right":
				entity.solidArea.x += entity.movementSpeed;
				if(entity.solidArea.intersects(gp.objects[i].solidArea)) {
					if(gp.objects[i].collision) {
						entity.collisionOn = true;
					}
					if(isPlayer) {
						index = i;
					}
				}
				break;
			}
			
			entity.solidArea.x = entity.solidAreaDefaultX;
			entity.solidArea.y = entity.solidAreaDefaultY;
			gp.objects[i].solidArea.x = gp.objects[i].solidAreaDefaultX;
			gp.objects[i].solidArea.y = gp.objects[i].solidAreaDefaultY;
		}
		
		return index;
	}
	
	public int collideEntity(Entity entity, Entity[] target) {
		int index = 999;
		
		for(int i = 0; i < target.length; i++) {
			if(target[i] == null) continue;
			// Get entity's solid area position
			entity.solidArea.x = (int) (entity.worldPosition.x + entity.solidArea.x);
			entity.solidArea.y = (int) (entity.worldPosition.y + entity.solidArea.y);
			
			// Get the object's solid area position
			target[i].solidArea.x = (int) (target[i].worldPosition.x + target[i].solidArea.x);
			target[i].solidArea.y = (int) (target[i].worldPosition.y + target[i].solidArea.y);
			
			switch(entity.direction) {
			case "up":
				entity.solidArea.y -= entity.movementSpeed;
				if(entity.solidArea.intersects(target[i].solidArea)) {
					entity.collisionOn = true;
					entity.isMoving = false;
					index = i;
				}
				break;
			case "down":
				entity.solidArea.y += entity.movementSpeed;
				if(entity.solidArea.intersects(target[i].solidArea)) {
					entity.collisionOn = true;
					entity.isMoving = false;
					index = i;
				}
				break;
			case "left":
				entity.solidArea.x -= entity.movementSpeed;
				if(entity.solidArea.intersects(target[i].solidArea)) {
					entity.collisionOn = true;
					entity.isMoving = false;
					index = i;
				}
				break;
			case "right":
				entity.solidArea.x += entity.movementSpeed;
				if(entity.solidArea.intersects(target[i].solidArea)) {
					entity.collisionOn = true;
					entity.isMoving = false;
					index = i;
				}
				break;
			}
			
			entity.solidArea.x = entity.solidAreaDefaultX;
			entity.solidArea.y = entity.solidAreaDefaultY;
			target[i].solidArea.x = target[i].solidAreaDefaultX;
			target[i].solidArea.y = target[i].solidAreaDefaultY;
		}
		return index;
	}
	
	public void collidePlayer(Entity entity) {
		// Get entity's solid area position
		entity.solidArea.x = (int) (entity.worldPosition.x + entity.solidArea.x);
		entity.solidArea.y = (int) (entity.worldPosition.y + entity.solidArea.y);
		
		// Get the object's solid area position
		gp.player.solidArea.x = (int) (gp.player.worldPosition.x + gp.player.solidArea.x);
		gp.player.solidArea.y = (int) (gp.player.worldPosition.y + gp.player.solidArea.y);
		
		switch(entity.direction) {
		case "up":
			entity.solidArea.y -= entity.movementSpeed;
			if(entity.solidArea.intersects(gp.player.solidArea)) {
				entity.collisionOn = true;
				entity.isMoving = false;
			}
			break;
		case "down":
			entity.solidArea.y += entity.movementSpeed;
			if(entity.solidArea.intersects(gp.player.solidArea)) {
				entity.collisionOn = true;
				entity.isMoving = false;
			}
			break;
		case "left":
			entity.solidArea.x -= entity.movementSpeed;
			if(entity.solidArea.intersects(gp.player.solidArea)) {
				entity.collisionOn = true;
				entity.isMoving = false;
			}
			break;
		case "right":
			entity.solidArea.x += entity.movementSpeed;
			if(entity.solidArea.intersects(gp.player.solidArea)) {
				entity.collisionOn = true;
				entity.isMoving = false;
			}
			break;
		}
		
		entity.solidArea.x = entity.solidAreaDefaultX;
		entity.solidArea.y = entity.solidAreaDefaultY;
		gp.player.solidArea.x = gp.player.solidAreaDefaultX;
		gp.player.solidArea.y = gp.player.solidAreaDefaultY;
	}
}
