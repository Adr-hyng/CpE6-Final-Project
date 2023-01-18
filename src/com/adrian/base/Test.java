package com.adrian.base;

import com.adrian.utils.WeightedRandom;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WeightedRandom<Integer> roll = new WeightedRandom<>();
		roll.addEntry(1, 90);
		roll.addEntry(5, 10);
		System.out.println(roll.getRandom());
	}

}
