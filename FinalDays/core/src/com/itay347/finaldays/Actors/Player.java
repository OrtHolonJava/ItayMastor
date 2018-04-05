package com.itay347.finaldays.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.itay347.finaldays.GameInput;

public class Player extends BasicActor {

    public Player(Texture texture, World world) {
        super(texture, texture.getWidth() / 6, texture.getHeight() / 6, world);
    }

    @Override
    protected void updateMovementInformation() {
        GameInput.update();
    }

    @Override
    protected boolean isApplyingMovement() {
        return GameInput.KeyPressed;
    }

    @Override
    protected Vector2 getMoveDirection() {
        return GameInput.KeyForce;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Rotate the player according to the mouse
        Vector2 dir = new Vector2(Gdx.input.getX() - getStage().getWidth() / 2,
                Gdx.input.getY() - getStage().getHeight() / 2);
        this.setRotation(-dir.angle()); // minus the angle because it's flipped without it...
    }
}
