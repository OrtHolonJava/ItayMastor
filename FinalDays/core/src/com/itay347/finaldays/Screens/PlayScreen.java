package com.itay347.finaldays.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.itay347.finaldays.Actors.BasicActor;
import com.itay347.finaldays.Actors.Player;
import com.itay347.finaldays.FinalDays;
import com.itay347.finaldays.GameInput;

public class PlayScreen extends ScreenAdapter {
    public static final String MAP_FILE_NAME = "Maps\\FinalDaysMap.tmx";
    public static final String PLAYER_IMAGE = "survivor-move_handgun_0.png";
    private FinalDays game;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private boolean drawBox2DDebug;

    private Stage stage;
    private Player player;

    public PlayScreen(FinalDays game) {
        this.game = game;
        game.getAssetManager().load(PLAYER_IMAGE, Texture.class);
        game.getAssetManager().finishLoadingAsset(PLAYER_IMAGE);

        drawBox2DDebug = false;

        // Load the tiled map
        tiledMap = new TmxMapLoader().load(MAP_FILE_NAME);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Init the Box2D World
        world = new World(Vector2.Zero, false); // TODO: maybe change doSleep for better(?) performance
        box2DDebugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);

        // Init the Stage and add the player
        stage = new Stage();
        player = new Player((Texture) game.getAssetManager().get(PLAYER_IMAGE), world);
        stage.addActor(player);
        // TODO: Add the enemy AI actors

        createWallColliders();
        initInputProcessor();
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float myDelta = Math.min(delta, 1 / 30f);

        for (Actor actor : stage.getActors() ) {
            if (actor instanceof BasicActor) {
                ((BasicActor) actor).updateBody();
            }
        }
        world.step(myDelta, 6, 2);
        for (Actor actor : stage.getActors() ) {
            if (actor instanceof BasicActor) {
                ((BasicActor) actor).updateAfterWorldStep();
            }
        }

        // update the stage
//        stage.act(myDelta);


        // TODO: maybe Make the camera move smoother
        // Move the camera above the player
        stage.getCamera().position.set(player.getX() + player.getWidth() / 2,
                player.getY() + player.getHeight() / 2, 0);

        // TODO: maybe zoom in and out according to distance of the mouse from the player
        ((OrthographicCamera) stage.getCamera()).zoom = 0.5f;


        // draw the map
        tiledMapRenderer.setView((OrthographicCamera) stage.getCamera());
        tiledMapRenderer.render();

        // draw the stage (player and enemies)
        stage.draw();

        // render box2D Debug
        if (drawBox2DDebug) {
            box2DDebugRenderer.render(world, stage.getCamera().combined);
        }
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

    /**
     * Create Box2D Bodies for wall collisions
     */
    private void createWallColliders() {
        TiledMapTileLayer mapLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        int tileSize = tiledMap.getProperties().get("tilewidth", Integer.class);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(tileSize / 2, tileSize / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        for (int y = 0; y < mapLayer.getHeight(); y++) {
            for (int x = 0; x < mapLayer.getWidth(); x++) {
                if (mapLayer.getCell(x, y).getTile().getProperties().containsKey("Collision")) {
                    bodyDef.position.set(tileSize * x + tileSize / 2, tileSize * y + tileSize / 2);
                    Body body = world.createBody(bodyDef);
                    body.createFixture(fixtureDef);
                    // TODO: maybe save all the bodies in a list
                }
            }
        }
        shape.dispose();
    }

    private void initInputProcessor() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        // TODO: add the uiStage to the multiplexer
        //multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.NUMPAD_0) {
                    drawBox2DDebug = !drawBox2DDebug;
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);
    }
}
