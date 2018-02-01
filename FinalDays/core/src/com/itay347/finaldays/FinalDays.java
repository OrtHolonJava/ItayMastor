package com.itay347.finaldays;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.itay347.finaldays.Screens.PlayScreen;

public class FinalDays extends Game {

    private AssetManager assetManager;

	@Override
	public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
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
