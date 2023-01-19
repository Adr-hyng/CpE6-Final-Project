package com.adrian.events;

import java.awt.Rectangle;

import com.adrian.utils.Vector2DUtil;

@SuppressWarnings("serial")
public class EventRect extends Rectangle{
	Vector2DUtil defaultRect = new Vector2DUtil(0, 0);
	boolean eventDone = false;
	
	public EventRect() {
	}
}
