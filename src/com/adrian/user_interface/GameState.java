package com.adrian.user_interface;

public enum GameState {
	Menu(0),
    Continue(1),
    NewGame(2),
    ShowStat(3),
    Pause(100),
    Dialogue(101);

    public final int state;

    private GameState(int state) {
        this.state = state;
	}
}
