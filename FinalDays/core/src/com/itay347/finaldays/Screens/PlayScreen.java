package com.itay347.finaldays.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
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
    private Body playerBody;

    private Stage stage;
    private Player player;
    private Vector2 keyPressDirection;

    private Vector2 previousVelocity;

    private boolean drawBox2DDebug;

    public PlayScreen(FinalDays game) {
        this.game = game;
        game.getAssetManager().load(PLAYER_IMAGE, Texture.class);
        game.getAssetManager().finishLoadingAsset(PLAYER_IMAGE);

        drawBox2DDebug = true;
        keyPressDirection = Vector2.Zero;
        previousVelocity = null;

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
        Gdx.app.debug("Does have collision", ((TiledMapTileLayer) tiledMap.getLayers().get(0))
                .getCell(16, 16).getTile().getProperties().containsKey("Collision") ? "True" : "False");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Init the Box2D World
        world = new World(Vector2.Zero, false); // TODO: maybe change doSleep for better performance
        box2DDebugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);

        // Init the Stage and add the player
        stage = new Stage();
        player = new Player((Texture) game.getAssetManager().get(PLAYER_IMAGE));
        stage.addActor(player);

        createPlayerBody();

        initInputProcessor();
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        GameInput.update();
        if (GameInput.KeyPressed) {
//            Gdx.app.debug("keypress", "Applying force: " + GameInput.KeyForce);
//            playerBody.applyForceToCenter(GameInput.KeyForce.cpy().scl(100000f), true);

            Gdx.app.debug("keypress", "Applying impulse: " + GameInput.KeyForce);
            playerBody.applyLinearImpulse(GameInput.KeyForce.cpy().scl(20000), playerBody.getWorldCenter(), true);
        }
        // TODO: Friction
        if (previousVelocity != null)
        {
            // Unnecessary
//            Vector2 acceleration = playerBody.getLinearVelocity().cpy().sub(previousVelocity).scl(1f / delta);
            playerBody.applyForceToCenter(playerBody.getLinearVelocity().cpy().scl(-1, -1).scl(5000), true);
        }

        world.step(Math.min(delta, 1 / 30f), 6, 2);
        player.setPosition(playerBody.getPosition().x, playerBody.getPosition().y, Align.center);

        previousVelocity = playerBody.getLinearVelocity();

        // update the stage
//        stage.act(Math.min(delta, 1 / 30f));
        // TODO: Make the camera move smoother
        // Move the camera above the player
        stage.getCamera().position.set(player.getX() + player.getWidth() / 2,
                player.getY() + player.getHeight() / 2, 0);
        // TODO: zoom in and out properly
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

    private void createPlayerBody() {
        // TODO: Move to BasicActor

        // Now create a BodyDefinition.  This defines the physics objects type
        //and position in the simulation
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // We are going to use 1 to 1 dimensions.  Meaning 1 in physics engine
        // is 1 pixel
        // Set our body to the same position as our sprite
        bodyDef.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getWidth() / 2);

        // Create a body in the world using our definition
        playerBody = world.createBody(bodyDef);

        // Now define the dimensions of the physics shape
        CircleShape shape = new CircleShape();
        shape.setRadius(Math.min(player.getWidth() / 2, player.getHeight() / 2) * 0.8f);
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(player.getWidth() / 2, player.getHeight() / 2);

        // FixtureDef is a confusing expression for physical properties
        // Basically this is where you, in addition to defining the shape of the
        // body
        // you also define it's properties like density, restitution and others
        // we will see shortly
        // If you are wondering, density and area are used to calculate over all
        // mass
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Fixture fixture = playerBody.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
    }

    private void initInputProcessor() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        // TODO: add the uiStage to the multiplexer
//        multiplexer.addProcessor(new InputAdapter() {
//            @Override
//            public boolean keyDown(int keycode) {
//                switch (keycode) {
//                    case Input.Keys.W:
//                        keyPressDirection.y = 1;
//                        return true;
//                    case Input.Keys.A:
//                        keyPressDirection.x = -1;
//                        return true;
//                    case Input.Keys.S:
//                        keyPressDirection.y = -1;
//                        return true;
//                    case Input.Keys.D:
//                        keyPressDirection.x = 1;
//                        return true;
//                }
//                return false;
//            }
//
//            @Override
//            public boolean keyUp(int keycode) {
//                switch (keycode) {
//                    case Input.Keys.W:
//                        keyPressDirection.y = 0;
//                        break;
//                    case Input.Keys.A:
//                        keyPressDirection.x = 0;
//                        break;
//                    case Input.Keys.S:
//                        keyPressDirection.y = 0;
//                        break;
//                    case Input.Keys.D:
//                        keyPressDirection.x = 0;
//                        break;
//                }
//                return true;
//            }
//        });
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
