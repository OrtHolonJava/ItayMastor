package com.itay347.finaldays.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.itay347.finaldays.Actors.MyActor;
import com.itay347.finaldays.FinalDays;

public class PlayScreen extends ScreenAdapter {
    private FinalDays game;

    private Stage stage;
    private MyActor tempActor;

    public PlayScreen(FinalDays game) {
        this.game = game;
        game.getAssetManager().load("badlogic.jpg", Texture.class);
        game.getAssetManager().finishLoadingAsset("badlogic.jpg");

        stage = new Stage();
        tempActor = new MyActor((Texture) game.getAssetManager().get("badlogic.jpg"));
        stage.addActor(tempActor);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update the stage
        stage.act(Math.min(delta, 1 / 30f));
        // draw the stage
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
