package com.itay347.finaldays.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.itay347.finaldays.AI.AIManager;
import com.itay347.finaldays.AI.AINode;
import com.itay347.finaldays.MyValues;

/**
 * The basic class of an actor
 */
public abstract class BasicActor extends Actor {
    /**
     * Contains the texture to draw when rendering the character
     */
    protected TextureRegion textureRegion;
    /**
     * A reference to the world used for ray-casting
     */
    protected World world;
    /**
     * The Box2D body of the character
     */
    protected Body body;

    /**
     * A value indicating how fast the character moves (not actual speed units)
     */
    protected float speed;
    /**
     * Maximum health points
     */
    protected int maxHp;
    /**
     * Current health points
     */
    protected int hp;
    /**
     * Output damage per hit
     */
    protected int damage;
    /**
     * The delay between hits
     */
    protected float hitDelay;
    /**
     * Current time until able to hit again
     */
    protected float hitCooldown;

    /**
     * Init an actor (called from the subclasses)
     *
     * @param xTile        the tile x index to start at
     * @param yTile        the tile y index to start at
     * @param texture      the texture for drawing the actor
     * @param width        The width to draw the actor's texture
     * @param height       The height to draw the actor's texture
     * @param world        A reference to the Box2D world (used to create the body of the actor)
     * @param speed        the speed of the actor
     * @param categoryBits the category for the body's filter
     * @param maskBits     the mask for the body's filter
     */
    public BasicActor(int xTile, int yTile, Texture texture, int width, int height, World world, float speed,
                      short categoryBits, short maskBits) {
        textureRegion = new TextureRegion(texture);
        setSize(width, height);
        this.setOrigin(getWidth() / 2, getHeight() / 2);
        this.setPosition(MyValues.tileToPos(xTile), MyValues.tileToPos(yTile), Align.center);
        this.speed = speed;
        this.world = world;
        createBody(categoryBits, maskBits);
    }

    /**
     * Create the Box2D Body for the actor
     *
     * @param categoryBits the category for the body's filter
     * @param maskBits     the mask for the body's filter
     */
    private void createBody(short categoryBits, short maskBits) {
        // Now create a BodyDefinition.  This defines the physics objects type
        //and position in the simulation
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // We are going to use 1 to 1 dimensions.  Meaning 1 in physics engine
        // is 1 pixel
        // Set our body to the same position as our actor
        bodyDef.position.set(this.getX() + this.getWidth() / 2, this.getY() + this.getWidth() / 2);

        // Create a body in the world using our definition
        body = world.createBody(bodyDef);

        // Now define the dimensions of the physics shape
        CircleShape shape = new CircleShape();
        shape.setRadius(Math.min(this.getWidth() / 2, this.getHeight() / 2) * 0.8f);

        // FixtureDef ("physical properties" of the body)
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = categoryBits;
        fixtureDef.filter.maskBits = maskBits;
        body.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
    }

    /**
     * update the actor's body
     */
    public void updateBody() {
        // update GameInput for player and calculate for AI
        updateMovementInformation();

        if (isApplyingMovement()) {
            body.applyLinearImpulse(getMoveDirection().cpy().scl(speed), body.getWorldCenter(), true);
        }

        // Friction
        applyFriction();
    }

    /**
     * Update the actor's movement information
     */
    protected abstract void updateMovementInformation();

    /**
     * @return Whether or not the actor "wants" to move
     */
    protected abstract boolean isApplyingMovement();

    /**
     * @return The direction to move the actor
     */
    protected abstract Vector2 getMoveDirection();

    /**
     * Applies friction on the body
     */
    private void applyFriction() {
        body.applyForceToCenter(body.getLinearVelocity().cpy().scl(-1, -1).scl(MyValues.FRICTION_CONST), true);
    }

    /**
     * Apply after world step things
     */
    public void updateAfterWorldStep() {
        syncActorPosToBodyPos();
    }

    /**
     * Set's the actor's position to the body position
     */
    public void syncActorPosToBodyPos() {
        this.setPosition(body.getPosition().x, body.getPosition().y, Align.center);
    }

    /**
     * @return The node at the character's position
     */
    public AINode getNodeOfMyPosition() {
        return AIManager.getInstance().getGraph().getNodeAt(
                MyValues.posToTile(MathUtils.round(this.getX(Align.center))),
                MyValues.posToTile(MathUtils.round(this.getY(Align.center)))
        );
    }


    /**
     * Teleport the player the the tile index specified
     *
     * @param x the tile's x index
     * @param y the tile's y index
     */
    public void setPositionByTileIndex(int x, int y) {
        body.getPosition().set(MyValues.tileToPos(x), MyValues.tileToPos(y));
        syncActorPosToBodyPos();
    }

    /**
     * Draws the actor
     *
     * @param batch       the sprite batch
     * @param parentAlpha the parent's alpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
