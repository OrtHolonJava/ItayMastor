package com.itay347.finaldays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.itay347.finaldays.Actors.MyActor;

public class PlayScreen extends ScreenAdapter {
    private Stage stage;
    private MyActor tempActor;

    public PlayScreen() {
        stage = new Stage();
        tempActor = new MyActor();
        stage.addActor(tempActor);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.act(Math.min(delta, 1/30f));
        stage.draw();
    }

    @Override
    public void dispose() {
        tempActor.getSprite().getTexture().dispose();
        stage.dispose();
    }
}
