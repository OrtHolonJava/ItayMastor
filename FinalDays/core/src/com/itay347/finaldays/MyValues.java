package com.itay347.finaldays;

public class MyValues {
    // Values used in collision and light filters
    public static final short ENTITY_PLAYER = 0x1;
    public static final short ENTITY_ENEMY = 0x1 << 1;
    public static final short ENTITY_WALL = 0x1 << 2;
    public static final short ENTITY_LIGHT = 0x1 << 2;

    // Values used for movement speed and "physics"
    public static final float FRICTION_CONST = 5000;
    public static final float SPEED_PLAYER = 20000;
    public static final float SPEED_ENEMY = 15000;
}
