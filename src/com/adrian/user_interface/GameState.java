package com.adrian.user_interface;

public enum GameState {
	Menu(0),
    Continue(1),
    NewGame(2),
    Pause(100),
    Dialogue(101);

    public final int state;

    private GameState(int state) {
        this.state = state;
	}
}
