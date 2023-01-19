package com.adrian.tiles.interactive;

import java.awt.Color;

import com.adrian.entity.base.InteractiveTile;
import com.adrian.entity.base.Player;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.utils.Sound;
import com.adrian.utils.Vector2DUtil;

public class DryTree extends InteractiveTile{
	
	GamePanel gp;

	public DryTree(GamePanel gp, int x, int y) {
		super(gp, x, y);
		this.gp = gp;
		this.currentLife = 3;
		this.worldPosition = new Vector2DUtil(gp.tileSize * x, gp.tileSize * y);
		this.down1 = this.loadSprite("interactive_tiles\\drytree.png", gp.tileSize, gp.tileSize);
		this.isDestructible = true;
	}
	
	@Override
	public void setSound() {
		Sound.CUT.playSE();
	}
	
	@Override
	public InteractiveTile getDestroyedForm() {
		this.setSound();
		InteractiveTile tile = new Trunk(gp, this.worldPosition.x / gp.tileSize, this.worldPosition.y / gp.tileSize);
		return tile;
	}
	
	@Override
	public boolean isItemValid(Player entity, Class<?> requiredType) {
		boolean isValid = false;
		if(entity.currentWeapon.weaponType == requiredType) {
			 isValid = true;
		}
		return isValid;
	}
	
	@Override
	public Color getParticleColor() {
		Color color = new Color(65, 50, 30);
		return color;
	}
	
	@Override
	public int getParticleSize() {
		int size = 6;
		return size;
	}
	
	@Override
	public int getParticleSpeed() {
		int speed = 1;
		return speed;
	}
	
	@Override
	public int getParticleMaxLife() {
		int maxLife = 15;
		return maxLife;
	}

}
