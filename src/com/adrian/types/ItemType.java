package com.adrian.types;

import java.util.HashMap;
import java.util.Map;

public enum ItemType implements BaseType {
	Weapon("Air"), Shield("Air"), Consumable("Air");

    private static final Map<String, ItemType> BY_NAME = new HashMap<>();
    
    static {
        for (ItemType weapon : values()) {
            BY_NAME.put(weapon.name, weapon);
        }
    }

    public final String name;

    private ItemType(String name) {
        this.name = name;
    }
}
