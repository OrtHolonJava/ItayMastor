package com.itay347.finaldays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

/**
 * This class handles the movement input of the player
 */
public class GameInput {
    /**
     * Vector that contains the direction to move the player
     */
    public static Vector2 KeyForce = new Vector2();
    /**
     * Were the movement keys pressed
     */
    public static boolean KeyPressed = false;

    /**
     * Update the movement input
     */
    public static void update() {
        KeyForce.y = 0;
        KeyPressed = false;
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            KeyForce.y += 1;
            KeyPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            KeyForce.y -= 1;
            KeyPressed = true;
        }

        KeyForce.x = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            KeyForce.x -= 1;
            KeyPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            KeyForce.x += 1;
            KeyPressed = true;
        }
    }
}
