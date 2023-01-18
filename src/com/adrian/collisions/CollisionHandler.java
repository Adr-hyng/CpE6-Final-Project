package com.adrian.collisions;

import com.adrian.entity.base.Entity;
import com.adrian.user_interfaces.GamePanel;

public class CollisionHandler {
	GamePanel gp;
	CollisionRect entityWorld;
	CollisionRect entityDirection;
	
	public CollisionHandler(GamePanel gp) {
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
		
		int tileNum1 = 0;
		int tileNum2 = 0;
		
		switch(entity.direction) {
		case "up":
			entityDirection.top = (int) ((entityWorld.top - entity.movementSpeed) / gp.tileSize);
			tileNum1 = gp.tileManager.mapTileNum[entityDirection.left][entityDirection.top];
			tileNum2 = gp.tileManager.mapTileNum[entityDirection.right][entityDirection.top];
			break;
		case "down":
			entityDirection.down = (int) ((entityWorld.down + entity.movementSpeed) / gp.tileSize);
			tileNum1 = gp.tileManager.mapTileNum[entityDirection.left][entityDirection.down];
			tileNum2 = gp.tileManager.mapTileNum[entityDirection.right][entityDirection.down];
			break;
		case "left":
			entityDirection.left = (int) ((entityWorld.left - entity.movementSpeed) / gp.tileSize);
			tileNum1 = gp.tileManager.mapTileNum[entityDirection.left][entityDirection.top];
			tileNum2 = gp.tileManager.mapTileNum[entityDirection.left][entityDirection.down];
			break;
		case "right":
			entityDirection.right = (int) ((entityWorld.right + entity.movementSpeed) / gp.tileSize);
			tileNum1 = gp.tileManager.mapTileNum[entityDirection.right][entityDirection.top];
			tileNum2 = gp.tileManager.mapTileNum[entityDirection.right][entityDirection.down];
			break;
		}
		
		if(gp.tileManager.tiles[tileNum1].collision || gp.tileManager.tiles[tileNum2].collision) entity.collisionOn = true;
	}
	
	public int collideObject(Entity entity, boolean isPlayer) {
		int index = 999;
		
		for(int i = 0; i < gp.itemObjects.length; i++) {
			if(gp.itemObjects[i] == null) continue;
			entity.solidArea.x = (int) (entity.worldPosition.x + entity.solidArea.x);
			entity.solidArea.y = (int) (entity.worldPosition.y + entity.solidArea.y);

			gp.itemObjects[i].solidArea.x = (int) (gp.itemObjects[i].worldPosition.x + gp.itemObjects[i].solidArea.x);
			gp.itemObjects[i].solidArea.y = (int) (gp.itemObjects[i].worldPosition.y + gp.itemObjects[i].solidArea.y);
			
			switch(entity.direction) {
			case "up": entity.solidArea.y -= entity.movementSpeed; break;
			case "down": entity.solidArea.y += entity.movementSpeed; break;
			case "left": entity.solidArea.x -= entity.movementSpeed; break;
			case "right": entity.solidArea.x += entity.movementSpeed; break;
			}
			
			if(entity.solidArea.intersects(gp.itemObjects[i].solidArea)) {
				if(gp.itemObjects[i].collision) entity.collisionOn = true;
				if(isPlayer) index = i;
			}
			
			entity.solidArea.x = entity.solidAreaDefaultX;
			entity.solidArea.y = entity.solidAreaDefaultY;
			gp.itemObjects[i].solidArea.x = gp.itemObjects[i].solidAreaDefaultX;
			gp.itemObjects[i].solidArea.y = gp.itemObjects[i].solidAreaDefaultY;
		}
		return index;
	}
	
	public int collideEntity(Entity callerEntity, Entity[] otherEntities) {
		int index = 999;
		int len = 0;
		try {
			len = otherEntities.length;
		} catch (NullPointerException e) {
			System.out.println(this.getClass().getName());
		}
		for(int i = 0; i < len; i++) {
			if(otherEntities[i] == null || otherEntities[i].hashCode() == callerEntity.hashCode()) continue;
			callerEntity.solidArea.x = (int) (callerEntity.worldPosition.x + callerEntity.solidArea.x);
			callerEntity.solidArea.y = (int) (callerEntity.worldPosition.y + callerEntity.solidArea.y);
			
			otherEntities[i].solidArea.x = (int) (otherEntities[i].worldPosition.x + otherEntities[i].solidArea.x);
			otherEntities[i].solidArea.y = (int) (otherEntities[i].worldPosition.y + otherEntities[i].solidArea.y);
			
			switch(callerEntity.direction) {
			case "up": callerEntity.solidArea.y -= callerEntity.movementSpeed; break;
			case "down": callerEntity.solidArea.y += callerEntity.movementSpeed; break;
			case "left": callerEntity.solidArea.x -= callerEntity.movementSpeed; break;
			case "right": callerEntity.solidArea.x += callerEntity.movementSpeed; break;
			}
			
			if(callerEntity.solidArea.intersects(otherEntities[i].solidArea)) {
				callerEntity.collisionOn = true;
				callerEntity.isMoving = false;
				index = i;
			}
			
			callerEntity.solidArea.x = callerEntity.solidAreaDefaultX;
			callerEntity.solidArea.y = callerEntity.solidAreaDefaultY;
			otherEntities[i].solidArea.x = otherEntities[i].solidAreaDefaultX;
			otherEntities[i].solidArea.y = otherEntities[i].solidAreaDefaultY;
		}
		return index;
	}
	
	public boolean collidePlayer(Entity entity) {
		boolean contactPlayer = false;
		entity.solidArea.x = (int) (entity.worldPosition.x + entity.solidArea.x);
		entity.solidArea.y = (int) (entity.worldPosition.y + entity.solidArea.y);
		
		gp.player.solidArea.x = (int) (gp.player.worldPosition.x + gp.player.solidArea.x);
		gp.player.solidArea.y = (int) (gp.player.worldPosition.y + gp.player.solidArea.y);
		
		switch(entity.direction) {
		case "up": entity.solidArea.y -= entity.movementSpeed; break;
		case "down": entity.solidArea.y += entity.movementSpeed; break;
		case "left": entity.solidArea.x -= entity.movementSpeed; break;
		case "right": entity.solidArea.x += entity.movementSpeed; break;
		}
		if(entity.solidArea.intersects(gp.player.solidArea)) {
			entity.collisionOn = true;
			entity.isMoving = false;
			contactPlayer = true;
		}
		
		entity.solidArea.x = entity.solidAreaDefaultX;
		entity.solidArea.y = entity.solidAreaDefaultY;
		gp.player.solidArea.x = gp.player.solidAreaDefaultX;
		gp.player.solidArea.y = gp.player.solidAreaDefaultY;
		
		return contactPlayer;
	}
}
