package com.itay347.finaldays.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BasicActor extends Actor {
    protected TextureRegion textureRegion;
    protected float maxSpeed;

    public BasicActor(Texture texture) {
        textureRegion = new TextureRegion(texture);
        setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    }

    public BasicActor(Texture texture, int width, int height) {
        textureRegion = new TextureRegion(texture);
        setSize(width, height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
