package com.itay347.finaldays.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

public abstract class BasicActor extends Actor {
    protected TextureRegion textureRegion;
    protected Body body;

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

    public BasicActor(Texture texture, World world) {
        this(texture, texture.getWidth(), texture.getHeight(), world, (short) 0, (short) 0);
    }

    public BasicActor(Texture texture, int width, int height, World world, short categoryBits, short maskBits) {
        textureRegion = new TextureRegion(texture);
        setSize(width, height);
        this.setOrigin(getWidth() / 2, getHeight() / 2);
        createBody(world, categoryBits, maskBits);
    }

    private void createBody(World world, short categoryBits, short maskBits) {
        // Now create a BodyDefinition.  This defines the physics objects type
        //and position in the simulation
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // We are going to use 1 to 1 dimensions.  Meaning 1 in physics engine
        // is 1 pixel
        // Set our body to the same position as our sprite
        bodyDef.position.set(this.getX() + this.getWidth() / 2, this.getY() + this.getWidth() / 2);

        // Create a body in the world using our definition
        body = world.createBody(bodyDef);

        // Now define the dimensions of the physics shape
        CircleShape shape = new CircleShape();
        shape.setRadius(Math.min(this.getWidth() / 2, this.getHeight() / 2) * 0.8f);
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
        fixtureDef.filter.categoryBits = categoryBits;
        fixtureDef.filter.maskBits = maskBits;

        Fixture fixture = body.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
    }

    public void updateBody() {
        // update GameInput for player and calculate for AI
        updateMovementInformation();

        if (isApplyingMovement()) {
//            Gdx.app.debug("keypress", "Applying impulse: " + GameInput.KeyForce);
            body.applyLinearImpulse(getMoveDirection().cpy().scl(20000), body.getWorldCenter(), true);
        }

        // Friction
        applyFriction();
    }

    protected abstract void updateMovementInformation();

    protected abstract boolean isApplyingMovement();

    protected abstract Vector2 getMoveDirection();

    private void applyFriction() {
        body.applyForceToCenter(body.getLinearVelocity().cpy().scl(-1, -1).scl(5000), true);
    }

    public void updateAfterWorldStep() {
        this.setPosition(body.getPosition().x, body.getPosition().y, Align.center);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
