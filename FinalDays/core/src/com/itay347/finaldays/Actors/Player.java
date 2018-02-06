package com.itay347.finaldays.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.itay347.finaldays.GameInput;

public class Player extends BasicActor {
    private Vector2 velocity;
    private Vector2 maxVelocity;
    private float timeToMaxSpeed;

    public Player(Texture texture) {
        super(texture);
        setSize(getWidth() / 4, getHeight() / 4);
        velocity = Vector2.Zero;
        maxSpeed = 2.5f;
        maxVelocity = GameInput.KeyForce.cpy().scl(maxSpeed);
        Gdx.app.debug("Player constructor", "max velocity" + maxVelocity.toString());
        timeToMaxSpeed = 0.5f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        Gdx.app.debug("Player", "acting");

        GameInput.update();
        if (GameInput.KeyPressed) {
            maxVelocity = GameInput.KeyForce.cpy().scl(maxSpeed);
            velocity.mulAdd(maxVelocity, delta / timeToMaxSpeed);
            Gdx.app.debug("Player movement", "key pressed, velocity = " + velocity.toString());
        } else {
            velocity.mulAdd(velocity, -delta / timeToMaxSpeed);
            if (Math.abs(velocity.x) < 0.1 && Math.abs(velocity.y) < 0.1) {
                velocity.x = 0;
                velocity.y = 0;
                Gdx.app.debug("Player movement", "set velocity to ZERO");
            }
            Gdx.app.debug("Player movement", "not pressed, velocity = " + velocity.toString());
        }
        velocity.clamp(0, maxSpeed);
        setPosition(getX() + velocity.x, getY() + velocity.y);
    }
}
