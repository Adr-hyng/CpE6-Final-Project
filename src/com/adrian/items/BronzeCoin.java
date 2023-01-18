package com.adrian.items;

import com.adrian.items.base.Coin;
import com.adrian.user_interfaces.GamePanel;

public class BronzeCoin extends Coin {
	public BronzeCoin(GamePanel gp) {
		super(gp);
		this.name = "Bronze Coin";
		this.value = 1;
		this.getSprite();
	}

	protected void getSprite() {
		this.image = this.loadSprite("objects\\coin_bronze.png");
	}
}
