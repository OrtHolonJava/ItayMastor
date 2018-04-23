package com.itay347.finaldays.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.itay347.finaldays.AI.AIManager;
import com.itay347.finaldays.AI.AINode;
import com.itay347.finaldays.MyValues;

public class Enemy extends BasicActor {
    private Vector2 moveDirection;
    private Vector2 lookDirection;

    public Enemy(int xTile, int yTile, Texture texture, World world) {
        super(xTile, yTile, texture, texture.getWidth() / 6, texture.getHeight() / 6, world, MyValues.SPEED_ENEMY,
                (short) (MyValues.ENTITY_PLAYER | MyValues.ENTITY_WALL), MyValues.ENTITY_ENEMY);
        moveDirection = new Vector2();
        lookDirection = new Vector2();
    }

    @Override
    protected void updateMovementInformation() {
        Player player = getParent().findActor(Player.NAME);
        final float[] closestFraction = {1.0f};
        final Fixture[] closestFixture = {null};

        // Ray-cast to check if the player is in line of sight
        world.rayCast(new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fraction < closestFraction[0]) {
                    closestFraction[0] = fraction;
                    closestFixture[0] = fixture;
                }
                return 1;
            }
        }, this.body.getPosition().cpy(), player.body.getPosition().cpy());

        Vector2 target;
        boolean targetIsPlayer;
        // If the closest fixture is the player
        if ((closestFixture[0].getFilterData().categoryBits & MyValues.ENTITY_PLAYER) == MyValues.ENTITY_PLAYER) {
            target = player.body.getPosition().cpy();
            targetIsPlayer = true;
        } else {
            // Else, use path-finding to get next node and set it as the target
            AINode nextNode = AIManager.getInstance().findNextNode(this);
            Gdx.app.debug("node tile", "x = " + nextNode.tileX + "\t y = " + nextNode.tileY);
            target = nextNode.getCenterPoint();
            targetIsPlayer = false;
        }

        // Direction to target = target pos - enemy pos AND normalize the result
        Vector2 distanceToTarget = new Vector2(target.x, target.y);
        distanceToTarget.sub(this.getX(), this.getY());
        lookDirection.set(distanceToTarget);
        // if the target is a player, don't get too close
        if (!targetIsPlayer || distanceToTarget.len() > getWidth()) {
            moveDirection.set(distanceToTarget.cpy().nor());
        } else {
            moveDirection.set(0, 0);
        }

    }

    @Override
    protected boolean isApplyingMovement() {
        return moveDirection.x != 0 && moveDirection.y != 0;
    }

    @Override
    protected Vector2 getMoveDirection() {
        return moveDirection;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Rotate the enemy according to his move direction
        this.setRotation(lookDirection.angle());
    }
}
