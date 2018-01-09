package com.itay347.finaldays.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MyActor extends Actor {

    Sprite sprite;

    public MyActor() {
        super();
        sprite = new Sprite(new Texture(Gdx.files.internal("badlogic.jpg")));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        sprite.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public Sprite getSprite() {
        return sprite;
    }
}
