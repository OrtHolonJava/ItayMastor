package com.itay347.finaldays.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.itay347.finaldays.FinalDays;

/**
 * The launcher of the game for desktop
 */
public class DesktopLauncher {
    /**
     * The main method which starts the game
     *
     * @param arg Arguments
     */
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 960;
        config.height = 540;
        new LwjglApplication(new FinalDays(), config);
    }
}
