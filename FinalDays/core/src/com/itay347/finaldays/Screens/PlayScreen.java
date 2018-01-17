package com.itay347.finaldays.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.itay347.finaldays.Actors.MyActor;
import com.itay347.finaldays.FinalDays;

public class PlayScreen extends ScreenAdapter {
    public static final String MAP_FILE_NAME = "Maps\\FinalDaysMap.tmx";
    public static final String TEMP_IMAGE = "badlogic.jpg";
    private FinalDays game;

    private Stage stage;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private MyActor tempActor;
    private Vector2 moveDirection;

    public PlayScreen(FinalDays game) {
        this.game = game;
        game.getAssetManager().load(TEMP_IMAGE, Texture.class);
        game.getAssetManager().finishLoadingAsset(TEMP_IMAGE);

//        // only needed once
//        game.getAssetManager().setLoader(TiledMap.class, new TmxMapLoader());
//        game.getAssetManager().load(MAP_FILE_NAME, TiledMap.class);
//        game.getAssetManager().finishLoadingAsset(MAP_FILE_NAME);
//        // once the asset manager is done loading
//        tiledMap = game.getAssetManager().get(MAP_FILE_NAME);
//        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        tiledMap = new TmxMapLoader().load(MAP_FILE_NAME);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        stage = new Stage();
        tempActor = new MyActor((Texture) game.getAssetManager().get(TEMP_IMAGE));
        stage.addActor(tempActor);

        initInputProcessor();
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update the stage
        stage.act(Math.min(delta, 1 / 30f));
        // draw the map
        tiledMapRenderer.setView((OrthographicCamera) stage.getCamera());
        tiledMapRenderer.render();
        // draw the stage
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        ((OrthographicCamera) stage.getCamera()).setToOrtho(false, width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
        tiledMap.dispose();
    }

    private void initInputProcessor() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        // TODO: add the uiStage to the multiplexer
//        multiplexer.addProcessor(new InputAdapter() {
//            @Override
//            public boolean keyDown(int keycode) {
//                switch (keycode) {
//                    case Input.Keys.W:
//                        moveDirection.y = 1;
//                        return true;
//                    case Input.Keys.A:
//
//                        return true;
//                    case Input.Keys.S:
//
//                        return true;
//                    case Input.Keys.D:
//
//                        return true;
//                }
//                return false;
//            }
//
//            @Override
//            public boolean keyUp(int keycode) {
//                switch (keycode) {
//                    case Input.Keys.W:
//
//                        break;
//                    case Input.Keys.A:
//
//                        break;
//                    case Input.Keys.S:
//
//                        break;
//                    case Input.Keys.D:
//
//                        break;
//                }
//                return true;
//            }
//        });
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }
}
