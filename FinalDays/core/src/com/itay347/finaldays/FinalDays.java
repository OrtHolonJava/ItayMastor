package com.itay347.finaldays;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.itay347.finaldays.Screens.PlayScreen;

public class FinalDays extends Game {

    private AssetManager assetManager;

	@Override
	public void create() {
        assetManager = new AssetManager();
		setScreen(new PlayScreen(this));
	}

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
