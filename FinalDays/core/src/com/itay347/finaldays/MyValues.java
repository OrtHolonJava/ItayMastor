package com.itay347.finaldays;

import com.itay347.finaldays.Screens.PlayScreen;

/**
 * Contains game constants and helper methods
 */
public class MyValues {
    // Strings of file paths and names
    public static final String SKIN_FILE = "Skins\\glassy-ui.json";
    public static final String LOGO_FILE = "FinalDaysLogo.png";
    public static final String BACKGROUND_FILE = "MenuBackground.png";
    public static final String MAP_FILE = "Maps\\FinalDaysMap.tmx";
    public static final String PLAYER_IMAGE = "survivor-move_handgun_0.png";
    public static final String ENEMY_IMAGE = "skeleton-idle_0.png";

    // Values used in collision and light filters
    public static final short ENTITY_PLAYER = 0x1;
    public static final short ENTITY_ENEMY = 0x1 << 1;
    public static final short ENTITY_WALL = 0x1 << 2;
    public static final short ENTITY_LIGHT = 0x1 << 2;

    // Values used for movement speed and "physics"
    public static final float FRICTION_CONST = 5000;
    public static final float SPEED_PLAYER = 20000;
    public static final float SPEED_ENEMY = 15000;

    /**
     * @param index tile index at the x or y axis
     * @return the pixel position of the center of the tile
     */
    public static int tileToPos(int index) {
        return index * PlayScreen.getTileSize() + PlayScreen.getTileSize() / 2;
    }

    /**
     * @param pos pixel position at the x or y axis
     * @return the tile index of the given position
     */
    public static int posToTile(int pos) {
        return pos / PlayScreen.getTileSize();
    }
}
