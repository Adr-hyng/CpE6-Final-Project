package com.adrian.collisions;

import java.awt.Rectangle;

@SuppressWarnings("serial")
public class CollisionRect extends Rectangle{
	public int top, down, left, right;
	
	public CollisionRect(int top, int down, int left, int right) {
		this.top = top;
		this.down = down;
		this.left = left;
		this.right = right;
	}
}
