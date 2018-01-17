package com.itay347.finaldays.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.itay347.finaldays.GameInput;

public class MyActor extends Actor {
    private TextureRegion textureRegion;
    private float speed;

    public MyActor(Texture texture) {
        textureRegion = new TextureRegion(texture);
        setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        speed = 2.5f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        GameInput.update();
        setPosition(getX() + GameInput.KeyForce.x * speed, getY() + GameInput.KeyForce.y * speed);
    }
}
