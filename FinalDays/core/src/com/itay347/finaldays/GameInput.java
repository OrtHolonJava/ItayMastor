package com.itay347.finaldays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class GameInput {
    public static Vector2 KeyForce = new Vector2();
    // TODO: maybe replace KeyPressed with a (KeyForce == 0) type of method
    public static boolean KeyPressed = false;

    public static void update() {
        KeyForce.y = 0;
        KeyPressed = false;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            KeyForce.y += 1;
            KeyPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            KeyForce.y -= 1;
            KeyPressed = true;
        }

        KeyForce.x = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            KeyForce.x -= 1;
            KeyPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            KeyForce.x += 1;
            KeyPressed = true;
        }
    }
}
