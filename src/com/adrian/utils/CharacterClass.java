package com.adrian.utils;


public interface CharacterClass {
	enum Selection{
		MAGE("Mage"),
		WARRIOR("Warrior");
		
		public final String name;
		private Selection(String name) {
	      this.name = name;
	   }
	}
}
