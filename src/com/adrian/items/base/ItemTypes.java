package com.adrian.items.base;

import com.adrian.items.equipments.*;
import com.adrian.user_interfaces.GamePanel;

class Items{
	protected static Item item = null;
}

public interface ItemTypes{
	interface Weapon{
		abstract class Axe extends Items{
			public static Item getRustyAxe(GamePanel gp) {
				item = new RustyAxe(gp);
				return item;
			}
		}
		
		abstract class Sword extends Items{
			public static Item getCommonSword(GamePanel gp) {
				item = new CommonSword(gp);
				return item;
			}
		}
		
		class Spear{
		}
	}
	
	abstract class Shield extends Items{
		public static Item getWoodenShield(GamePanel gp) {
			item = new WoodenShield(gp);
			return item;
		}
		
		public static Item getBlueShield(GamePanel gp) {
			item = new BlueShield(gp);
			return item;
		}
	}
	
	interface Consumable{
		enum Potion{
			RedPotion();
		}
	}
	
	interface NotObtainable{
		enum Coin{
			BronzeCoin();
		}
	}
}

