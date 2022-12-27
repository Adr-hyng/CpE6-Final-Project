package com.adrian.events;

import java.awt.Rectangle;

import com.adrian.utils.Vector2D;

@SuppressWarnings("serial")
public class EventRect extends Rectangle{
	Vector2D defaultRect = new Vector2D(0, 0);
	boolean eventDone = false;
	
	public EventRect() {
	}
}
