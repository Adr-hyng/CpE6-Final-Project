package com.adrian.user_interface;

public enum GameState {
	Menu(0),
    Play(1),
    NewClass(2),
    Pause(100),
    Dialogue(101);

    public final int state;

    private GameState(int state) {
        this.state = state;
	}
}
