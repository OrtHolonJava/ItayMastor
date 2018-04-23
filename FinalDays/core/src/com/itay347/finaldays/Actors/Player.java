package com.itay347.finaldays.Actors;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.itay347.finaldays.GameInput;
import com.itay347.finaldays.MyValues;

/**
 * The actor class of the player
 */
public class Player extends BasicActor {
    public static final String NAME = "Player";

    /**
     * The small, bright cone light
     */
    private ConeLight coneLight1;
    /**
     * The large, less bright cone light
     */
    private ConeLight coneLight2;
    /**
     * The point light on top of the player
     */
    private PointLight pointLight;

    /**
     * Init the player
     *
     * @param xTile      the tile x index to start at
     * @param yTile      the tile y index to start at
     * @param texture    the texture for drawing the player
     * @param world      A reference to the Box2D world (used to create the body of the player)
     * @param rayHandler A reference to the ray handler (used to init the light objects)
     */
    public Player(int xTile, int yTile, Texture texture, World world, RayHandler rayHandler) {
        super(xTile, yTile, texture, texture.getWidth() / 6, texture.getHeight() / 6, world, MyValues.SPEED_PLAYER,
                MyValues.ENTITY_PLAYER, (short) (MyValues.ENTITY_ENEMY | MyValues.ENTITY_WALL));
        setName(NAME);

        // Init the player's lights
        coneLight1 = new ConeLight(rayHandler, 500, new Color(1, 1, 1, 1), 1000, 0, 0, 0, 35);
        coneLight2 = new ConeLight(rayHandler, 500, new Color(1, 1, 1, 0.2f), 1000, 0, 0, 0, 80);
        pointLight = new PointLight(rayHandler, 100, new Color(1, 1, 1, 1), 125, 0, 0);
    }

    /**
     * Update the player's movement information
     */
    @Override
    protected void updateMovementInformation() {
        GameInput.update();
    }

    /**
     * @return Whether or not the player "wants" to move
     */
    @Override
    protected boolean isApplyingMovement() {
        return GameInput.KeyPressed;
    }

    /**
     * @return The direction to move the player
     */
    @Override
    protected Vector2 getMoveDirection() {
        return GameInput.KeyForce;
    }

    /**
     * Called when stage.act(...) is called (after the movement updates)
     *
     * @param delta The delta time
     */
    @Override
    public void act(float delta) {
        super.act(delta);

        // Rotate the player according to the mouse
        Vector2 dir = new Vector2(Gdx.input.getX() - getStage().getCamera().viewportWidth / 2,
                Gdx.input.getY() - getStage().getCamera().viewportHeight / 2);
        this.setRotation(-dir.angle()); // minus the angle because it's flipped without it...

        // Set and rotate the lights according to the players position and the mouse's direction
        coneLight1.setPosition(body.getPosition());
        coneLight1.setDirection(-dir.angle());
        coneLight2.setPosition(body.getPosition());
        coneLight2.setDirection(-dir.angle());
        pointLight.setPosition(body.getPosition());
    }
}
