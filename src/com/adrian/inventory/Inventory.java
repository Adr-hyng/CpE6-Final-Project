package com.adrian.inventory;

import java.util.ArrayList;

import com.adrian.base.Item;
import com.adrian.items.Boots;
import com.adrian.items.Key;
import com.adrian.items.equipments.CommonSword;
import com.adrian.user_interfaces.GamePanel;

public class Inventory{
	public GamePanel gp;
	public ArrayList<Item> items = new ArrayList<>();
	public final int size;
	
	public Inventory(GamePanel gp) {
		this.gp = gp;
		this.size = gp.ui.inventoryMaxSlotX * gp.ui.inventoryMaxSlotY; 
		this.setDefaultItems();
	}
	
	public void setDefaultItems() {
		this.addItem(new Boots(this.gp));
		this.addItem(new CommonSword(this.gp));
		this.addItem(new Key(this.gp));
		this.addItem(new Boots(this.gp));
		this.addItem(new CommonSword(this.gp));
	}
	
	public void addItem(Item item) {
		if(this.items.size() < this.size) {
			this.items.add(item); 
		} else {
			System.out.println("You cannot add " + item.name + " to slot " + (this.items.size()) );
		}
	}
	
	public int getItemIndex() {
		int itemIndex = gp.ui.inventorySlotCol + (gp.ui.inventorySlotRow * gp.ui.inventoryMaxSlotX);
		return itemIndex;
	}
	
	public void draw() {
		
	}
}
