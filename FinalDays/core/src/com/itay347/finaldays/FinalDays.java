package com.itay347.finaldays;

import com.badlogic.gdx.Game;

public class FinalDays extends Game {

	@Override
	public void create() {
		setScreen(new PlayScreen());
	}

    @Override
    public void dispose() {
        super.dispose();
        // TODO: Dispose AssetsManager and such
    }
}
