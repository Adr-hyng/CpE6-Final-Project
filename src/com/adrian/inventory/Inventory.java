package com.adrian.inventory;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.adrian.items.base.Item;
import com.adrian.items.base.ItemTypes;
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
		this.addItem(ItemTypes.Weapon.Axe.getRustyAxe(gp));
		this.addItem(ItemTypes.Shield.getWoodenShield(gp));
	}
	
	public <T extends Item> void addItem(T item) {
		if(!this.isFull()) {
			this.items.add(item); 
		} else {
			System.out.println("You cannot add " + item.name + " to slot " + (this.items.size()) );
		}
	}
	
	public <T extends Item> void removeItem(T item) {
		if(!this.items.isEmpty()) {
			this.items.remove(item);
		} else {
			System.out.println("You cannot remove any item anymore. There's none left.");
		}
	}
	
	public boolean isFull() {
		return !(this.items.size() < this.size);
	}
	
	public <T extends Item> T getItem(int index) {
		return (T) this.items.get(index);
	}
	
	public int getItemIndex() {
		int itemIndex = gp.ui.inventorySlotCol + (gp.ui.inventorySlotRow * gp.ui.inventoryMaxSlotX);
		return itemIndex;
	}
	
	public <T extends Item> boolean hasItem(T item) {
		return this.items.stream().map(_item -> _item.name).collect(Collectors.toList()).contains(item.name);
	}
	
	public <T extends Item> T popItem(T item) {
		T x = (T) this.items.stream().filter(selectedItem -> selectedItem.name == item.name).findAny().get();
		this.removeItem(x);
		return x;
	}
}
