package com.itay347.finaldays;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.itay347.finaldays.Screens.MenuScreen;
import com.itay347.finaldays.Screens.PlayScreen;

/**
 * The Game class which handles the screens
 */
public class FinalDays extends Game {
    /**
     * Used to load the assets
     */
    private AssetManager assetManager;

    /**
     * Init the game
     */
    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        assetManager = new AssetManager();
        setScreen(new MenuScreen(this));
    }

    /**
     * Clears the resources when the game is closed
     */
    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }

    /**
     * @return the asset manager
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }
}
