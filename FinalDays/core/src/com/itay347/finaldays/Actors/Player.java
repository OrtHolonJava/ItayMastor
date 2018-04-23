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

public class Player extends BasicActor {
    public static final String NAME = "Player";

    /**
     * The small degree bright cone light
     */
    private ConeLight coneLight1;
    /**
     * The large degree less bright cone light
     */
    private ConeLight coneLight2;
    /**
     * The light on top of the player
     */
    private PointLight pointLight;

    public Player(int xTile, int yTile, Texture texture, World world, RayHandler rayHandler) {
        super(xTile, yTile, texture, texture.getWidth() / 6, texture.getHeight() / 6, world, MyValues.SPEED_PLAYER,
                (short) (MyValues.ENTITY_ENEMY | MyValues.ENTITY_WALL), MyValues.ENTITY_PLAYER);
        setName(NAME);

        // Init the player's lights
        coneLight1 = new ConeLight(rayHandler, 500, new Color(1, 1, 1, 1), 1000, 0, 0, 0, 35);
        coneLight2 = new ConeLight(rayHandler, 500, new Color(1, 1, 1, 0.2f), 1000, 0, 0, 0, 80);
        pointLight = new PointLight(rayHandler, 100, new Color(1, 1, 1, 1), 125, 0, 0);
    }

    @Override
    protected void updateMovementInformation() {
        GameInput.update();
    }

    @Override
    protected boolean isApplyingMovement() {
        return GameInput.KeyPressed;
    }

    @Override
    protected Vector2 getMoveDirection() {
        return GameInput.KeyForce;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Rotate the player according to the mouse
        Vector2 dir = new Vector2(Gdx.input.getX() - getStage().getCamera().viewportWidth / 2,
                Gdx.input.getY() - getStage().getCamera().viewportHeight / 2);
        this.setRotation(-dir.angle()); // minus the angle because it's flipped without it...

        coneLight1.setPosition(body.getPosition());
        coneLight1.setDirection(-dir.angle());
        coneLight2.setPosition(body.getPosition());
        coneLight2.setDirection(-dir.angle());
        pointLight.setPosition(body.getPosition());
    }
}
