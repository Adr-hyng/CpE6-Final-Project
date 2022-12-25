package com.adrian.entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.adrian.utils.Vector2D;

public class Entity {
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public String direction;
	
	public double movementSpeed;
	protected boolean isMoving = false;
	public Vector2D worldPosition;
	
	public int spriteCounter = 0;
	public int spriteNum = 1;
	
	public Rectangle solidArea;
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collisionOn = false;
}
