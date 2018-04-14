package com.itay347.finaldays.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.itay347.finaldays.MyValues;

public class Enemy extends BasicActor {
    private Vector2 moveDirection;
    private Vector2 lookDirection;

    public Enemy(Texture texture, World world) {
        super(texture, texture.getWidth() / 6, texture.getHeight() / 6, world, MyValues.SPEED_ENEMY,
                (short) (MyValues.ENTITY_PLAYER | MyValues.ENTITY_WALL), MyValues.ENTITY_ENEMY);
        moveDirection = new Vector2();
        lookDirection = new Vector2();
    }

    @Override
    protected void updateMovementInformation() {
        Player player = getParent().findActor(Player.NAME);
        // Direction to player = player pos - enemy pos AND normalize the result
        Vector2 distanceToTarget = new Vector2(player.getX(), player.getY());
        distanceToTarget.sub(this.getX(), this.getY());
        lookDirection.set(distanceToTarget);
        if (distanceToTarget.len() > getWidth()) {
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
