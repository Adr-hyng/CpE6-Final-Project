package com.adrian.items.equipments;

import com.adrian.base.ItemTypes;
import com.adrian.base.Weapon;
import com.adrian.user_interfaces.GamePanel;

public class CommonSword extends Weapon{
	public CommonSword(GamePanel gp) {
		super(gp);
		this.name = "Normal Sword";
		this.weaponType = ItemTypes.Weapon.Sword.class;
		this.description = "[" + name + "]\nAn old rusty sword.";
		this.attackValue = 2;
		this.attackArea.width = 36;
		this.attackArea.height = 36;
		this.getSprite();
	}

	@Override
	protected void getSprite() {
		this.image = this.loadSprite("objects\\sword_normal.png");
	}
}
