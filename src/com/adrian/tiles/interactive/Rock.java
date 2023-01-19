package com.adrian.tiles.interactive;

import com.adrian.entity.base.InteractiveTile;
import com.adrian.entity.base.Player;
import com.adrian.user_interfaces.GamePanel;
import com.adrian.utils.Vector2DUtil;

public class Rock extends InteractiveTile{
	
	GamePanel gp;

	public Rock(GamePanel gp, int x, int y) {
		super(gp, x, y);
		this.gp = gp;
		this.currentLife = 3;
		this.worldPosition = new Vector2DUtil(gp.tileSize * x, gp.tileSize * y);
		this.down1 = this.loadSprite("interactive_tiles\\rock.png", gp.tileSize, gp.tileSize);
		this.isDestructible = true;
	}
	
	@Override
	public boolean isItemValid(Player entity, Class<?> requiredType) {
		boolean isValid = false;
		if(entity.currentWeapon.weaponType == requiredType) {
			 isValid = true;
		}
		return isValid;
	}

}
