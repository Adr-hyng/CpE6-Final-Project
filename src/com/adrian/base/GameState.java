package com.adrian.base;

public enum GameState {
	Menu(0),
    Continue(1),
    NewGame(2),
    ShowStat(3),
    ShowOption(4),
    Pause(100),
    Dialogue(101);

    public final int state;

    private GameState(int state) {
        this.state = state;
	}
}
