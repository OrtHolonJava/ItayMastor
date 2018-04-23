package com.itay347.finaldays.AI;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.itay347.finaldays.MyValues;
import com.itay347.finaldays.Screens.PlayScreen;

public class AINode {
    public final int tileX;
    public final int tileY;
    private final int index;
    private final Array<Connection<AINode>> connections;

    public AINode(int tileX, int tileY) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.index = tileX + tileY * PlayScreen.getWalls().length;
        this.connections = new Array<Connection<AINode>>();
    }

    public boolean isWall() {
        return PlayScreen.getWalls()[tileX][tileY];
    }

    public int getIndex() {
        return index;
    }

    public Vector2 getCenterPoint() {
        return new Vector2(MyValues.tileToPos(tileX), MyValues.tileToPos(tileY));
    }

    public Array<Connection<AINode>> getConnections() {
        return connections;
    }
}
