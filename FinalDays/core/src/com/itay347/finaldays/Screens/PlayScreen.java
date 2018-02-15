package com.itay347.finaldays.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.itay347.finaldays.Actors.Player;
import com.itay347.finaldays.FinalDays;

public class PlayScreen extends ScreenAdapter {
    public static final String MAP_FILE_NAME = "Maps\\FinalDaysMap.tmx";
    public static final String PLAYER_IMAGE = "survivor-move_handgun_0.png";
    private FinalDays game;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private Stage stage;
    private Player player;
    private Vector2 moveDirection;

    public PlayScreen(FinalDays game) {
        this.game = game;
        game.getAssetManager().load(PLAYER_IMAGE, Texture.class);
        game.getAssetManager().finishLoadingAsset(PLAYER_IMAGE);

//        // only needed once
//        game.getAssetManager().setLoader(TiledMap.class, new TmxMapLoader());
//        game.getAssetManager().load(MAP_FILE_NAME, TiledMap.class);
//        game.getAssetManager().finishLoadingAsset(MAP_FILE_NAME);
//        // once the asset manager is done loading
//        tiledMap = game.getAssetManager().get(MAP_FILE_NAME);
//        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Load the tiled map
        tiledMap = new TmxMapLoader().load(MAP_FILE_NAME);
        // TODO: Use this for collision making
//        ((TiledMapTileLayer)tiledMap.getLayers().get(0)).getCell(0, 0).setTile(tiledMap.getTileSets().getTile(10));
        Gdx.app.debug("Does have collision", ((TiledMapTileLayer)tiledMap.getLayers().get(0))
                .getCell(16, 16).getTile().getProperties().containsKey("Collision") ? "True" : "False");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Init the Box2D World
        world = new World(Vector2.Zero, false); // TODO: maybe change doSleep for better performance
        box2DDebugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);

        // Init the Stage and add the player
        stage = new Stage();
        player = new Player((Texture) game.getAssetManager().get(PLAYER_IMAGE));
        stage.addActor(player);

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
        // TODO: Make the camera move smoother
        // Move the camera above the player
        stage.getCamera().position.set(player.getX() + player.getWidth() / 2,
                player.getY() + player.getHeight() / 2, 0);
        // TODO: zoom in and out properly
        ((OrthographicCamera)stage.getCamera()).zoom = 0.5f;
        // draw the map
        tiledMapRenderer.setView((OrthographicCamera) stage.getCamera());
        tiledMapRenderer.render();

        // draw the stage (player and enemies)
        stage.draw();

        // render box2D Debug
        box2DDebugRenderer.render(world, stage.getCamera().combined);
    }

    @Override
    public void resize(int width, int height) {
        ((OrthographicCamera) stage.getCamera()).setToOrtho(false, width, height);
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
        world.dispose();
        stage.dispose();
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
