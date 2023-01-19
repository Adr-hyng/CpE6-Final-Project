package com.adrian.entity.base;

import java.awt.Color;
import java.awt.Graphics2D;

import com.adrian.user_interfaces.GamePanel;
import com.adrian.utils.Vector2DUtil;

public class Particle extends Entity{
	Entity generator;
	Color color;
	int size;
	int speed;
	int xd;
	int yd;

	public Particle(
			GamePanel gp, 
			Entity generator, 
			Color color, 
			int size, 
			int speed, 
			int maxLife, 
			int xd, 
			int yd
			) {
		super(gp);
		
		this.generator = generator;
		this.color = color;
		this.size = size;
		this.speed = speed;
		this.maxLife = maxLife;
		this.xd = xd;
		this.yd = yd;
		
		
		int offset = (gp.tileSize / 2) - (size / 2);
		this.currentLife = this.maxLife;
		this.worldPosition = new Vector2DUtil(generator.worldPosition.x + offset, generator.worldPosition.y + offset);
	}

	@Override
	protected void getSprite() {
	}
	
	public void update() {
		this.currentLife--;
		
		if(this.currentLife < this.maxLife / 2) {
			yd++;
		}
		
		this.worldPosition.x += this.xd * this.speed;
		this.worldPosition.y += this.yd * this.speed;
		if(this.currentLife <= 0) {
			this.isAlive = false;
		}
		
	}
	
	public void draw(Graphics2D g2) {
		Vector2DUtil screenPosition = new Vector2DUtil(
				this.worldPosition.x - gp.player.worldPosition.x + gp.player.screen.x,
				this.worldPosition.y - gp.player.worldPosition.y + gp.player.screen.y
				);
		
		g2.setColor(color);
		g2.fillRect(screenPosition.x, screenPosition.y, size, size);
	}
}
