package com.adrian.objects;

import com.adrian.user_interface.GamePanel;
import com.adrian.user_interface.GameState;

public class Boots extends ItemObject {
	public Boots(GamePanel gp) {
		super(gp);
		this.name = "Boots";
		this.getSprite();
	}

	protected void getSprite() {
		this.image = this.loadSprite("objects\\boots.png");
	}
	
	public void setDialogue () {
		gp.ui.currentDialogue = "You obtained a " + this.name;
		gp.playSoundEffect(9);
		gp.gameState = GameState.Dialogue.state;
	}
}
