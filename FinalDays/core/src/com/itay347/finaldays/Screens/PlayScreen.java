package com.itay347.finaldays.Screens;

import box2dLight.Light;
import box2dLight.RayHandler;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.itay347.finaldays.Actors.BasicActor;
import com.itay347.finaldays.Actors.Enemy;
import com.itay347.finaldays.Actors.Player;
import com.itay347.finaldays.FinalDays;
import com.itay347.finaldays.MyValues;

public class PlayScreen extends ScreenAdapter {
    public static final String MAP_FILE_NAME = "Maps\\FinalDaysMap.tmx";
    public static final String PLAYER_IMAGE = "survivor-move_handgun_0.png";
    public static final String ENEMY_IMAGE = "skeleton-idle_0.png";

    private FinalDays game;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private static int TileSize;
    private boolean[][] walls;

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private boolean drawBox2DDebug;
    private RayHandler rayHandler;
    private boolean enableLights;

    private Stage stage;
    private Player player;
    private Enemy testEnemy;
    private Array<Enemy> enemies;

    public PlayScreen(FinalDays game) {
        this.game = game;
        game.getAssetManager().load(PLAYER_IMAGE, Texture.class);
        game.getAssetManager().load(ENEMY_IMAGE, Texture.class);

        drawBox2DDebug = false;
        enableLights = true;

        // Load the tiled map
        tiledMap = new TmxMapLoader().load(MAP_FILE_NAME);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        saveTileSize();
        findAndSaveWalls();

        // Init the Box2D World
        world = new World(Vector2.Zero, false); // TODO: maybe change doSleep for better(?) performance
        box2DDebugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);

        // Init the RayHandler for the lights
        rayHandler = new RayHandler(world);
        RayHandler.setGammaCorrection(true);     // enable or disable gamma correction
        RayHandler.useDiffuseLight(true);        // enable or disable diffused lighting
        rayHandler.setBlur(true);                // enabled or disable blur
        rayHandler.setBlurNum(5);                // set number of gaussian blur passes
        rayHandler.setShadows(true);             // enable or disable shadow
        rayHandler.setCulling(true);             // enable or disable culling
        rayHandler.setAmbientLight(0f);          // set default ambient light
        // Set lights to only affect walls
        Light.setGlobalContactFilter(MyValues.ENTITY_LIGHT, (short) 0, MyValues.ENTITY_WALL);

        // Init the Stage and add the player
        stage = new Stage();
        game.getAssetManager().finishLoadingAsset(PLAYER_IMAGE);
        int startX = 25;
        int startY = 25;
        player = new Player(startX, startY, (Texture) game.getAssetManager().get(PLAYER_IMAGE), world, rayHandler);
        stage.addActor(player);

        game.getAssetManager().finishLoadingAsset(ENEMY_IMAGE);
        testEnemy = new Enemy(25, 0, (Texture) game.getAssetManager().get(ENEMY_IMAGE), world);
        stage.addActor(testEnemy);
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

        for (Actor actor : stage.getActors()) {
            if (actor instanceof BasicActor) {
                ((BasicActor) actor).updateBody();
            }
        }
        world.step(myDelta, 6, 2);
        for (Actor actor : stage.getActors()) {
            if (actor instanceof BasicActor) {
                ((BasicActor) actor).updateAfterWorldStep();
            }
        }

        // update the stage
        stage.act(myDelta);


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

        if (enableLights) {
            // Apply lights
            this.rayHandler.setCombinedMatrix((OrthographicCamera) stage.getCamera());
            this.rayHandler.updateAndRender();
        }

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
     * Save the tile indexes of walls in a 2D array of booleans "walls"
     */
    private void findAndSaveWalls() {
        TiledMapTileLayer mapLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Main Layer");

        walls = new boolean[mapLayer.getWidth()][mapLayer.getHeight()];
        for (int x = 0; x < walls.length; x++) {
            for (int y = 0; y < walls[x].length; y++) {
                walls[x][y] = mapLayer.getCell(x, y).getTile().getProperties().containsKey("Collision");
            }
        }
    }

    /**
     * Create Box2D Bodies for wall collisions
     */
    private void createWallColliders() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(TileSize / 2, TileSize / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = MyValues.ENTITY_WALL;
        fixtureDef.filter.maskBits = MyValues.ENTITY_PLAYER | MyValues.ENTITY_ENEMY | MyValues.ENTITY_LIGHT;

        for (int x = 0; x < walls.length; x++) {
            for (int y = 0; y < walls[x].length; y++) {
                if (walls[x][y]) {
                    bodyDef.position.set(TileSize * x + TileSize / 2, TileSize * y + TileSize / 2);
                    Body body = world.createBody(bodyDef);
                    body.createFixture(fixtureDef);
                    // TODO: maybe save all the bodies in a list
                }
            }
        }
        shape.dispose();
    }

    /**
     * Save the pixel size of one map tile in a static field
     */
    private void saveTileSize() {
        PlayScreen.TileSize = tiledMap.getProperties().get("tilewidth", Integer.class);
    }

    /**
     * @return the pixel size of one map tile
     */
    public static int getTileSize() {
        return TileSize;
    }

    /**
     * Initialize the input processor
     */
    private void initInputProcessor() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        // TODO: add the uiStage to the multiplexer
//        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                // Toggle Box2DDebug drawing
                if (keycode == Input.Keys.NUMPAD_0) {
                    drawBox2DDebug = !drawBox2DDebug;
                    return true;
                }
                // Toggle lights
                if (keycode == Input.Keys.NUMPAD_1) {
                    enableLights = !enableLights;
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);
    }
}
